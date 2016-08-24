package com.Alex.crawler;
import static com.Alex.crawler.MyValues.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;



import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class SpiderLegImplement extends SpiderLeg{
	/**
	 * use a fake USER_AGENT so the web server thinks the robot is a normal web browser.
	 */
	
	private List<String> links = new LinkedList<>();
	private Document htmlDocument = null;

	@Override
	public boolean crawl(String url) {
		// TODO Auto-generated method stub
		if(url==null || url=="")
			return false;
		try{
			Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
			htmlDocument = connection.get();
			if(connection.response().statusCode() == 200){
				System.out.println("Received web page at " + url);
			}
			if(!connection.response().contentType().contains("text/html")){
				System.out.println("**Failure** Retrieved something other than HTML");
				return false;
			}
			
			Elements linksOnPage = htmlDocument.select("a[href]");
			System.out.println("Found (" + linksOnPage.size() + ") links");
			for(Element link: linksOnPage){
				links.add(link.absUrl("href"));
			}
			return true;
		}catch(IOException ioe){
			System.out.println("Error in out HTTP request " + ioe);
			return false;
		}
	}

	@Override
	public boolean searchForWord(String searchWord) {
		// TODO Auto-generated method stub
		if(htmlDocument == null){
			 System.out.println(Thread.currentThread().getName()+ " ERROR! Call crawl() before performing analysis on the document");
	         return false;
		}
		System.out.println("Search for the word " + searchWord + "...");
		String bodyText = htmlDocument.body().text();
		return bodyText.toLowerCase().contains(searchWord.toLowerCase());
	}

	@Override
	public List<String> getLinks() {
		// TODO Auto-generated method stub
		return links;
	}
	
}
