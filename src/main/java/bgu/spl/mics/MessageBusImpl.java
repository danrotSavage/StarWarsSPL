package main.java.bgu.spl.mics;

import main.java.bgu.spl.mics.Messages.*;

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
    private HashMap<Event, Future> theTruth;

	private static MessageBusImpl messageBusImpl;

	public static Object lock = new Object();


	private MessageBusImpl() {
		IndividualQueue=new HashMap<>();
		roundRobinQueues=new HashMap<>();
		theTruth =new HashMap<>();
		unsubscribingQueue=new HashMap<>();
		lock=new Object();
	}

	public synchronized static MessageBusImpl getMessageBusImpl() {
		if (messageBusImpl == null) {
			messageBusImpl = new MessageBusImpl();
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
		System.out.println(Thread.currentThread().getId() + " has completed an attack");
	}

	@Override
	public synchronized void sendBroadcast(Broadcast b) {

		int size = roundRobinQueues.get(b.getClass()).size();
		for (int i = 0; i < size; i++) {
			MicroService temp = (roundRobinQueues.get(b.getClass())).remove();
			roundRobinQueues.get(b.getClass()).add(temp);
			//take the microservice and add to its queue the event
			if (IndividualQueue.containsKey(temp)) {


				IndividualQueue.get(temp).add(b);
				notifyAll();
			}
		}
	}



	
	@Override
	public synchronized  <T> Future<T> sendEvent(Event<T> e)throws Exception
	{
		Future f=new Future();
		theTruth.put(e,f);
		boolean again=true;
		int i=0;
		while (again&&i<roundRobinQueues.size()) {
			MicroService temp = (roundRobinQueues.get(e.getClass())).remove();
			roundRobinQueues.get(e.getClass()).add(temp);

			//take the microservice and add to its queue the event
			if (IndividualQueue.containsKey(temp)) {
				IndividualQueue.get(temp).add(e);
				notifyAll();
				again=false;
			}
			i++;
		}
		return theTruth.get(e);
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
			Queue<MicroService> queueMicro = itr.next();
			for (int i = 0; i < queueMicro.size(); i++) {

				MicroService temp = queueMicro.poll();
				if (temp != m) {
					queueMicro.add(temp);
				}
			}
		}

	}

	@Override
	public synchronized Message awaitMessage(MicroService m) throws InterruptedException {

		while (IndividualQueue.get(m).isEmpty()) {
			try {
				this.wait();
			} catch (InterruptedException ignored){}
		}
		System.out.println(Thread.currentThread().getId() + " has taken an attack");
		Message mess  = IndividualQueue.get(m).remove();
		return mess;
	}
}
