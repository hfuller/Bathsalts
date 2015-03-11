package com.hackmiester.bathsalts.command;

import java.util.ArrayList;

import com.hackmiester.bathsalts.CommandListener;
import com.hackmiester.bathsalts.Listener;
import com.skype.ChatMessage;
import com.skype.SkypeException;

public class HelpCommandListener extends CommandListener {

	public HelpCommandListener() {
		super(new String[]{"help"});
		help = "Crashes the bot";
	}
	
	@Override
	protected ChatMessage doCommand(ChatMessage msg) {
		ChatMessage m = null;
		if ( super.getCmd(msg).equals("help") ) {
			try {
				//make a collection of all CommandListeners (incl services)
				ArrayList<Listener> temp = new ArrayList<Listener>();
				temp.addAll(lm.cmdListeners);
				temp.addAll(lm.services);
				
				String help = new String();
				for ( Listener l : temp ) {
					String[] cmds = ((CommandListener) l).getCmdList();
					if ( cmds.length > 0 && !l.disableHelp ) {
						help = help + "!" + cmds[0];
						if ( l.help != null && l.help.length() > 0 ) {
							help = help + " - " + l.help;
						}
						help = help + "\n";
					}
				}
				help = help + "\n"
						+ "If the bot is working correctly, it was written by live:hfuller. If it isn't, I don't know who wrote it.";
				m = msg.getChat().send(help);
			} catch (SkypeException e) {
				e.printStackTrace();
			}
			return m;
		}
		return m;
	}

}
