package com.hackmiester.bathsalts.link;

import java.net.URL;

import com.hackmiester.bathsalts.LinkListener;
import com.skype.ChatMessage;

public class LoggerLinkListener extends LinkListener {

	public LoggerLinkListener() {
		super(-100); //weight -100 (do first)
	}
	
	protected ChatMessage doLink(ChatMessage msg, URL url) {
		System.out.println("Found URL: " + url);
		return null; //TODO: is there a better architecture here?
	}

}
