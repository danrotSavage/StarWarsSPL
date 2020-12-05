package main.java.bgu.spl.mics.application.services;

import java.util.ArrayList;
import java.util.List;

import main.java.bgu.spl.mics.MicroService;
import main.java.bgu.spl.mics.application.messages.DeactivationBroadcast;
import main.java.bgu.spl.mics.application.messages.DeathStarDestroyed;
import main.java.bgu.spl.mics.application.messages.DestroyTheSITH;
import main.java.bgu.spl.mics.application.passiveObjects.Attack;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as  {@link AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvents}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;
	
    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
    }

    @Override
    protected void initialize() {
        subscribeBroadcast(DeathStarDestroyed.class,(DeathStarDestroyed d)->{
            this.terminate();
        });



    }

}
