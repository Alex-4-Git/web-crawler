package com.Alex.crawler;
import static com.Alex.crawler.MyValues.*;

import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Worker implements Runnable{
	private ConcurrentLinkedQueue<String> pagesToVisit;
	private Set<String> pagesVisited;
	private String searchWord;
	private int workerNum;

	private static volatile int waitingWorker = 0;
	private Object lock;
	private static final Object setLock = new Object();
	
	
	public Worker(int workerNum, Object lock, ConcurrentLinkedQueue<String> pagesToVisit, Set<String> pagesVisited, String searchWord){
		this.pagesToVisit = pagesToVisit;
		this.pagesVisited = pagesVisited;
		this.searchWord = searchWord;
		this.workerNum = workerNum;
		this.lock = lock;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		search(searchWord);
	}
	
	private void search(String searchWord){
		while(!Thread.interrupted()){
			String currentUrl;
			SpiderLeg leg = new SpiderLegImplement();

			currentUrl = nextUrl();
			
			if(currentUrl==null || currentUrl=="")
				continue;
			
			leg.crawl(currentUrl);

			pagesVisited.add(currentUrl);
			boolean success = leg.searchForWord(searchWord);
			
			if(success){
				System.out.println(String.format("Successfully find word '%s' at '%s'", 
						searchWord, currentUrl));
				synchronized(lock){
					lock.notifyAll();
				}
				return;
			}
			
			if(pagesVisited.size()>= MAX_PAGES_TO_SEARCH){
				synchronized(lock){
					lock.notifyAll();
				}
				System.out.println("kill Message: "+"Visted pages "+pagesVisited.size());
				return;
			}
				
			this.pagesToVisit.addAll(leg.getLinks());
			if(pagesToVisit.size()>0){
				synchronized(pagesToVisit){
					pagesToVisit.notifyAll();
				}
			}
		}
		
	}
	


	private String nextUrl(){
		String nextUrl = null;
		try {
			synchronized (pagesToVisit) {
				waitingWorker++;
				stopIfAllWorkerIsWaiting();
				while(nextUrl==null){
					if(pagesToVisit.size()==0){
						pagesToVisit.wait();
					}
					nextUrl = pagesToVisit.poll();
					if(pagesVisited.contains(nextUrl))
						nextUrl = null;
				}
				waitingWorker--;
				System.out.println(Thread.currentThread().getName()+" current url : "+nextUrl);
			}

			return nextUrl;
		} catch (Exception e) {
			return "";
		}
	}
	
	private void stopIfAllWorkerIsWaiting(){
		if(allWorkerIsWaiting()){
			synchronized(lock){
				lock.notifyAll();
			}
			System.out.println("kill Message: "+"WaitingWorker Num: "+waitingWorker);
		}
	}
	
	private boolean allWorkerIsWaiting(){
		return pagesToVisit.size()==0 && waitingWorker == workerNum;
	}
}
