package com.hackmiester.bathsalts.link;

import java.net.URL;

import com.hackmiester.bathsalts.LinkListener;
import com.skype.ChatMessage;

public class QuickmemeLinkListener extends LinkListener {

	//TODO: probably just remove this because no one gives a damn about quickmeme anymore and I never wrote this anyway. instead I should port the imgur listener from python.
	
	@Override
	protected ChatMessage doLink(ChatMessage msg, URL url) {
		return null;
	}

}
