package com.titus.network.packet.outgoing;

import com.titus.game.entity.player.Player;

/**
 * 
 * @author RandQm
 *
 */

public interface OutgoingPacket {
	
	
	/**
	 * Sends the outgoing packet.
	 * 
	 * @param player The sending player.
	 */
	public void send(Player player);

}
