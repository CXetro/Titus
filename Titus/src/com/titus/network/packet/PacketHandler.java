package com.titus.network.packet;

import java.util.HashMap;
import java.util.Map;

import com.titus.game.entity.player.Player;
import com.titus.network.packet.incoming.IncomingPacket;
import com.titus.network.packet.outgoing.OutgoingPacket;

/**
 * 
 * @author RandQm
 *
 */

public class PacketHandler {
	
	/**
	 * A map holding the possible incoming packets.
	 */
	private static Map<Integer, IncomingPacket> inPackets = new HashMap<>();
	
	
	/**
	 * Registers the possible incoming packets.
	 */
	public static void registerIncomingPackets() {
	
	}
	
	/**
	 * Handles a received packet for a player.
	 * 
	 * @param player The player who received the packet.
	 * 
	 * @param packet The received packet.
	 */
	public static void handleReceivedPacket(Player player, Packet packet) {
		if (player != null && packet != null && inPackets.containsKey(packet.getOpcode()))
			inPackets.get(packet.getOpcode()).handle(player, packet);
	}
	
	/**
	 * Sends a packet to the player client.
	 * 
	 * @param player The player.
	 * 
	 * @param out The packet to send.
	 */
	public static void sendPacket(Player player, OutgoingPacket out) {
		if (player != null && out != null)
			out.send(player);
	}

}
