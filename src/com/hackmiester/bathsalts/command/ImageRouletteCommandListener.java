package com.hackmiester.bathsalts.command;

import java.io.BufferedReader;
import java.net.URL;

import com.hackmiester.bathsalts.CommandListener;
import com.skype.ChatMessage;
import com.skype.SkypeException;

public class ImageRouletteCommandListener extends CommandListener {

	public ImageRouletteCommandListener() {
		super(new String[] {"roulette","imgr","puushr","puush","imgur","objection"});
		help = "Gets a random image from Image Roulette\n"
				+ "     Usage: !" + cmdList.get(0) + " <site>\n"
				+ "     Supports puush, imgur, objection, youtube, reddit";
		dontLoad=true; //TODO: rewrite image roulette
	}

	@Override
	protected ChatMessage doCommand(ChatMessage msg) {

		ChatMessage m = null;
		
		String site = "";
		String[] args =  getArgs(msg).split(" ");
		if ( args.length > 0 ) {
			site = args[0].toLowerCase();
		}
		if ( getCmd(msg).toLowerCase().equals("imgur") ) {
			site = "imgur";
		} else if ( getCmd(msg).toLowerCase().startsWith("obj") ) {
			site = "objection";
		}
		
		try {
			m = msg.getChat().send("[Working]");
			BufferedReader br = getBufferedReaderFromUrl("http://hackmiester.com/apps/roulette/_getrandomurl.php?site=" + site);
			String result = br.readLine();
			m.setContent(result);
			
			//try for title
			if ( !( args.length > 1 && args[1].startsWith("q") ) ) {
				lm.doLink(msg, new URL(result));
			}
		} catch (Exception e) {
			try {
				if ( m != null) m.setContent("Yeah, I don't even know, dude.");
			} catch (SkypeException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}		
		
		return m;
	}

}
