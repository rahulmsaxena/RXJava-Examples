package com.rahul.reactor.Flux_Reactor;



import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.reactivestreams.Subscriber;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.functions.Consumer;
import io.reactivex.subscribers.TestSubscriber;
import reactor.core.scheduler.Scheduler;
import rx.Observable.*;
import rx.subscriptions.*;
import rx.Observer;
import io.reactivex.subscribers.DefaultSubscriber;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import io.reactivex.subjects.*;
//import rx.subjects.PublishSubject;
import io.reactivex.subjects.PublishSubject;

public class SimpleObserverClass {
	
	public static void main(String args[]) throws Exception
	{
//				Observable.from("Hello world")
//		.subscribe(element->
//			System.out.println(Thread.currentThread().getName()+"\t"+element));
		
		SimpleObserverClass soc = new SimpleObserverClass();
//		soc.observable_With_ImplementedObserver_Using_Scheduler();
//		soc.testWithBackPressure();
//		soc.thenAllValuesAreBufferedAndReceived();
//		soc.hotPublish();
		
		//create subscriber-1
		Consumer<String> sub1 = soc.createSubscriber("subscriber1");

		//create subscriber-2
		Consumer<String> sub2 = soc.createSubscriber("subscriber2");

		String [] strArray = {"The","quick","brown","fox","jumped","over"};
		//create Observable
		Observable<String> observable = Observable.create((subscriber)->{
			
			for (String string : strArray) {
				subscriber.onNext(string);
			}
			subscriber.onComplete();
		});

		//async subject
//		soc.asyncSubjectExample(observable, sub1, sub2);
		soc.createBehaviorOrPublishSubject(BehaviorSubject.create());
//		System.out.println("************ Publish Subject ***************");
//		soc.createBehaviorOrPublishSubject(PublishSubject.create());
		System.out.println("************ Replay Subject ***************");		
		soc.createBehaviorOrPublishSubject(ReplaySubject.create());
		
	}

	public void asyncSubjectExample(Observable<String> observable,Consumer sub1,Consumer sub2)
	{
		AsyncSubject<String> asyncSubject = AsyncSubject.create();
		//adding async subject as publisher
		
		observable.subscribe(asyncSubject);
		asyncSubject.subscribe(sub1);
		asyncSubject.subscribe(sub2);

	}
	
	public void createBehaviorOrPublishSubject(Subject behaviorSub) throws Exception
	{
		//behavorial subject
		Observable<Long> observable1 = Observable.interval(1,TimeUnit.MILLISECONDS);
		observable1.subscribe(behaviorSub);
		
		Consumer<Long> longConsumer1 = createLongConsumer("consumer1");
		Consumer<Long> longConsumer2 = createLongConsumer("consumer2");
		System.out.println("Sleeping");
		Thread.sleep(1000);
		behaviorSub.subscribe(longConsumer1);
		behaviorSub.subscribe(longConsumer2);
		System.out.println("Waking up");
	}
	

	
	//	public void observable_With_ImplementedObserver_Using_Scheduler()
//	{
//		String [] StringArray = {"The","quick","brown","fox","jumped"};
//		rx.Observable.create(new rx.Observable.OnSubscribe<String>() {
//			@Override
//			public void call(Subscriber<? super String> t1) {
//				for (String str : StringArray) {
//					System.out.println("Inside Call "+Thread.currentThread().getName());
//					t1.onNext(str);
//				}
//				t1.onCompleted();
//			}})
//		.filter(elem-> ! (elem.equalsIgnoreCase("fox")))
//		.map(element->element.toUpperCase())
//		.subscribeOn(Schedulers.newThread())
//		.subscribe(
//		new Observer<Object>() {
//			@Override
//			public void onCompleted() {
//				System.out.println("Completed");
//			}
//			@Override
//			public void onError(Throwable e) {
//				System.err.println("Error");
//			}
//			@Override
//			public void onNext(Object t) {
//				String s = (String) t;
//				System.out.println("OnNext: "+s+"\t"+Thread.currentThread().getName());
//			}});
//	}
//	

	public void testWithBackPressure()
	{
		FlowableOnSubscribe<Integer> flowableOnSubscribe
		 = flowable -> 
		
		{
			while(true) {
				System.out.println(Thread.currentThread().getName());
				flowable.onNext(1);
			}
		};
		 Flowable<Integer> intFlowable = Flowable.create(flowableOnSubscribe,
				 BackpressureStrategy.BUFFER);
		 
		 intFlowable.subscribeOn(io.reactivex.schedulers.Schedulers.newThread())
		 .subscribe(elem->System.out.println(Thread.currentThread().getName()+"\t"+"The element is "+elem));
		 
		 try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static class Computation {
		public static void compute(Integer v) {
	        try {
	            System.out.println("compute integer v: " + v);
	            Thread.sleep(1000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	public void hotPublish()
	{
//		TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
//		PublishSubject<Integer> source = PublishSubject.<Integer>create();
//		 
//		source.observeOn(Schedulers.computation())
//		  .subscribe(Computation::compute, Throwable::printStackTrace);
//		 
//		IntStream.range(1, 1_000_000).forEach(source::onNext);
//		
//		try {
//			Thread.sleep(3000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}
	
	
//	public void thenAllValuesAreBufferedAndReceived() {
//	    List<Integer> testList = IntStream.range(0, 100000)
//	      .boxed()
//	      .collect(Collectors.toList());
//	  
//	    io.reactivex.Observable<Integer> observable = Observable.fromIterable(testList);
//	    TestSubscriber<Integer> testSubscriber = (TestSubscriber<Integer>) observable
//	      .toFlowable(BackpressureStrategy.DROP)
//	      .observeOn(io.reactivex.schedulers.Schedulers.computation())
//	      .subscribe();
//	      //.observeOn(io.reactivex.schedulers.Schedulers.computation())
//	 
//	    testSubscriber.awaitTerminalEvent();
//	 
//	    List<Integer> receivedInts = testSubscriber.getEvents()
//	      .get(0)
//	      .stream()
//	      .mapToInt(object -> (int) object)
//	      .boxed()
//	      .collect(Collectors.toList());
//	    
//	    System.out.println("Published list size: "+testList.size()
//	    +" actual events size: "+receivedInts.size());
//	    //assertEquals(testList, receivedInts);
//	}
	
	public Consumer createSubscriber(String subscriberName)
	{
		Consumer<String> subscriber = (t)-> {
			String str = (String) t;
			System.out.println("Inside "+subscriberName+" consumer "+str);
	};
		return subscriber;
	}
	
	public Consumer createLongConsumer(String subscriberName)
	{
		Consumer<Long> subscriber = (t)-> {
				Long lng = (Long) t;
				System.out.println("Inside "+ subscriberName +" consumer "+lng);
		};
		return subscriber;
	}
}
