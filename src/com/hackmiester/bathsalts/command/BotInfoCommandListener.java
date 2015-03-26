package com.hackmiester.bathsalts.command;

import java.net.MalformedURLException;
import java.net.URL;

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
			
			tosend = "I have been running for " + DurationFormatUtils.formatDurationWords(duration, true, true) + ".\n";
				try {
					String master = "https://github.com/hfuller/Bathsalts/commit/master";
					String change = getHtmlTitleFromUrl(new URL(master));
					int pos = change.indexOf('·')-1; //github title separates change name with this char
					if ( pos > 0 ) {
						change = change.substring(0,pos);
					}
					tosend += "The last change made to me was: \"" + change + "\" - more details at " + master;
				} catch (MalformedURLException e) {
					//this can never happen, at least not when I have the 
					System.err.println("My last change URL is misconfigured!!");
				}
			
			m = ch.send(tosend);
		} catch (SkypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return m;
	}

}
