package com.hackmiester.bathsalts.link;

import java.net.URL;

import com.hackmiester.bathsalts.LinkListener;
import com.skype.ChatMessage;

public class TwitterLinkListener extends LinkListener {

	@Override
	protected ChatMessage doLink(ChatMessage msg, URL url) {
		// TODO This isn't written! Port from python (probably rewrite becuase that one is buggy)
		if (url.toString().contains("twitter.com/")) {
			throw new RuntimeException("Twitter not implemented");
		} else {
			return null;
		}
		//
	}

}
