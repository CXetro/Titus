package com.titus.network.packet.incoming;

import com.titus.game.entity.player.Player;
import com.titus.network.packet.Packet;

/**
 * 
 * @author RandQm
 *
 */

public interface IncomingPacket {
	
	
	/**
	 * Handles a received packet.
	 * 
	 * @param player The player that received the packet.
	 * 
	 * @param packet The received packet.
	 */
	public void handle(Player player, Packet packet);

}
