package main.java.bgu.spl.mics;

import main.java.bgu.spl.mics.Messages.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {


	private ConcurrentHashMap < MicroService, Queue<Message> > IndividualQueue;
	private ConcurrentHashMap< Class<? extends Message> , Queue<MicroService> > roundRobinQueues;
	private ConcurrentHashMap< MicroService , LinkedList<Queue<MicroService>> > unsubscribingQueue;
    private ConcurrentHashMap<Event, Future> theTruth;

	private static MessageBusImpl messageBusImpl=new MessageBusImpl();

	private static Object roundRobinLock = new Object();

    private AtomicInteger subscribed;//count how many microservices subscribed(lea need to wait for everyone to subscribe before she can start send messages)
    private void ZeroSubscribed(){
		int val;
		do {
			val = subscribed.get();
		}
		while (!subscribed.compareAndSet(val,0));
	}
    private void addSubscribed () {
		int val;
		do {
			val = subscribed.get();
		}
		while (!subscribed.compareAndSet(val,val+1));
}
	private void subtractSubscribed () {
		int val;
		do {
			val = subscribed.get();
		}
		while (!subscribed.compareAndSet(val,val-1));
	}

	private MessageBusImpl() {
		IndividualQueue=new ConcurrentHashMap<>();
		roundRobinQueues=new ConcurrentHashMap<>();
		theTruth =new ConcurrentHashMap<>();
		unsubscribingQueue=new ConcurrentHashMap<>();
		roundRobinLock=new Object();
		subscribed = new AtomicInteger(0);
	}

	public static MessageBusImpl getMessageBusImpl() {return messageBusImpl;}
	public static MessageBusImpl getInstance() {return messageBusImpl;}


	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		//System.out.println(Thread.currentThread().getId() + " has subscribed to event "+type);
		//take the relevant round robin queue for the specific event
		synchronized (this) {
			if (!roundRobinQueues.containsKey(type)) {
				Queue<MicroService> b = new LinkedList<>();
				roundRobinQueues.put(type, b);
			}

			addSubscribed();
			System.out.println(Thread.currentThread().getId() + " has added to subscribed " + subscribed.get());
			if (subscribed.get() == 4) {
				notifyAll();
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

		synchronized (roundRobinLock) {
			if (!roundRobinQueues.containsKey(type)) {
				Queue<MicroService> b = new LinkedList<>();
				roundRobinQueues.put(type, b);
			}
		}
		roundRobinQueues.get(type).add(m);

		synchronized (roundRobinLock) {
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
         theTruth.remove(e);
		System.out.println(Thread.currentThread().getId() + " has completed an attack");
	}

	@Override
	public synchronized void sendBroadcast(Broadcast b) {

		int size = roundRobinQueues.get(b.getClass()).size();
		for (int i = 0; i < size; i++) {
			MicroService temp = (roundRobinQueues.get(b.getClass())).remove();
			roundRobinQueues.get(b.getClass()).add(temp);
			//take the microservice and add to its queue the event
			synchronized (IndividualQueue.get(temp)) {
				if (IndividualQueue.containsKey(temp)) {
					IndividualQueue.get(temp).add(b);
					IndividualQueue.get(temp).notifyAll();
				}
			}
		}
		ZeroSubscribed();//only brodcast sent is terminate so no one is Subscribed

	}



	
	@Override
	public synchronized  <T> Future<T> sendEvent(Event<T> e)
	{
		Future f=new Future();
		theTruth.put(e,f);
		boolean again=true;
		int i=0;
		while ( subscribed.get() < 4) {
			try {
				System.out.println("lea went to sleep");
				this.wait();
			}
			catch (Exception ex){}
			if(subscribed.get() == 4){
				System.out.println("lea awoke");
			}
		}

	while (roundRobinQueues.containsKey(e.getClass()) && !roundRobinQueues.get(e.getClass()).isEmpty() &&  i < roundRobinQueues.size() & again) {

		MicroService temp = (roundRobinQueues.get(e.getClass())).remove();
		roundRobinQueues.get(e.getClass()).add(temp);

		//take the microservice and add to its queue the event
		synchronized (IndividualQueue.get(temp)) {
			if (IndividualQueue.containsKey(temp)) {
				IndividualQueue.get(temp).add(e);
				IndividualQueue.get(temp).notifyAll();
				System.out.println(Thread.currentThread().getId() + " has awaken");
				again = false;
			}
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
	public  Message awaitMessage(MicroService m)  {

		synchronized (IndividualQueue.get(m)) {
			while (IndividualQueue.get(m).isEmpty()) {
				System.out.println(Thread.currentThread().getId() + " is waiting for an attack");
				try {
					IndividualQueue.get(m).wait();
				} catch (InterruptedException ignored) {
				}
			}
			System.out.println(Thread.currentThread().getId() + " has taken an attack");
			Message mess = IndividualQueue.get(m).remove();
			return mess;
		}
	}
}
