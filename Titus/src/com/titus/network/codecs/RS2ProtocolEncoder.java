package com.titus.network.codecs;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import com.titus.network.packet.Packet;

/**
 * 
 * @author RandQm
 *
 */

public class RS2ProtocolEncoder extends MessageToMessageEncoder<Packet> {

	@Override
	protected void encode(ChannelHandlerContext context, Packet packet,
			List<Object> out) throws Exception {
		context.writeAndFlush(packet.getBuffer());
	}

}
