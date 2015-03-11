package com.hackmiester.bathsalts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.Skype;
import com.skype.SkypeException;

public abstract class Service extends CommandListener {
	
	protected ArrayList<Chat> subscribedChats;
	
	public Service(String[] cl) {
		super(cl);
		subscribedChats = new ArrayList<Chat>();
		this.loadSubscriptions();
	}
	
	@Override
	protected ChatMessage doCommand(ChatMessage msg) {
		ChatMessage m = null;
		try {
			Chat c = msg.getChat();
			if ( subscribedChats.remove(c) ) {
				m = c.send("Unsubscribed from " + this.getClass().getSimpleName());
			} else {
				subscribedChats.add(c);
				m = c.send("Subscribed to " + this.getClass().getSimpleName());
			}
			System.err.println("Saving subscriptions");
			this.saveSubscriptions();
		} catch (SkypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m;
	}
	
	private void loadSubscriptions() {
		System.err.println("Loading service subscriptions");
		Preferences prefs = Preferences.userNodeForPackage(this.getClass());
		String str = prefs.get(this.getClass().getName() + "/subscriptions","");
		System.err.println(this.getClass().getName()+ "/subscriptions");
        System.err.println(str);
        
		List<String> items = Arrays.asList(str.split("\\s*,\\s*"));
		System.err.println(items);
		Chat[] chats;
		try {
			chats = Skype.getAllChats();
			for ( String s : items ) {
				System.err.println(s);
				for ( Chat c : chats ) {
					if ( c.getId().equals(s)) {
						subscribedChats.add(c);
					}
				}
			}
		} catch (SkypeException e1) {
			e1.printStackTrace();
		} finally {
			System.err.println(subscribedChats);
		}
		
		//try {Thread.sleep(3000);} catch (InterruptedException e) {}
	}

	protected void saveSubscriptions() {
		System.err.println("Saving service subscriptions");
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        prefs.put(this.getClass().getName() + "/subscriptions",this.getSubscriptions());
        System.err.println(this.getClass().getName()+ "/subscriptions");
        System.err.println(this.getSubscriptions());
	}

	private String getSubscriptions() {
		String s = "";
		for ( Chat c : subscribedChats ) {
			s = s + c.getId() + ",";
		}
		if ( s.length()>0 ) s=s.substring(0,s.length()-1);
		return s;
	}

	public ChatMessage processMessage(ChatMessage msg) {
		
		try {
			if ( subscribedChats.contains(msg.getChat()) ) {
				this.doService(msg);
			}
		} catch (SkypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return super.processMessage(msg);
	}

	protected abstract void doService(ChatMessage msg);

}
