package com.Alex.crawler;
import static com.Alex.crawler.MyValues.*;


import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;




public class Spider {
	
	private Set<String> pagesVisited = new HashSet<>();
	//private List<String> pagesToVisit = new LinkedList(); 
	private Queue<String> pagesToVisit = new LinkedList<>();
	
	
	
	/**returns the next URL to visit. We also do a check to make sure this method doesn't return
	 * a URL that has already been visited
	 * @return
	 */
	private String nextUrl(){
		String nextUrl;
		try {
			do{
				nextUrl = pagesToVisit.poll();
			}while(pagesVisited.contains(nextUrl));
			return nextUrl;
		} catch (Exception e) {
			return null;
		}
	}
	
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
		pagesToVisit.add(url);
		while(!pagesToVisit.isEmpty() && pagesVisited.size()< MAX_PAGES_TO_SEARCH){
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
				break;
			}
			this.pagesToVisit.addAll(leg.getLinks());
		}
		System.out.println(String.format("Visited %s web pages", pagesVisited.size()));
		long c2 = System.currentTimeMillis();
		System.out.println(c2-c1);
	}
	
}
