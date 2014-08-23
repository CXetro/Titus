package com.titus.network.codecs;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

import com.titus.game.entity.player.Player;
import com.titus.network.crypto.ISAACCipher;
import com.titus.utilities.RS2String;

/**
 * 
 * @author Braeden Dempsey
 * 
 */

public class RS2ProtocolLoginDecoder extends ReplayingDecoder<ByteBuf> {
	
	private RS2ProtocolLoginState state = RS2ProtocolLoginState.INITIAL_STATE;
	
	@Override
	protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> out) throws Exception {
		final Channel channel = context.channel();
		final int connectionType = buffer.readByte();
		
		if (!channel.isRegistered()) {
			return;
		}
		
		switch (state) {
		
		case INITIAL_STATE:
			if (!channel.isRegistered())
				return;
			state = RS2ProtocolLoginState.CONNECTION_STATE;
			break;
		
		case CONNECTION_STATE:
			if (buffer.readableBytes() < 2)
				return;
			
			if (connectionType != 14) {
				System.out.println("[ERROR] Invalid login request. Connection type: " + connectionType + ".");
				channel.close();
				return;
			}
			state = RS2ProtocolLoginState.LOGGING_IN_STATE;
			break;
			
		case LOGGING_IN_STATE:
			if (buffer.readableBytes() < 2)
				return;
			
			if (connectionType != 16 && connectionType != 18) {
				System.out.println("[ERROR] Invalid login request. Connection type: " + connectionType + ".");
				channel.close();
				return;
			}
			
			int loginEncryption = buffer.readUnsignedByte() - (36 + 1 + 1 + 2);
			if (loginEncryption <= 0) {
				System.out.println("[ERROR] Invalid login encryption request.");
				channel.close();
				return;
			}
			
			if (buffer.readUnsignedShort() != 317) {
				System.out.println("[ERROR] Invalid client version request.");
				channel.close();
				return;				
			}
			
			buffer.readByte();
			
			for (int i = 0; i < 9; i++)
				buffer.readInt();
			
			buffer.readByte();
			
			if (buffer.readByte() != 10) {
				System.out.println("[ERROR] Unable to decode RSA.");
				channel.close();
				return;
			}
			
			long clientHalf = buffer.readLong();
			long serverHalf = buffer.readLong();
			int[] isaacSeed = { (int) (clientHalf >> 32), (int) clientHalf, (int) (serverHalf >> 32), (int) serverHalf };
			ISAACCipher inCipher = new ISAACCipher(isaacSeed);
			for (int i = 0; i < isaacSeed.length; i++)
				isaacSeed[i] += 50;
			ISAACCipher outCipher = new ISAACCipher(isaacSeed);
			
			int version = buffer.readInt();
			
			final String username = RS2String.formatPlayerName(RS2String.buildRS2String(buffer));
			final String password = RS2String.buildRS2String(buffer);
			

			if (!username.matches("[A-Za-z0-9 ]+") || username.length() > 12 || username.length() < 3) {
				return;
			}
			
			Player player = new Player(context, username, password);
			player.setEncryption(outCipher);
			player.setUsername(username);
			player.setPassword(password);
			
			channel.pipeline().replace("rs2-login-decoder", "rs2-decoder", new RS2ProtocolDecoder(player));
			
			
			break;
	
		default:
			break;
	
		}
		
	}
	

}