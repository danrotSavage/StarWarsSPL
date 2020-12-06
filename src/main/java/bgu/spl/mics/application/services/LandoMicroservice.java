package main.java.bgu.spl.mics.application.services;

import main.java.bgu.spl.mics.MicroService;
import main.java.bgu.spl.mics.application.messages.TerminateBroadcast;
import main.java.bgu.spl.mics.application.messages.DestroyDeathStarEvent;
import main.java.bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
    private long sleepTime;
    public LandoMicroservice(long duration) {

        super("Lando");
        sleepTime=duration;
    }



    @Override
    protected void initialize() {

        subscribeEvent(DestroyDeathStarEvent.class,(DestroyDeathStarEvent d)->{
            try {
                Thread.sleep(sleepTime);
            }catch (Exception e){}
            complete(d,3);

        });
        subscribeBroadcast(TerminateBroadcast.class,(TerminateBroadcast d)->{
            Diary.getDiary().setLandoTerminate(System.currentTimeMillis());

            this.terminate();
        });
    }







}
