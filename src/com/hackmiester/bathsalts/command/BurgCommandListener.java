package com.hackmiester.bathsalts.command;

import java.net.URL;
import java.net.URLConnection;

import javax.swing.text.html.HTMLDocument;

import com.hackmiester.bathsalts.CommandListener;
import com.skype.ChatMessage;
import com.skype.SkypeException;

public class BurgCommandListener extends CommandListener {

	public BurgCommandListener() {
		super(new String[] {"burg","yeahmanforrealforrealtotes"});
		help = "Yeah man forreal forreal totes\n"
				+ "     Usage: !" + cmdList.get(0) + " <length>";
	}

	@Override
	protected ChatMessage doCommand(ChatMessage msg) {

		ChatMessage m = null;
		
		try {
			m = msg.getChat().send("[Working]");
			String length = "";
			if ( getArgs(msg).length() > 0 ) {
				length = (new Integer(getArgs(msg))).toString();
			}
			URLConnection c = (new URL("http://hackmiester.com/apps/burg/html.php?length=" + length)).openConnection();
			HTMLDocument h = getHtmlDocumentFromUrlConnection(c);
			String title = (String) h.getProperty(HTMLDocument.TitleProperty);
			m.setContent(title);
		} catch (Exception e) {
			try {
				if ( m != null) m.setContent("Sorry, bor.");
			} catch (SkypeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}		
		
		return m;
	}

}
