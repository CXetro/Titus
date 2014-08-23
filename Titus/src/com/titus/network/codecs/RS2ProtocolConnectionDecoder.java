package com.titus.network.codecs;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.security.SecureRandom;
import java.util.List;

import com.titus.network.packet.Packet;

/**
 * 
 * @author Braeden Dempsey
 *
 */

public class RS2ProtocolConnectionDecoder extends ReplayingDecoder<ByteBuf> {

	@Override
	protected void decode(ChannelHandlerContext context, ByteBuf buffer,
			List<Object> out) throws Exception {
		final Channel channel = context.channel();
		final int loginRequestType = buffer.readByte();

		if (loginRequestType != 14) {
			System.out.println("[ERROR] Invalid login request type.");
			channel.close();
			return;
		}
		buffer.readUnsignedByte();

		Packet response = new Packet(context.alloc().buffer());
		response.getBuffer().writeLong(0);
		response.getBuffer().writeByte(0);
		response.getBuffer().writeLong(new SecureRandom().nextLong());
		channel.write(response);

		context.pipeline().replace("rs2-connection-decoder",
				"rs2-login-decoder", new RS2ProtocolLoginDecoder());
	}

}