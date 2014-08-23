package com.titus.network.codecs;

import java.util.List;

import com.titus.game.entity.player.Player;
import com.titus.network.crypto.ISAACCipher;
import com.titus.network.packet.Packet;
import com.titus.utilities.RS2String;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

/**
 * 
 * @author Braeden Dempsey
 * @author RandQm
 *
 */

public class RS2ProtocolLoginDecoder extends ReplayingDecoder<ByteBuf> {

	
	@Override
	protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> out) throws Exception {
		final Channel channel = context.channel();
		final int connectionType = buffer.readByte();
		
		if (connectionType != 16 && connectionType != 18) {
			channel.close();
			return;
		}
		final int blockLength = buffer.readByte() & 0xff;
		
		if (buffer.readableBytes() < blockLength) {
			channel.close();
			return;
		}
		if (buffer.readUnsignedByte() != 0xFF) {
			channel.close();
			return;
		}
		if (buffer.readShort() != 317) {
			channel.close();
			return;
		}
		buffer.readUnsignedByte();

		for (int i = 0; i < 9; i ++)
			buffer.readInt();

		buffer.readUnsignedByte();

		if (buffer.readUnsignedByte() != 10) {
			channel.close();
			return;
		}
		final long clientHalf = buffer.readLong();
		final long serverHalf = buffer.readLong();
		
		final int[] isaacSeed = { (int) (clientHalf >> 32), (int) clientHalf, (int) (serverHalf >> 32), (int) serverHalf };
		final ISAACCipher crypticIn = new ISAACCipher(isaacSeed);
		
		for (int i = 0; i < isaacSeed.length; i++)
			isaacSeed[i] += 50;
		
		final ISAACCipher crypticOut = new ISAACCipher(isaacSeed);
		
		@SuppressWarnings("unused")
		final int version = buffer.readInt();
		
		final String username = RS2String.formatPlayerName(RS2String.buildRS2String(buffer));
		final String password = RS2String.buildRS2String(buffer);
		
		final int responseCode = getResponseCode(username, password);
		
		Packet response = new Packet(context.alloc().buffer());
		response.getBuffer().writeByte(responseCode);
		response.getBuffer().writeByte(2); //TODO: rights
		response.getBuffer().writeByte(0);
		channel.write(response);
		
		if (responseCode != 2) {
			channel.close();
			return;
		}
		Player player = new Player(context, username, password);
		
		player.setDecryption(crypticIn);
		player.setEncryption(crypticOut);
		
		context.pipeline().replace("rs2-login-decoder", "rs2-decoder", new RS2ProtocolDecoder(player));
	}
	
	/**
	 * Retrieves the response code for the login protocol.
	 * 
	 * @param username The username of the connection.
	 * 
	 * @param password The password of the connection.
	 * 
	 * @return The resulting response code.
	 */
	private int getResponseCode(final String username, final String password) {
		if (!username.matches("[A-Za-z0-9 ]+") 
				|| username.length() < 3 || username.length() > 12)
			return 3;
		
		// TODO: Check if banned (4), Already logged in (5), World Full (7), Multiple connections from same IP (9).
		return 2;
	}

}