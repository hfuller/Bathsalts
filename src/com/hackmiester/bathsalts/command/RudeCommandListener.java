package com.hackmiester.bathsalts.command;

import java.net.URLEncoder;

import com.hackmiester.bathsalts.CommandListener;
import com.skype.ChatMessage;

public class RudeCommandListener extends CommandListener {

	public RudeCommandListener() {
		super(new String[] {"rude","wikifun"});
		help = "Makes Wikipedia more interesting\n"
				+ "     (thanks http://orteil.dashnet.org/ ; NSFW)\n"
				+ "     Usage: !" + cmdList.get(0) + " <Wikipedia article name>";
		dontLoad=true; //TODO: reimplement rude?
	}
	
	@Override
	protected ChatMessage doCommand(ChatMessage msg) {
		
		ChatMessage m = null;
		try {
			m = msg.getChat().send("[Working]");
			
			String url = "http://orteil.dashnet.org/experiments/wikifun.php";
			if ( getArgs(msg).length() > 0 ) {
				url += "?q=" + URLEncoder.encode(getArgs(msg),"UTF-8");
			}
			
			String res = getStringFromUrl(url);
			String [] parts = res.split("<br>");
			if ( res.equals("") ) {
				m.setContent("No goddamn Wikipedia vaginal at that butt.");
			} else {
				m.setContent(res);
				try {
					if ( parts[7].contains("<b>") ) { // if there was a redirect
						res = parts[8];
					} else {
						res = parts[7];
					}
				} catch (Exception e) {
					lm.uncaughtException(Thread.currentThread(), e, res);
				}
			}
			
		} catch (Exception e) {
			lm.uncaughtException(Thread.currentThread(), e, m);
		}
				
		
		return m;
	}

}
