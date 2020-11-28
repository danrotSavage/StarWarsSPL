package main.java.bgu.spl.mics;

import main.java.bgu.spl.mics.application.messages.AttackEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	private List<Queue<Message>> microservice;

	private List<MicroService> AttackEvent;
	private List<MicroService> DeactivationEvent;
	private List<MicroService> BombEvent;


	private List<MicroService> Broadcast;


	private static MessageBusImpl messageBusImpl;

    public static Object lock=new Object();

	public MessageBusImpl()
	{

		microservice=new ArrayList<Queue<Message>>() ;
	}

	public static MessageBusImpl getMessageBusImpl(){
		if(messageBusImpl==null){
			synchronized (lock) {
				if(messageBusImpl==null)
				   messageBusImpl = new MessageBusImpl();
			}
		}
		return messageBusImpl;
	}


    private void addEvent(MicroService m, AttackEvent e){
		AttackEvent.add(m);
    }
	private void addEvent(MicroService m, main.java.bgu.spl.mics.application.messages.DeactivationEvent e){
		DeactivationEvent.add(m);
	}
	private void addEvent(MicroService m, main.java.bgu.spl.mics.application.messages.BombEvent e){
		BombEvent.add(m);
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		//addEvent(m,type);
		//add the relevent event to the relevent queue
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
