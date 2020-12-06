package main.java.bgu.spl.mics.application.services;

import main.java.bgu.spl.mics.MicroService;
import main.java.bgu.spl.mics.application.messages.*;
import main.java.bgu.spl.mics.application.passiveObjects.Diary;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {
    private long sleepTime;
    public R2D2Microservice(long duration) {
        super("R2D2");
        sleepTime=duration;
    }



    @Override
    protected void initialize() {
        subscribeEvent(DeactivationEvent.class,(DeactivationEvent d)->{
            try {
                Thread.sleep(sleepTime);
            }catch (Exception e){}
            Diary diary=Diary.getDiary();
            diary.setR2D2Deactivate(System.currentTimeMillis());

            complete(d, 8);
        });
        subscribeBroadcast(TerminateBroadcast.class,(TerminateBroadcast d)->{
            Diary.getDiary().setR2D2Terminate(System.currentTimeMillis());
            this.terminate();
        });
    }
}
