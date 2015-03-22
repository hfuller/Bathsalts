package com.hackmiester.bathsalts.link;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

import javax.swing.text.html.HTMLDocument;

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
		
		URLConnection c = null;
		try {
			c = url.openConnection();
			
			//TODO: BIG UGLY HACK SECTION. need to have an extensible way to set certain headers for certain sites.
			if ( url.toString().contains("play.spotify.com") || url.toString().contains("facebook.com") ) {
				//cause these sites to not redirect us because we have a shitty/unknown browser
				c.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:36.0) Gecko/20100101 Firefox/36.0");
			}
			if ( url.toString().contains("fjcdn") ) {
				//cause FunnyJunk to actually redirect us to the HTML page which has a title
				c.setRequestProperty("Accept", "text/html");
			}
		} catch (IOException e1) {
			lm.uncaughtException(Thread.currentThread(), e1, url);
		}
		
		String response = c.getHeaderFields().get(null).get(0);
		//System.out.println(response);
		if ( response.endsWith("301 Moved Permanently") || response.endsWith("301 Moved") || response.endsWith("302 Moved Temporarily") ) {
			try {
				System.out.println("Following redirect to " + c.getHeaderField("Location"));
				lm.doLink(msg, new URL(c.getHeaderField("Location")));
				return null;
			} catch (MalformedURLException e) {
				lm.uncaughtException(Thread.currentThread(), e, c.getHeaderFields());
			}
		}			
		
		HTMLDocument htmlDoc = getHtmlDocumentFromUrlConnection(c);
		String title = (String) htmlDoc.getProperty(HTMLDocument.TitleProperty);
		ChatMessage sentmsg = null;
		if ( title != null && title.length() > 0 && !(Arrays.asList(uselessTitles).contains(title)) ) {
			try {
				sentmsg = msg.getChat().send(title);
			} catch (SkypeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return sentmsg;
	}

}
