package main.java.bgu.spl.mics;

import main.java.bgu.spl.mics.application.messages.AttackEvent;
import main.java.bgu.spl.mics.application.passiveObjects.Ewok;
import main.java.bgu.spl.mics.application.passiveObjects.Ewoks;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	private List<Queue<Event>> microservice;

	private static MessageBusImpl messageBusImpl;



	private MessageBusImpl()
	{
		microservice=new LinkedList<Queue<Event>>() ;
	}

	public synchronized MessageBusImpl getEwoks(int count){
		if(messageBusImpl==null){
			messageBusImpl=new MessageBusImpl();
		}
		return messageBusImpl;
	}



	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		
    }

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		
	}

	
	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		
        return null;
	}

	@Override
	public void register(MicroService m) {
		
	}

	@Override
	public void unregister(MicroService m) {
		
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		
		return null;
	}
}
