package com.hackmiester.bathsalts;

import java.util.Arrays;
import java.util.List;

import com.skype.ChatMessage;
import com.skype.SkypeException;

public abstract class CommandListener extends Listener {
	
	public static final char COMMAND_CHARACTER = '!'; //TODO: make this configurable
	protected List<String> cmdList;
	protected String description;
	
	protected CommandListener() {
		cmdList=null;
	}
	public CommandListener(String[] cl) {
		cmdList = Arrays.asList(cl);
	}
	
	//get the list of cmds that the CommandListener will respond to.
	public String[] getCmdList() {
		return cmdList.toArray(new String[]{});
	}

	public ChatMessage processMessage(ChatMessage msg) {
		if ( cmdList.contains(getCmd(msg)) ) {
			return doCommand(msg);
		}
		return null;
	}
	
	protected String getCmd(ChatMessage msg) {
		String cmd = "", txt = "";
		
		try {
			txt = msg.getContent();
		} catch (SkypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if ( txt.length() > 0 && txt.charAt(0) != CommandListener.COMMAND_CHARACTER ) {
			//that message wasn't a command.
			return "";
		}

		//well if we get this far, at least it was a command (started with COMMAND_CHARACTER)
		if ( txt.contains(" ") ) {
			cmd  = txt.substring(1,txt.indexOf(' '));
		} else if ( txt.length() > 0 ) { //removed messages are -1 I think
			cmd  = txt.substring(1,txt.length()); //skip the COMMAND_CHARACTER
		}
		
		return cmd.toLowerCase();
		
	}
	protected String getArgs(ChatMessage msg) {
		String args = "", txt = "";
		
		try {
			txt = msg.getContent();
		} catch (SkypeException e) {
			//TODO: need to rethrow better exceptions
			System.err.println("Couldn't get message content for args");
			e.printStackTrace();
			return args;
		}
		if ( txt.charAt(0) != CommandListener.COMMAND_CHARACTER ) {
			//there were no args because there was no command.
			//TODO: can this ever happen?
			return "";
		}
		if ( txt.contains(" ") ) {
			args = txt.substring(txt.indexOf(' ')+1);
		}
		return args;
		
	}

	protected abstract ChatMessage doCommand(ChatMessage msg);
	
}
