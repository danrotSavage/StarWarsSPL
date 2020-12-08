package main.java.bgu.spl.mics.application.services;

import main.java.bgu.spl.mics.Messages.Future;
import main.java.bgu.spl.mics.MicroService;
import main.java.bgu.spl.mics.application.messages.*;
import main.java.bgu.spl.mics.application.passiveObjects.Attack;
import main.java.bgu.spl.mics.application.passiveObjects.Diary;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;
	private  Future[] f;
	
    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
        f=new Future[attacks.length];
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(TerminateBroadcast.class,(TerminateBroadcast d)->{
            this.terminate();
        });

        // send out attack to Han and C3PO
        for (int i = 0; i < attacks.length; i++) {

            AttackEvent e = new AttackEvent(attacks[i]);
            Future x=sendEvent(e);
            System.out.println("Lea sent att " + i);
            f[i]=x;
        }

        //wait for all attacks to be resolved
        for (int i = 0; i < attacks.length; i++) {
            f[i].get();
            System.out.println("future for attack " + i);
        }

        //send event to R2D2
        Future deactivate= sendEvent(new DeactivationEvent());
        deactivate.get();

        //send event to Lando
        Future blowUpDeathStar = sendEvent(new DestroyDeathStarEvent());
        blowUpDeathStar.get();

        //send broadcast to terminate
        sendBroadcast(new TerminateBroadcast ());
        Diary.getDiary().setLeiaTerminate(System.currentTimeMillis());
        this.terminate();


    }

}
