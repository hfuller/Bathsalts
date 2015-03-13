package com.hackmiester.bathsalts.link;

import java.net.MalformedURLException;
import java.net.URL;
import com.hackmiester.bathsalts.LinkListener;
import com.skype.ChatMessage;
import com.skype.SkypeException;

public class FixerLinkListener extends LinkListener {
	
	private String[][] masterlist;
	
	public FixerLinkListener() {

		masterlist = new String[][] {
			new String[]{"m.wikipedia.org","wikipedia.org"},
			new String[]{"m.facebook.com/story.php","www.facebook.com/permalink.php"}
		};
		
	}

	@Override
	protected ChatMessage doLink(ChatMessage msg, URL url) {
		//TODO: this should be a generic link fixer. (Mobile, imgur, whatever.)
		
		for ( String[] i : masterlist ) {
			if ( url.toString().contains(i[0]) ) {
				String repl = url.toString().replace(i[0], i[1]);
				try {
					msg.getChat().send("[ FTFY: " + repl + " ] ");
				} catch (SkypeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					return lm.doLink(msg, new URL(repl));
				} catch (MalformedURLException e) {
					//that's not a valid URL. I don't know how we got here, but fuck it I guess
					return null;
				}
			}
		}
		return null;
		
	}

}
