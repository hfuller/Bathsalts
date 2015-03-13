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
				"Content Not Found"
		}; //if something has this title then don't bother to report on it
		
	}
	
	protected ChatMessage doLink(ChatMessage msg, URL url) {
		
		URLConnection c = null;
		try {
			c = url.openConnection();
			if ( url.toString().contains("play.spotify.com") || url.toString().contains("facebook.com") ) {
				//TODO: this is a huge temporary hack to make spotify not suck. (should we just always use this as our user agent? user agent configurable?)
				c.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:36.0) Gecko/20100101 Firefox/36.0");
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
