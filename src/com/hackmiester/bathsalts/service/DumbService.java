package com.hackmiester.bathsalts.service;

import java.util.Random;

import com.hackmiester.bathsalts.Service;
import com.skype.Chat;
import com.skype.ChatMessage;
import com.skype.SkypeException;

public class DumbService extends Service {

	private Random rnd;
	
	public DumbService() {
		super(new String[] {"dumb"});
		help = "Toggles responses to certain phrases in chat";
		rnd = new Random();
		disableHelp=true;
	}

	@Override
	protected void doService(ChatMessage msg) {
		String resp = null;
		try {
			String s = msg.getContent().toLowerCase();
			if ( s.contains("bat sucks")
					|| s.contains("fuck you bat") ) {
				//msg.getChat().send("wot the fok did ye just say 2 me m8? i dropped out of newcastle primary skool im the sickest bloke ull ever meet & ive nicked 300 candy bars from tha corner store. im trained in street fitin' & im the strongest foker in tha entire newcastle gym. yer nothin to me but a cheeky lil dickhead w/ a hot mum & fake bling. ill waste u and smash a fokin bottle oer yer head bruv, i swer 2 christ. ya think u can fokin run ya gabber at me whilst sittin on yer arse behind a lil screen? think again wanka. im callin me homeboys rite now preparin for a proper rumble. tha rumble thatll make ur nan sore jus hearin about it. yer a waste bruv. my homeboys be all over tha place & ill beat ya to a proper fokin pulp with me fists wanka. if i aint satisfied w/ that ill borrow me m8s cricket bat & see if that gets u the fok out o' newcastle ya daft kunt. if ye had seen this bloody fokin mess commin ye might a' kept ya gabber from runnin. but it seems yer a stewpid lil twat, innit? ima shite fury & ull drown in it m8. ur in proper mess ya knobhead.");
				String[] uwotm8 = {
						"u are 1 fucking cheeky kunt mate i swear i am goin 2 wreck u i swear on my mums life and i no u are scared lil bitch gettin your mates to send me messages saying dont meet up coz u r sum big bastard with muscles lol fuckin sad mate really sad jus shows what a scared lil gay boy u are and whats all this crap ur mates sendin me about sum bodybuildin website that 1 of your faverite places to look at men u lil fuckin gay boy fone me if u got da balls cheeky prick see if u can step up lil queer.",
						"wot the fok did ye just say 2 me m8? i dropped out of newcastle primary skool im the sickest bloke ull ever meet & ive nicked ova 300 chocolate globbernaughts frum tha corner shop. im trained in street fitin' & im the strongest foker in tha entire newcastle gym. yer nothin to me but a cheeky lil bellend w/ a fit mum & fakebling. ill waste u and smash a fokin bottle oer yer head bruv, i swer 2 christ. ya think u can fokin run ya gabber at me whilst sittin on yer arse behind a lil screen? think again wanka. im callin me homeboys rite now preparin for a proper scrap. A roomble thatll make ur nan sore jus hearin about it. yer a waste bruv. me crew be all over tha place & ill beat ya to a proper fokin pulp with me fists wanka. if i aint satisfied w/ that ill borrow me m8s cricket paddle & see if that gets u the fok out o' newcastle ya daft kunt. if ye had seen this bloody fokin mess commin ye might a' kept ya gabber from runnin. but it seems yea stupid lil twat, innit? ima shite fury & ull drown in it m8. ur ina proper mess knob.",
						"il bash ye fookin ead in i sware on me mom if u got sumthn 2 say say it 2 me face ur a lil gey boi lol i cud own u irl 1v1 fite me irl fgt il rek u u better shut ur moth u cheeky lil kunt i swer 2 christ il hook u in the gabber m8 il rek ur fokn shit i swer 2 christ il teece u﻿ 4 spekin 2 me lik that u little cunt m8"
				};
				int idx = rnd.nextInt(uwotm8.length);
				resp = (uwotm8[idx]);
			}
			//TODO: need to rearchitect this whole section to be more like CannedResponseCommandListener
			if ( s.equals("v") ) {
				resp = ("YOU SUCK AT PASTE");
			}
			if ( s.contains("thanks bat") | s.contains("thank you bat") | s.contains("ty bat") ) {
				resp = ("np");
			}
			
			if ( resp != null ) msg.getChat().send(resp);				

		} catch (SkypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//we hav eto override the next two so that chats in the list are
	//NOT processed. i.e., this one is opt-out
	protected ChatMessage doCommand(ChatMessage msg) {
		//TODO: we could convert this overload into a flag on the Service and have the stock doCommand check for that flag.
		ChatMessage m = null;
		try {
			Chat c = msg.getChat();
			if ( subscribedChats.remove(c) ) {
				m = c.send("Opted into " + this.getClass().getSimpleName());
			} else {
				subscribedChats.add(c);
				m = c.send("Opted out of " + this.getClass().getSimpleName());
			}
			System.out.println("Saving subscriptions");
			this.saveSubscriptions();
		} catch (SkypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return m;
	}
	public ChatMessage processMessage(ChatMessage msg) {
		
		try {
			if ( !( subscribedChats.contains(msg.getChat()) ) ) {
				this.doService(msg);
			}
		} catch (SkypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//TODO: hacky code duplication workaround - see above todo
		if ( cmdList.contains(getCmd(msg)) ) {
			return doCommand(msg);
		}
		return null;
	}

}
