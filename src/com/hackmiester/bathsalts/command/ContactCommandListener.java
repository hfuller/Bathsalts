package com.hackmiester.bathsalts.command;

import com.hackmiester.bathsalts.CommandListener;
import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.SkypeException;

public class ContactCommandListener extends CommandListener {

	public ContactCommandListener() {
		super(new String[] {"contact"} );
		help = "Lets you contact the author";
		disableHelp = true;
	}
	
	@Override
	protected ChatMessage doCommand(ChatMessage msg) {
		
		Chat ch; String tosend; ChatMessage m = null;
		try {
			ch = msg.getChat();
			
			lm.debug(msg.getSenderId() + " wants to contact the bot author!");
			tosend = "I've passed along your contact information to my creator.";
			
			m = ch.send(tosend);
		} catch (SkypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return m;
	}

}
