package com.hackmiester.bathsalts.command;

import com.hackmiester.bathsalts.CommandListener;
import com.skype.ChatMessage;
import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;

public class WolframAlphaCommandListener extends CommandListener {

	private WAEngine engine;
	
	public WolframAlphaCommandListener() {
		super(new String[] {"alpha","wolfram","wa"});
		help = "Queries Wolfram Alpha\n"
				+ "     Usage: !"
				+ cmdList.get(0) + " <query>";
		engine = new WAEngine();
		engine.setAppID("H84948-8GA95XLY4Y");
		engine.addFormat("plaintext");
	}

	@Override
	protected ChatMessage doCommand(ChatMessage msg) {
		WAQuery query = engine.createQuery();
		query.setInput(getArgs(msg));
		ChatMessage m = null;
		try {
			m = msg.getChat().send("[Working]");
			WAQueryResult queryResult = engine.performQuery(query);
			if (queryResult.isError()) {
				m.setContent("Error: " + queryResult.getErrorMessage());
            } else if (!queryResult.isSuccess()) {
            	m.setContent("Alpha didn't understand your query.");
            } else {
            	StringBuilder builder = new StringBuilder();
        		
            	WAPod[] pods = queryResult.getPods();
            	if ( pods.length >= 2 ) {
            		WAPod pod = pods[1];
            		if (!pod.isError()) {
                        builder.append(pod.getTitle() + ":\n");
                        for (WASubpod subpod : pod.getSubpods()) {
                            for (Object element : subpod.getContents()) {
                                if (element instanceof WAPlainText) {
                                	builder.append(((WAPlainText) element).getText() + "\n");
                                	builder.append("\n");
                                }
                            }
                        }
                    }
            	} else {
	            	for (WAPod pod : pods) {
	                    if (!pod.isError()) {
	                        builder.append(pod.getTitle() + "\n");
	                        builder.append("------------\n");
	                        for (WASubpod subpod : pod.getSubpods()) {
	                            for (Object element : subpod.getContents()) {
	                                if (element instanceof WAPlainText) {
	                                	builder.append(((WAPlainText) element).getText() + "\n");
	                                	builder.append("\n");
	                                }
	                            }
	                        }
	                        builder.append("\n");
	                    }
	                }
            	}
	            	
            	m.setContent(builder.toString());
            	
            }
		} catch (Exception e) {
			lm.uncaughtException(Thread.currentThread(), e, msg);
			/*
			lm.debug(e.getMessage() + " " + e.getCause());
			StringBuilder sb = new StringBuilder();
			for ( StackTraceElement s : e.getStackTrace()) {
				sb.append(s + " " ) ;
			}
			lm.debug(sb.toString());
			*/
			return m;
		}
		
		System.err.println(m);
		return m;
	}

}
