package com.hackmiester.bathsalts.link;

import java.net.MalformedURLException;
import java.net.URL;

import com.hackmiester.bathsalts.LinkListener;
import com.skype.ChatMessage;
import com.skype.SkypeException;

public class WikipediaLinkListener extends LinkListener {

	@Override
	protected ChatMessage doLink(ChatMessage msg, URL url) {
		//TODO: this should be a generic link fixer. (Mobile, imgur, whatever.)
		
		if (url.toString().contains("m.wikipedia.org")) {
			try {
				String repl = url.toString().replace("m.wikipedia.org", "wikipedia.org");
				msg.getChat().send("[ Non-mobile link: " + repl + " ]");
				return lm.doLink(msg, new URL(repl));
			} catch (MalformedURLException e) {
				return null;
			} catch (SkypeException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
