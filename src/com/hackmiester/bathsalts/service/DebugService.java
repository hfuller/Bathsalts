package com.hackmiester.bathsalts.service;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;

import com.hackmiester.bathsalts.Service;
import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.SkypeException;

public class DebugService extends Service implements UncaughtExceptionHandler {

	public DebugService() {
		super(new String[] {"debug","exception"});
		disableHelp = false;
		help = "Turn on debug logging in this chat";
		
	}

	@Override
	protected void doService(ChatMessage msg) {}
	
	protected ChatMessage doCommand(ChatMessage msg) {
		ChatMessage m = null;
		//throw an exception!
		if ( getCmd(msg).equals("exception") ) {
			try {
				m = msg.getChat().send("Throwing exception now.");
			} catch (SkypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ArrayList<String> al = new ArrayList<String>(); //load six bullets
			al.get(1); //play russian roulette
		}
		
		try {
			if ( msg.getSenderId().equals("live:hfuller") ) { //TODO: make the owner(s) of the bot configurable
				return super.doCommand(msg);
			} else {
				m = msg.getChat().send("nah man");
				return m;
			}
		} catch (SkypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m;
	}


	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
		//TODO: i'm pretty sure this sucks (adn maybe it's duplication of code?)
		StringBuilder sb = new StringBuilder();
		sb.append("Uncaught exception in " + arg0.toString() + "\n");
		sb.append(arg1.toString() + "\n");
		for ( StackTraceElement x : arg1.getStackTrace() ) {
			sb.append(x + "\n");
		}
		for ( Chat c : subscribedChats ) {
			try {
				c.send(sb.toString());
			} catch (SkypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void debug(String string) {
		for ( Chat c : subscribedChats ) {
			try {
				c.send("DEBUG: " + string);
			} catch (SkypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
