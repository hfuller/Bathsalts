package com.hackmiester.bathsalts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import com.skype.ChatMessage;

public abstract class Listener implements Comparable<Listener> {

	final int weight;
	public boolean dontLoad;
	public String help = null;
	public boolean disableHelp = false; //TODO: make this configurable
	protected ListenerManager lm = null;
	
	protected Listener() {
		weight = 0;
		dontLoad = false;
	}
	protected Listener(int w) {
		weight = w;
		dontLoad = false;
	}
	
	public abstract ChatMessage processMessage(ChatMessage msg);
	
	@Override
	public int compareTo(Listener arg0) {
		return weight-arg0.weight;
	}
	
	//sets the listener manager in case we need to reference it later
	public void setListenerManager(ListenerManager listenerManager) {
		lm = listenerManager;
	}
	
	//TODO: everything after this line is shit.
	//TODO: can we refactor this into a "Tools" class or somethign?

	public String getHtmlTitleFromUrl(URL url) {
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
		if ( response.endsWith("301 Moved Permanently") || response.endsWith("301 Moved") || response.endsWith("302 Moved Temporarily") ) {
			try {
				System.out.println("Following redirect to " + c.getHeaderField("Location"));
				return getHtmlTitleFromUrl(new URL(c.getHeaderField("Location")));
			} catch (MalformedURLException e) {
				lm.uncaughtException(Thread.currentThread(), e, c.getHeaderFields());
			}
		}			
		
		HTMLDocument htmlDoc = getHtmlDocumentFromUrlConnection(c);
		String title = (String) htmlDoc.getProperty(HTMLDocument.TitleProperty);

		return title;

	}
	
	@Deprecated
	public HTMLDocument getHtmlDocumentFromUrl(String s) {
		lm.debug("DEPRECATED use of getHtmlDocumentFromUrl"); //TODO: is this still in use somewhere?
		try {
			return getHtmlDocumentFromUrlConnection((new URL(s)).openConnection());
		} catch (Exception e) {
			lm.uncaughtException(Thread.currentThread(), e, s);
		}
		return null;
	}
	public HTMLDocument getHtmlDocumentFromUrlConnection(URLConnection connection) {
//		try {
			InputStream is;
			try {
				is = connection.getInputStream();
			} catch (FileNotFoundException e1) {
				lm.debug("Link returned 404: " + connection.getURL().toString() + " (" + e1.toString() + ")");
				//TODO: this really should throw an exception
				return null;
			} catch (IOException e) {
				lm.uncaughtException(Thread.currentThread(), e, connection);
				return null;
			}
//			InputStreamReader isr = new InputStreamReader(is);
//			BufferedReader br = new BufferedReader(isr);
			HTMLEditorKit htmlKit = new HTMLEditorKit();
			HTMLDocument htmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();
			
			//fixes charset exceptions
			htmlDoc.putProperty("IgnoreCharsetDirective", new Boolean(true));
			
			try {
				htmlKit.read(is, htmlDoc, 0);
			} catch (RuntimeException e) {
				lm.debug("Exception reading " + connection.getURL().toString() + ": " + e.toString());
			} catch (IOException e) {
				lm.uncaughtException(Thread.currentThread(), e, connection);
			} catch (BadLocationException e) {
				lm.debug("Bad location: " + connection.getURL().toString() + " (" + e.toString() + ")");
			}
			return(htmlDoc);
//		} catch (Exception e) {
//			lm.uncaughtException(Thread.currentThread(), e, connection);
//			return null;
//		}
		
		
	}
	
	public BufferedReader getBufferedReaderFromUrl(String s) { //TODO: should this method be in here?
		URL url;
		try {
			url = new URL(s);
			return getBufferedReaderFromUrl(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public BufferedReader getBufferedReaderFromUrl(URL url) { //TODO: should this method be in here?
		URLConnection connection = null;
		try {
			connection = url.openConnection();
			InputStream is = connection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			return(br);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String getStringFromUrl( URL url ) { //TODO: should this method be in here?
		BufferedReader reader = getBufferedReaderFromUrl(url);
		StringBuilder builder = new StringBuilder();
		String aux = "";

		try {
			while ( reader != null && (aux = reader.readLine()) != null) {
			    builder.append(aux);
			}
		} catch (Exception e) {
			//TODO: is this how we're throwing exceptions now?
			lm.debug("getStringFromUrl!!!");
			lm.debug("URL: " + url.toString());
			lm.debug("BufferedReader: " + reader.toString());
			lm.debug("StringBuilder: " + builder);
			lm.uncaughtException(Thread.currentThread(), e, url);
		}

		return builder.toString();
	}
	public String getStringFromUrl(String u) {
		try {
			return getStringFromUrl(new URL(u));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}


	
}
