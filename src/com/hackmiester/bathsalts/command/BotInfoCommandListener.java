package com.hackmiester.bathsalts.command;

import com.hackmiester.bathsalts.CommandListener;
import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.SkypeException;

public class BotInfoCommandListener extends CommandListener {

	public BotInfoCommandListener() {
		super(new String[] {"about","botinfo"} );
		help = "Displays the bot's life story";
	}
	
	@Override
	protected ChatMessage doCommand(ChatMessage msg) {
		
		Chat ch; String tosend; ChatMessage m = null;
		try {
			ch = msg.getChat();
			
			tosend =	"Well, a long, long time ago in a galaxy far, far away, " +
						"live:hfuller wrote a Skype bot called 'bat' in Python. It was " + 
						"fairly poorly written, but it gained much character and a " +
						"small following in the Skype universe. So, to make it easier " +
						"to maintain, it was rewritten in Java, which is the current " +
						"iteration. Due to the bot's sometimes erratic behavior, and " +
						"to keep the 'bat' theme, the new iteration is known as " + 
						"'Bathsalts.' It's still maintained as of 2015, although very, " +
						"very slowly. If anyone gives a shit, the source code " +
						"is available. You should know, though... if you want to " +
						"learn Java, this would *not* be a good reference project, " +
						"because parts of it are pretty bad. Anyway, here it is: " +
						"https://github.com/hfuller/Bathsalts\n" +
						"If you want to talk about the bot or make a feature request, " +
						"you can use the secret command !contact to get with the " +
						"creator. Thanks for using the bot!"
					;
			
			m = ch.send(tosend);
		} catch (SkypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return m;
	}

}
