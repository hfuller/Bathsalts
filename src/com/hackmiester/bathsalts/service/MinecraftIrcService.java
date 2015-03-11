package com.hackmiester.bathsalts.service;

import java.util.Observable;
import java.util.Observer;
import java.lang.Runtime;

import com.hackmiester.bathsalts.Service;
import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.Skype;
import com.skype.SkypeException;

import f00f.net.irc.martyr.IRCConnection;
import f00f.net.irc.martyr.clientstate.ClientState;
import f00f.net.irc.martyr.commands.MessageCommand;
import f00f.net.irc.martyr.commands.QuitCommand;
import f00f.net.irc.martyr.services.AutoJoin;
import f00f.net.irc.martyr.services.AutoReconnect;
import f00f.net.irc.martyr.services.AutoRegister;
import f00f.net.irc.martyr.services.AutoResponder;

public class MinecraftIrcService extends Service implements Observer {

	IRCConnection connection; AutoReconnect autoReconnect;
	
	public MinecraftIrcService() {
		super(new String[] {"mcirc","mcchat"} );
		help = "Bridges this chat with the Harbingers Minecraft server chat";
		disableHelp = true;
		
		//clear the subscribed list
		//subscribedChats = new ArrayList<Chat>();
		
		String nick = "null", name = "null";
		try {
			nick = Skype.getProfile().getId();
			name = Skype.getProfile().getFullName();
		} catch (SkypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			public void run() {
				autoReconnect.disable();
				connection.sendCommand(new QuitCommand("Bot is shutting down."));
				connection.disconnect();
			}
		});
		
		ClientState clientState = new ClientState( );
		connection = new IRCConnection(clientState);
		new AutoResponder( connection );
		new AutoRegister( connection, nick, nick, name );
		autoReconnect = new AutoReconnect( connection );
		new AutoJoin( connection, "#minecraft");
		
		//basically add irc message handler
		connection.addCommandObserver(this);
		
		autoReconnect.go("cart.hsv.hackmiester.com",6667);

	}

	@Override
	public void update(Observable arg0, Object arg1) {
		
		String tosend = "";
		
		if ( arg1 instanceof MessageCommand ) {
			MessageCommand msg = (MessageCommand)arg1;
			tosend = "<" + msg.getSource() + "> " + msg.getMessage();
		}
//		if ( arg1 instanceof JoinCommand ) {
//			JoinCommand jc = (JoinCommand)arg1;
//			if ( jc.getUser().toString().equals(connection.getClientState().getNick().toString()) ) {
//				return;
//			}
//			tosend = "[" + jc.getUser() + " joins " + jc.getChannel() + "]";
//		}
//		if ( arg1 instanceof PartCommand ) {
//			PartCommand jc = (PartCommand)arg1;
//			tosend = "[" + jc.getUser() + " leaves " + jc.getChannel() + "]";
//		}
//		if ( arg1 instanceof QuitCommand ) {
//			QuitCommand jc = (QuitCommand)arg1;
//			tosend = "[" + jc.getUser() + " quits (" + jc.getReason() + ")]";
//		}
		
		if (tosend.length()>0) {
			for ( Chat c : subscribedChats ) {
				try {
					c.send(tosend);
				} catch (SkypeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	protected void doService(ChatMessage msg) {
		try {
			connection.sendCommand(new MessageCommand(	
				"#minecraft",
				"<" + msg.getSenderId() + "> " + msg.getContent()
				));
		} catch (SkypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//security
	protected ChatMessage doCommand(ChatMessage msg) {
		ChatMessage m = null;
		try {
			if ( msg.getSenderId().equals("live:hfuller") ) {
				super.doCommand(msg);
			} else {
				m = msg.getChat().send("What? How did you hear about this command? Knock it off.");
			}
			
		} catch (SkypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m;
	}

}
