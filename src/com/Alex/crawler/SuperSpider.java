package com.Alex.crawler;
import static com.Alex.crawler.MyValues.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SuperSpider {
	
	private Set<String> pagesVisited = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
//	private Set<String> pagesVisited = new HashSet<>(); 
	//private List<String> pagesToVisit = new LinkedList(); 
	private ConcurrentLinkedQueue<String> pagesToVisit = new ConcurrentLinkedQueue<>();
	private static Object lock = new Object();
	
	
	/**
	 * Our main launching point for the Spider's functionality. Internally it creates spider legs
	 * that make an HTTP request and parse the response (the web page).
	 * @param url
	 * 		- The starting point of the spider
	 * @param searchWord
	 * 		- The word that you are searching for
	 */
	public void search(String url, String searchWord){
		long c1 = System.currentTimeMillis();
		int workerNum = 4;

		Thread[] threads = new Thread[workerNum];
		pagesToVisit.add(url);
		
		for(int i=0;i<workerNum;++i){
			threads[i] = new Thread(new Worker(workerNum, lock, pagesToVisit, pagesVisited, searchWord));
		}
		
		Thread monitor = new Thread(new Monitor(threads, lock));
		monitor.start();
		
		for(int i=0;i<workerNum;++i){
			threads[i].start();
		}
		
		try {
			monitor.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		long c2 = System.currentTimeMillis();
		System.out.println("visited pages Num: " + pagesVisited.size());
		System.out.println("Suprer spider running time: " + (c2-c1));
	}
}
