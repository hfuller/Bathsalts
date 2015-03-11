package com.hackmiester.bathsalts.command;

import com.hackmiester.bathsalts.CommandListener;
import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.SkypeException;

public class ChatInfoCommandListener extends CommandListener {

	public ChatInfoCommandListener() {
		super(new String[] {"chatinfo","info"} );
		help = "Displays technical info about this chat";
	}
	
	@Override
	protected ChatMessage doCommand(ChatMessage msg) {
		
		Chat ch; String tosend; ChatMessage m = null;
		try {
			ch = msg.getChat();
			
			tosend = 
					"Chat topic: " +
					ch.getWindowTitle() +
					"\n" +
					"Chat ID: " +
					ch.getId() + 
					"\n" +
					"Chat Status: " +
					ch.getStatus()
					;
			
			m = ch.send(tosend);
		} catch (SkypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return m;
	}

}
