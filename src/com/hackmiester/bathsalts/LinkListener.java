package com.hackmiester.bathsalts;

import java.net.URL;
import com.skype.ChatMessage;

public abstract class LinkListener extends Listener {
	
	public LinkListener(int w){ super(w); }
	public LinkListener(){ super(0); }
	
	@Deprecated //use doLink instead, bust out the links yourself
	public ChatMessage processMessage(ChatMessage msg) {
		System.out.println("This method is obsolete.");
		return null;
	}
	
	protected abstract ChatMessage doLink(ChatMessage msg, URL url);
	
}
