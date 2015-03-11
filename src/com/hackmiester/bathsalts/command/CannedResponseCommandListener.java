package com.hackmiester.bathsalts.command;

import java.util.ArrayList;
import com.hackmiester.bathsalts.CommandListener;
import com.skype.ChatMessage;
import com.skype.SkypeException;

public class CannedResponseCommandListener extends CommandListener {
	
	private String[][] masterlist;
	
	public CannedResponseCommandListener() {

		masterlist = new String[][] {
			new String[]{"sup","nm"},
			new String[]{"wampwamp","what it do"},
			new String[]{"supson","¯\\_(ツ)_/¯"},
			new String[]{"shrug","¯\\_(ツ)_/¯"},
			new String[]{"backflip","┬─┬ノ( º _ ºノ)"},
			new String[]{"patontheback","http://i.eho.st/pp4uf3xj.png"},
			new String[]{"mystery", 
			".    ___\n" + 
			"   _/ ..\\\n" + 
			"  ( \\  0/__  it is a mystery\n" + 
			"   \\     \\__)\n" + 
			"   /       \\\n" + 
			"  /        _\\\n" + 
			"`\"\"\"\"\"``\n"
			}
		};
		
		cmdList = new ArrayList<String>();
		for ( String[] s : masterlist ) {
			cmdList.add(s[0]);
		}
		
		help = "nm";
		
	}

	public ChatMessage doCommand(ChatMessage msg) {
		ChatMessage m=null;
		try {
			for ( String[] s : masterlist ) {
				if ( s[0].equals(getCmd(msg)) ) {
					m = msg.getChat().send(s[1]);
					return m;
				}
			}
			m = msg.getChat().send("nm, I guess"); //should never happen
		} catch (SkypeException e) {
			e.printStackTrace();
		}
		return m;
	}

}
