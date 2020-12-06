package main.java.bgu.spl.mics.application.services;

import main.java.bgu.spl.mics.Messages.Callback;
import main.java.bgu.spl.mics.MicroService;
import main.java.bgu.spl.mics.application.messages.AttackEvent;
import main.java.bgu.spl.mics.application.messages.TerminateBroadcast;
import main.java.bgu.spl.mics.application.passiveObjects.Diary;
import main.java.bgu.spl.mics.application.passiveObjects.Ewoks;


/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {
	Callback callback;
    public C3POMicroservice() {
        super("C3PO");
    }


    @Override
    protected void initialize() {
        subscribeEvent(AttackEvent.class,(AttackEvent attack)-> {
            Ewoks e = Ewoks.getEwoks();
            e.fetchEwok(attack.getAttack().getSerials());
            try {
                Thread.sleep((long) attack.getAttack().getDuration());
            }catch (Exception ex){}

            Diary diary=Diary.getDiary();
            diary.addAttack();
            diary.setC3POFinish(System.currentTimeMillis());

            complete(attack,attack.getAttack());
            e.returnEwoks(attack.getAttack().getSerials());

        }
        );

        subscribeBroadcast(TerminateBroadcast.class,(TerminateBroadcast d)->{
            Diary.getDiary().setC3POTerminate(System.currentTimeMillis());
            this.terminate();
        });
    }



}
