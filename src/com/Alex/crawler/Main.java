package com.Alex.crawler;

public class Main {
	public static void main(String[] args){
		Spider spider = new Spider();
		String url = "http://arstechnica.com/";
//		String url = "";
		String searchWord = "laowangxuexi";
		spider.search(url, searchWord);
		
		
		System.out.println("++++++++++++++++++++++++++++++++++++");
		
		SuperSpider spider2 = new SuperSpider();
		spider2.search(url, searchWord);
	}
}
