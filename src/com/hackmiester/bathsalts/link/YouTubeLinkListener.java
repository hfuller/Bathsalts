package com.hackmiester.bathsalts.link;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.time.DurationFormatUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.hackmiester.bathsalts.LinkListener;
import com.skype.ChatMessage;

public class YouTubeLinkListener extends LinkListener {
	
	//TODO: minor cleanup, but otherwise nothign. this is the best link listener ever. well, okay maybe we can support playlists or something
	
	public YouTubeLinkListener() {
		//service = new YouTubeService("Bathsalts");
		//service = new YouTubeService("Bathsalts","AI39si6izu9-owkuBNTEMGBjLPghmb2Anc2YDXoeWhNLxoUAq2V4hABau7LlSeccrIlu-eC-MfktF74abWt4BVHMCkgSlASHeA");
	}

	@Override
	protected ChatMessage doLink(ChatMessage msg, URL url) {
		if (url.toString().contains("youtube.com/watch") | url.toString().contains("youtu.be/")) {
			
			String id = "";
			//get the video id into the string id
			Pattern pattern = Pattern.compile("(?<=(?:v|i)=)[a-zA-Z0-9-]+(?=&)|(?<=(?:v|i)/)[^&\n]+|(?<=embed/)[^\"&\n]+|(?<=(?:v|i)=)[^&\n]+|(?<=youtu.be/)[^&\n]+");
			Matcher matcher = pattern.matcher(url.toString());
			if ( matcher.find() ) {
				id = url.toString().substring(matcher.start(),matcher.end());
			} else {
				return null;
			}
			
			try {
				URL ytXmlUrl = new URL("http://gdata.youtube.com/feeds/api/videos/" + id + "?v=2");
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(ytXmlUrl.openConnection().getInputStream());
				doc.getDocumentElement().normalize();
				
				NodeList titleNL = doc.getElementsByTagName("title");
				String title = titleNL.item(0).getTextContent();
				
				//<yt:duration seconds="378"/>
				NodeList durationNL = doc.getElementsByTagName("yt:duration");
				String duration = durationNL.item(0).getAttributes().getNamedItem("seconds").getNodeValue();
				duration = DurationFormatUtils.formatDurationWords(Integer.parseInt(duration)*1000, true, true);
				
				NodeList nameNL = doc.getElementsByTagName("name");
				String name = nameNL.item(0).getTextContent();
				
				String toSend = title + "\n"
						+ "     " + duration + " - Posted by " + name;
				
				return msg.getChat().send(toSend);
				
			} catch (Exception e) {
				lm.uncaughtException(Thread.currentThread(), e);
			}
			
			
			ChatMessage sentmsg = null;
			return sentmsg;
		}
		return null;
	}

}
