package com.hackmiester.bathsalts.service;

import java.util.HashMap;

import com.hackmiester.bathsalts.Service;
import com.skype.Chat;
import com.skype.SkypeException;
import com.skype.User;
import com.skype.ChatMessage;

public class RedactService extends Service {

	private HashMap< Chat, HashMap<User,ChatMessage> > m;
	
	//TODO: this service is a total piece of shit. can't it just note every message the bot ever sends and let anyone redact it? do we really have to keep track? ugh ugh ugh why did I ever write this
	
	public RedactService() {
		super(new String[] {"redact","remove"});
		disableHelp = false;
		help = "Removes the bot's last response";
		
		m = new HashMap< Chat, HashMap<User,ChatMessage> >();
	}
	
	public void addMessage(User sender, ChatMessage msg) throws SkypeException {
		Chat chat = msg.getChat();
		HashMap<User,ChatMessage> chm = m.get(chat);
		if ( chm == null ) {
			chm = new HashMap<User,ChatMessage>();
			m.put(chat, chm);
		}
		chm.put(sender, msg);
	}
	
	public boolean redact(User user, Chat chat) throws SkypeException {
		HashMap<User,ChatMessage> chm = m.get(chat);
		if ( chm == null ) {
			return false;
		}
		chm.get(user).setContent("<Redacted by " + user.getFullName() + ">");
		return true;
	}
	protected ChatMessage doCommand(ChatMessage msg) {
		try {
			if (!( this.redact(msg.getSender(),msg.getChat()) )) {
				msg.getChat().send("Couldn't redact. Not sure what's going on tbh.");
			}
		} catch (SkypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void doService(ChatMessage msg) {
	}
}
