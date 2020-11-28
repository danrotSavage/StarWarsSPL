package main.java.bgu.spl.mics.application.services;

import main.java.bgu.spl.mics.Message;
import main.java.bgu.spl.mics.MicroService;
import main.java.bgu.spl.mics.application.messages.DeactivationBroadcast;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {

    public LandoMicroservice(long duration) {
        super("Lando");
    }



    @Override
    protected void initialize() {
       
    }
}
