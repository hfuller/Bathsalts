package com.hackmiester.bathsalts.service;

import com.hackmiester.bathsalts.Service;
import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.SkypeException;

public class ShutdownService extends Service {

	public ShutdownService() {
		super(new String[] {"restart","shutdown","die","exit"});
		disableHelp = true;
		
		for ( Chat c : subscribedChats ) {
			try {
				c.send("back");
			} catch (SkypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		subscribedChats.clear();
		System.err.println("Saving subscriptions");
		this.saveSubscriptions();
	}

	@Override
	protected void doService(ChatMessage msg) {}
	
	protected ChatMessage doCommand(ChatMessage msg) {
		ChatMessage m = null;
		try {
			if ( msg.getSenderId().equals("live:hfuller") ) { //TODO
				Chat c = msg.getChat();
				subscribedChats.remove(c);
				subscribedChats.add(c);
				c.send("k brb");
				
				System.err.println("Saving subscriptions");
				this.saveSubscriptions();
				
				System.exit(0);
			} else {
				m = msg.getChat().send("wtf");
			}
			
		} catch (SkypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m;
	}

}
