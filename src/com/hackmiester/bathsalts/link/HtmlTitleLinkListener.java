package com.hackmiester.bathsalts.link;

import java.net.URL;
import java.util.Arrays;

import org.jsoup.nodes.Document;

import com.hackmiester.bathsalts.LinkListener;
import com.skype.ChatMessage;
import com.skype.SkypeException;

public class HtmlTitleLinkListener extends LinkListener {

	Document doc;
	private String[] uselessTitles;
	
	public HtmlTitleLinkListener() {
		super(99); //set weight to 99 (last resort)
		uselessTitles = new String[] {
				"Facebook",
				"Content Not Found",
				"Vocaroo | Voice message"
		}; //if something has this title then don't bother to report on it
		
	}
	
	protected ChatMessage doLink(ChatMessage msg, URL url) {

		String title = super.getHtmlTitleFromUrl(url);
		
		ChatMessage sentmsg = null;
		if ( title != null && title.length() > 0 && !(Arrays.asList(uselessTitles).contains(title)) ) {
			try {
				sentmsg = msg.getChat().send(title);
				return sentmsg;
			} catch (SkypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;		
	}

}
