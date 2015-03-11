package com.hackmiester.bathsalts;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;

import com.hackmiester.bathsalts.service.DebugService;
import com.hackmiester.bathsalts.service.RedactService;
import com.skype.ChatMessage;
import com.skype.ChatMessageListener;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.ChatMessage.Status;

public class ListenerManager implements UncaughtExceptionHandler {
	
	public ArrayList<Listener> cmdListeners, linkListeners, services;
	protected ChatMessageListener cml = null;

	public ListenerManager() {
		//handle exceptions (doesn't seem to work though)
		//TODO: does it?
		Thread.setDefaultUncaughtExceptionHandler(this);
		
		//listen for chat messages
		cml = new ChatMessageListener() {

			@Override
			public void chatMessageReceived(ChatMessage arg0) throws SkypeException {
				
				//sent messages (added for redact)
				//TODO: what the hell am I talking about?
				if ( arg0.getStatus() == Status.RECEIVED ) {
					
					//first log it
					logMessage(arg0);
					
					//then let all services process the message first
					for ( Listener l : ListenerManager.this.services ) {
						try {
							l.processMessage(arg0);
						} catch (Exception e) {
							arg0.getChat().send("I recognize that command, but it failed for some reason. Sorry bb.");
							ListenerManager.this.uncaughtException(Thread.currentThread(), e, arg0);
						}
					}
					
					if ( arg0.getContent().toLowerCase().contains("http") ) {
						System.err.println("Doing link listeners");
						String [] parts = arg0.getContent().split("\\s");
						//TODO: can we split this better than on whitespace? (probably not...)

						// Attempt to convert each item into an URL.
						//TODO: this try-catch block is totally fucked up
						for( String item : parts ) try {
							URL url = new URL(item);
							//got the link, now process it
							ChatMessage sentmsg = ListenerManager.this.doLink(arg0,url);
							((RedactService) ListenerManager.this.getService(RedactService.class)).addMessage(arg0.getSender(), sentmsg); //wow that's catchy
						} catch (MalformedURLException e) {
							//well that wasn't a URL, so give up.
						} catch (NullPointerException e) {
							System.err.println("Couldn't add to redact");
						}
					} else if ( arg0.getContent().charAt(0) == CommandListener.COMMAND_CHARACTER ) {
						System.err.println("Doing cmd listeners");
						for ( Listener l : ListenerManager.this.cmdListeners ) {
							//if ( ((CommandListener)l).processMessage(arg0) ) return;
							//process the cmd maybe
							ChatMessage sentmsg = ((CommandListener)l).processMessage(arg0);
							//if it processed add it to the redactor and return
							if ( sentmsg != null ) {
								((RedactService) ListenerManager.this.getService(RedactService.class)).addMessage(arg0.getSender(), sentmsg);
								return;
							}
						}
					} 
				}
				
			}

			public void chatMessageSent(ChatMessage arg0) throws SkypeException {
				
				if ( arg0.getStatus() == Status.SENT ) {
					//first log it
					logMessage(arg0);
				}
				//then... don't do anything, I guess
				
			}
			
			public void logMessage(ChatMessage arg0) throws SkypeException {
				System.err.println("[ " + arg0.getChat().getWindowTitle() + 
						" <" + arg0.getChat().getId() + 
						"> ] : ");
				System.err.println("\t[" + arg0.getStatus() + "] " +
						arg0.getSenderDisplayName() + 
						" <" + arg0.getSenderId() +
						"> : " + arg0.getContent());
				//TODO: we should have better logging...
			}
			
		};
		try {
			Skype.addChatMessageListener(cml);
		} catch (SkypeException e) {
			//TODO: not a real exception handler. we should really die if this happens
			e.printStackTrace();
		}

		//finally load listeners
		this.loadAllListeners();
		
	}

	protected Service getService(@SuppressWarnings("rawtypes") Class class1) {
		for ( Listener s : services ) {
			if ( ((Service)s).getClass().equals(class1)) return ((Service)s);
		}
		return null;
	}

	private void loadAllListeners() {
		//TODO: better logging in this method
		
		//command listeners
		cmdListeners = new ArrayList<Listener>();
		loadListeners(new File("com/hackmiester/bathsalts/command/"), cmdListeners);
		Collections.sort(cmdListeners);
		System.err.println("Command listeners:");
		for ( Listener l : cmdListeners ) { System.err.println(l); }
		
		//link listeners
		linkListeners = new ArrayList<Listener>();
		loadListeners(new File("com/hackmiester/bathsalts/link/"), linkListeners);
		Collections.sort(linkListeners);
		System.err.println("Link listeners:");
		for ( Listener l : linkListeners ) { System.err.println(l); }
		
		//services
		services = new ArrayList<Listener>();
		loadListeners(new File("com/hackmiester/bathsalts/service/"), services);
		Collections.sort(services);
		System.err.println("Services:");
		for ( Listener l : services ) { System.err.println(l); }
	}
	
	private void loadListeners(File file, ArrayList<Listener> listeners) {
		URL[] urls; File[] classfiles; ClassLoader cl;
		
		if ( file.isDirectory() ) {
			try {
				urls = new URL[]{file.toURI().toURL()};
				cl = new URLClassLoader(urls);
				System.err.print("Loading from directory " + file);
				classfiles = file.listFiles(new FilenameFilter() {
					public boolean accept(File arg0, String arg1) {
						return arg1.substring(arg1.length()-6,arg1.length()).equals(".class");
					}});
			    for ( File f : classfiles ) {
			    	String toLoad = f.toString().replace(".class","").replace('/', '.');
			    	try {
						Object temp = cl.loadClass(toLoad).newInstance();
						((Listener) temp).setListenerManager(this);
						listeners.add((Listener) temp);
						//System.err.println("Loaded " + toLoad);
						System.err.print(".");
						System.out.println(temp);
					} catch (ClassNotFoundException e) {
						System.err.println("Failed loading " + toLoad);
					} catch (InstantiationException e) {
						System.err.println("Failed instantiating " + toLoad);
					} catch (IllegalAccessException e) {
						System.err.println("Can't instantiate " + toLoad);
					}
			    }
			    System.err.println();
			} catch (MalformedURLException e) {
				//e.printStackTrace();
				System.err.println("Failed loading from directory " + file);
			}
		} else {
			System.err.println("Not loading from directory " + file + " (doesn't exist)");
		}
		
	}

	public ChatMessage doLink(ChatMessage msg, URL url) {
		System.err.println("Doing Link");
		for ( Listener l : ListenerManager.this.linkListeners ) {
			System.err.println(l);
			ChatMessage sentmsg = ((LinkListener) l).doLink(msg,url);
			if ( sentmsg != null ) return sentmsg;
		}
		return null;
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		//TODO: is this how we should be handling exceptions?
		((DebugService) this.getService(DebugService.class)).uncaughtException(t, e);
	}

	public void uncaughtException(Thread currentThread, Exception e, Object x) {
		if ( ( e instanceof com.skype.NotAttachedException )
				| e instanceof com.skype.CommandFailedException
				| e instanceof com.skype.connector.NotAttachedException 
				| e.toString().contains("NotAttachedException") ) {
			System.err.print("Suppressed Skype exception ");
			System.err.println(e);
			//TODO: if we suppress a bunch of these, we are permanently disconnected and should do something
		} else if ( !(e.toString().contains("403")) ) {
			this.debug("Did not suppress exception " + e);
			this.debug("Error processing " + x.toString());
			this.debug(e.getClass());
			this.uncaughtException(currentThread, e);
		}
	}

	public void debug(Object x) {
		((DebugService) this.getService(DebugService.class)).debug(x.toString());
		//TODO: this fails if we didn't load a DebugService
		//TODO: unrelated to this method, but I just realized all the redact shit will fail if we didn't load a RedactService. so that's probably worth also looking into
	}
	
}
