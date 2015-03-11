package com.hackmiester.bathsalts; 

import com.skype.Skype;
import com.skype.SkypeException;


public class BathsaltsLauncher {

	public static void main(String[] args) {
		System.err.println("Connecting to Skype...");
		try {
			System.err.println("Skype Client Version: " + Skype.getVersion());
			System.err.println("           User Name: " + Skype.getProfile().getId());
			System.err.println("        Display Name: " + Skype.getProfile().getFullName());
		} catch (SkypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
		
		System.err.println("Done. Loading processing classes...");
		final ListenerManager lm = new ListenerManager();
		System.out.println(lm);
		
		System.err.println("Done. Running.");
		System.err.println();
		
		try {
			while ( true ) { //this is only pointless if you actually System.exit
				Thread.sleep(86000000); //23.88888... hours
				System.exit(0); //the skype API is so unstable that we have to do this.
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
