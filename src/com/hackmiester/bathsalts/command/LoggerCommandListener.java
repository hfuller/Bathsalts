package com.hackmiester.bathsalts.command;

import com.hackmiester.bathsalts.CommandListener;
import com.skype.ChatMessage;

public class LoggerCommandListener extends CommandListener {

	public LoggerCommandListener() {
		super(new String[0]);
	}
	
	public ChatMessage processMessage(ChatMessage msg) {
		//we override this because we want to process every msg
		//not just ones in our cmdList in the constructor
		return doCommand(msg);
	}

	@Override
	protected ChatMessage doCommand(ChatMessage msg) {
		System.err.println(this.getClass().getPackage().getName() + " Cmd");
		System.err.println("CMD  : " + super.getCmd(msg));
		System.err.println("ARGS : " + super.getArgs(msg));
		return null;
	}

}
