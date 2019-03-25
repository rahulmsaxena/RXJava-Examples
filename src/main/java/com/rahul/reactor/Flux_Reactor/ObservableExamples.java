package com.rahul.reactor.Flux_Reactor;

import java.util.logging.Level;
import java.util.logging.Logger;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class ObservableExamples {
	
	private static Logger logger = Logger.getLogger("LogMessages");
	private String [] strArray = {"The","quick","brown","fox","jumped"};

	
	public static void printThread()
	{
		logger.setLevel(Level.INFO);
		logger.info(Thread.currentThread().getName());
	}
	
	//====== SIMPLE EXAMPLE #1 ============
	public void simpleObserverableWithNoImplementation()
	{

		Observable.from("Hello World")
		.subscribe(t->{
			printThread();
			logger.info(t);
		});
	}

	//========== SIMPLE EXAMPLE #2 (EQ TO #1)================
	private void simpleObservableExample()
	{
		Observable<String> observable = 
				Observable.create(new Observable.OnSubscribe<String>() {
					@Override
					public void call(Subscriber<? super String> t1) {
						// TODO Auto-generated method stub
						logger.info("Inside Publisher");	
						t1.onNext("Hello World");
						t1.onCompleted();
					}
				});
		
		observable.subscribe(new Subscriber<String>() {

			@Override
			public void onCompleted() {
				// TODO Auto-generated method stub
				logger.info("Inside On Completed");
			}

			@Override
			public void onError(Throwable e) {
				// TODO Auto-generated method stub
				logger.severe("Exception encountered");
			}

			@Override
			public void onNext(String t) {
				// TODO Auto-generated method stub
				logger.info("Inside On Next " +t);
			}
		});
	}
	
	
	//==== EXAMPLE USING ARRAY ===========
	
	public void printfromArray()
	{
				Observable.from(strArray)
		.subscribe(element->{
			printThread();
			logger.info(element);
		});
	}
	
	//EXAMPLE USING ARRAY WITH SCHEDULER ==============
	public void printfromArrayUsingScheduler() throws InterruptedException
	{
		Observable.from(strArray)
			.subscribeOn(Schedulers.newThread())
			.subscribe(element->{
				printThread();
				logger.info(element);
			});
		
		Thread.sleep(3000);
	}
	
	//EXAMPLE USING Operators
	
	public void printUsingOperators() throws Exception
	{
		Observable.from(strArray)
		.subscribeOn(Schedulers.newThread())
		.map(element->element.toUpperCase())
		.filter(element->(!element.equalsIgnoreCase("fox")))
		.subscribeOn(Schedulers.newThread())
		.subscribe(element->{
			printThread();
			logger.info(element);
		});
		
		Thread.sleep(3000);
	}
	
	// EXAMPLE USING PARALLEL MECHANISM
	public void printUsingOperatorAndParallelThreads() throws Exception
	{
		Observable<String> observable = 
		Observable.from(strArray)
		.subscribeOn(Schedulers.newThread())
		.parallel((element)->{
			return (
				element.map(elem->elem.toUpperCase())
				.filter(ele->! ele.equalsIgnoreCase("fox"))
				)
				.doOnNext(x->{
					System.out.println("Entering"+"\t\t\t"+Thread.currentThread().getName());
				});
		});
		
		observable.subscribe(new Subscriber(){

			@Override
			public void onCompleted() {
				// TODO Auto-generated method stub
				System.out.println("completed");
			}
			@Override
			public void onError(Throwable e) {
				// TODO Auto-generated method stub
				System.out.println("onError"+ Thread.currentThread().getName());
			}
			@Override
			public void onNext(Object t) {
				String element = (String) t;
				System.out.println("\t\t"+element);
				System.out.println("Leaving "+"\t\t\t"+Thread.currentThread().getName());
			}
		});
		
		Thread.sleep(2000);
	}
	
	
	public static void main(String args[]) throws Exception
	{
		ObservableExamples obs = new ObservableExamples();
		//obs.simpleObserverableWithNoImplementation();
		//obs.simpleObservableExample();
		//obs.printfromArray();
		//obs.printfromArrayUsingScheduler();
		//obs.printUsingOperators();
		obs.printUsingOperatorAndParallelThreads();
	}
	
}
