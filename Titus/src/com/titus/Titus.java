package com.titus;

import com.titus.network.ServerEngine;

public class Titus {
	
	public static void main(String[] args) {
		
		ServerEngine.setSingleton(new ServerEngine("127.0.0.1", 43594, 600, (byte) 1));
		new Thread(ServerEngine.getSingleton()).start();
		
	}

}
