package com.hackmiester.bathsalts.command;

import com.hackmiester.bathsalts.CommandListener;
import com.skype.ChatMessage;
import com.skype.ContactList;
import com.skype.Skype;
import com.skype.SkypeException;

public class AddContactCommandListener extends CommandListener {

	public AddContactCommandListener() {
		super(new String[] {"addme","add"});
		help = "Sends you a friend request";
	}

	@Override
	protected ChatMessage doCommand(ChatMessage msg) {
		ChatMessage m = null;
		try {
			ContactList cl = Skype.getContactList();
			cl.addFriend(msg.getSender(), "beep boop am robot");
			m = msg.getChat().send("Check your Recents in a couple of minutes.");
		} catch (SkypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return m;
	}

}
