package com.hackmiester.bathsalts.command;

import org.apache.commons.lang.time.DurationFormatUtils;

import com.hackmiester.bathsalts.CommandListener;
import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.SkypeException;

public class BotInfoCommandListener extends CommandListener {

	private long startTime;
	
	public BotInfoCommandListener() {
		super(new String[] {"about","botinfo"} );
		help = "Displays information about the bot";

		startTime = System.currentTimeMillis();
		
	}
	
	@Override
	protected ChatMessage doCommand(ChatMessage msg) {
		
		Chat ch; String tosend; ChatMessage m = null;
		try {
			ch = msg.getChat();
			
			long duration = System.currentTimeMillis() - startTime;
			
			tosend =	"I have been running for " + DurationFormatUtils.formatDurationWords(duration, true, true) + ".";
			
			m = ch.send(tosend);
		} catch (SkypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return m;
	}

}
