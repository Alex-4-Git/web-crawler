package com.Alex.crawler;

public class Monitor implements Runnable{
	private Thread[] threads;
	private Object lock;
	
	public Monitor(Thread[] threads, Object lock){
		this.threads = threads;
		this.lock = lock;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			synchronized(lock){
				lock.wait();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			return;
		}
		for(int i=0; i<threads.length; ++i){
			threads[i].interrupt();
			System.out.println("Kill "+threads[i].getName());
		}
		
	}

}
