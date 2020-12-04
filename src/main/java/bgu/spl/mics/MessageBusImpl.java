package main.java.bgu.spl.mics;

import jdk.jshell.spi.ExecutionControl;
import main.java.bgu.spl.mics.application.messages.AttackEvent;

import java.util.*;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {


	private HashMap< MicroService, Queue<Message> > IndividualQueue;
	private HashMap< Class<? extends Message> , Queue<MicroService> > roundRobinQueues;
	private HashMap< MicroService , LinkedList<Queue<MicroService>> > unsubscribingQueue;
    private HashMap<Event,Future> theTruth;

	private static MessageBusImpl messageBusImpl;

	public static Object lock = new Object();


	private MessageBusImpl() {
		IndividualQueue=new HashMap<>();
		roundRobinQueues=new HashMap<>();
		theTruth =new HashMap<>();
	}

	public static MessageBusImpl getMessageBusImpl() {
		if (messageBusImpl == null) {
			synchronized (lock) {
				if (messageBusImpl == null)
					messageBusImpl = new MessageBusImpl();
			}
		}
		return messageBusImpl;
	}


	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		//take the relevant round robin queue for the specific event
		synchronized (this) {
			if (!roundRobinQueues.containsKey(type)) {
				Queue<MicroService> b = new LinkedList<>();
				roundRobinQueues.put(type, b);

			}

		}
		roundRobinQueues.get(type).add(m);

		synchronized (this) {
			if (!unsubscribingQueue.containsKey(m)) {
				LinkedList<Queue<MicroService>> lq = new LinkedList<>();
				unsubscribingQueue.put(m,lq);

			}

		}
        unsubscribingQueue.get(m).add(roundRobinQueues.get(type));
	}


	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		//take the relevant round robin queue for the specific event

		synchronized (this) {
			if (!roundRobinQueues.containsKey(type)) {
				Queue<MicroService> b = new LinkedList<>();
				roundRobinQueues.put(type, b);
			}
		}
		roundRobinQueues.get(type).add(m);

		synchronized (this) {
			if (!unsubscribingQueue.containsKey(m)) {
				LinkedList<Queue<MicroService>> lq = new LinkedList<>();
				unsubscribingQueue.put(m,lq);

			}

		}
		unsubscribingQueue.get(m).add(roundRobinQueues.get(type));

	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
         theTruth.get(e).resolve(result);
	}

	@Override
	public synchronized void sendBroadcast(Broadcast b) {

		int size = roundRobinQueues.get(b).size();
		for (int i = 0; i < size; i++) {
			MicroService temp = (roundRobinQueues.get(b)).remove();
			roundRobinQueues.get(b).add(temp);
			//take the microservice and add to its queue the event
			if (IndividualQueue.containsKey(temp)) {


				IndividualQueue.get(temp).add(b);
				notifyAll();
			}
		}
	}



	
	@Override
	public <T> Future<T> sendEvent(Event<T> e)throws Exception
	{
		boolean again=true;
		while (again) {
			MicroService temp = (roundRobinQueues.get(e)).remove();
			roundRobinQueues.get(e).add(temp);

			//take the microservice and add to its queue the event
			if (IndividualQueue.containsKey(temp)) {
				IndividualQueue.get(temp).add(e);
				notifyAll();
				again=false;
			}
		}
		throw new Exception("no");
	}

	@Override
	public void register(MicroService m) {
		if(!IndividualQueue.containsKey(m))
		{
			Queue< Message> que = new LinkedList<>();
			IndividualQueue.put(m,que);
		}

	}

	@Override
	public synchronized void unregister(MicroService m) {
		if(IndividualQueue.containsKey(m)) {
			IndividualQueue.remove(m);
		}
		Iterator <Queue<MicroService>> itr = unsubscribingQueue.get(m).iterator();
		while (itr.hasNext()) {
			int size = itr.next().size();
			for (int i = 0; i < size; i++) {
				MicroService temp = (roundRobinQueues.get(m)).remove();
				if (temp != m) {
					roundRobinQueues.get(m).add(temp);
				}
			}
		}

	}

	@Override
	public  Message awaitMessage(MicroService m) throws InterruptedException {
		while (IndividualQueue.get(m).isEmpty()) {
			try {
				this.wait();
			} catch (InterruptedException ignored){}
		}
		Message mess  = IndividualQueue.get(m).remove();
		return mess;
	}
}
