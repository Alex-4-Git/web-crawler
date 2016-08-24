package com.Alex.crawler;

import java.util.List;

public abstract class SpiderLeg {
	abstract public boolean crawl(String url);
	abstract public boolean searchForWord(String word);
	abstract public List<String> getLinks();
}
