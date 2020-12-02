package main.java.bgu.spl.mics.application.services;

import main.java.bgu.spl.mics.Callback;
import main.java.bgu.spl.mics.Message;
import main.java.bgu.spl.mics.MicroService;
import main.java.bgu.spl.mics.application.messages.AttackEvent;


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

    }
    public void SolveEvent(AttackEvent a){
        callback.call(a);

    }

}
