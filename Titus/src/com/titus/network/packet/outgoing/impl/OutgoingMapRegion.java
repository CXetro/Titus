package com.titus.network.packet.outgoing.impl;

import com.titus.game.entity.player.Player;
import com.titus.network.packet.Packet;
import com.titus.network.packet.outgoing.OutgoingPacket;

public class OutgoingMapRegion implements OutgoingPacket {

	@Override
	public void send(Player player) {
		Packet packet = new Packet(player.getContext().alloc().buffer());
		packet.writeHeader(player.getEncryption(), 73);
		packet.writeAdditionalShort(player.getLocation().getRegionX() + 6);
		packet.writeShort(player.getLocation().getRegionY() + 6);
		player.getContext().writeAndFlush(packet);	
	}

}
