package com.titus.game.entity.player;

import io.netty.channel.ChannelHandlerContext;

import com.titus.game.attribute.AttributeKey;
import com.titus.game.attribute.AttributeMap;
import com.titus.game.entity.Entity;
import com.titus.game.entity.Location;
import com.titus.network.crypto.ISAACCipher;

/**
 * 
 * @author Braeden Dempsey
 *
 */

public class Player extends Entity {
	
	
	/** All the string values of a player. */
	private String username, password;
	
	/** Coordinates of the {@link Location} to the player. */
	private Location location;
	
	/** Creates a instance of the {@link ChannelHandlerContext}. */
	private ChannelHandlerContext context;
	
	/** Creates a instance of the {@link AttributeMap}. */
	private AttributeMap attributes;
	
	/** Creates an instance of the {@link ISAACCipher}. */
	private static ISAACCipher encryptor, decryptor;
	
	/** Constructs a new {@link AttributeKey} for the encryptor. */
	private final static AttributeKey<ISAACCipher> ENCRYPTOR = AttributeKey.valueOf("encryptor", encryptor);
	
	/** Constructs a new {@link AttributeKey} for the decryptor. */
	private final static AttributeKey<ISAACCipher> DECRYPTOR = AttributeKey.valueOf("decryptor", decryptor);
	
	/**
	 *  Constructs a new {@link Player}.
	 * @param context
	 * 		the channel of the player.
	 * @param username
	 * 		the username of the player.
	 * @param password
	 * 		the password of the player.
	 */
	public Player(final ChannelHandlerContext context, final String username, final String password) {
		this.context = context;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * @return the username.
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * @return the password.
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 *  Constructs a new {@link Location}.
	 * @return
	 * 		the locaton;
	 */
	public Location getLocation() {
		return location;
	}
	
	/**
	 *  Constructs a public instance for the {@link AttributeMap}.
	 * @return
	 */
	public AttributeMap getAttributes() {
		return attributes;
	}
	
	/**
	 *  Constructs a new {@link ISAACCipher}.
	 * @return
	 * 		the encryptor.
	 */
	public ISAACCipher getEncryption() {
		return getAttributes().get(ENCRYPTOR);
	}
	
	/**
	 *  Constructs a setter for the encryptor {@link ISAACCipher}.
	 * @param cipher
	 * 		the variable being set.
	 * @return
	 * 		the new encryptor.
	 */
	public ISAACCipher setEncryption(ISAACCipher cipher) {
		return cipher = encryptor;
	}
	
	/**
	 *  Constructs a new {@link ISAACCipher}.
	 * @return
	 * 		the decryptor.
	 */
	public ISAACCipher getDecryption() {
		return getAttributes().get(DECRYPTOR);
	}
	
	/**
	 *  Constructs a setter for the decryptor {@link ISAACCipher}.
	 * @param cipher
	 * 		the variable being set.
	 * @return
	 * 		the new decryptor.
	 */
	public ISAACCipher setDecryption(ISAACCipher cipher) {
		return cipher = decryptor;
	}
	
	/**
	 *  Constructs a public instance for the {@link ChannelHandlerContext}.
	 * @return
	 * 		the instance.
	 */
	public ChannelHandlerContext getContext() {
		return context;
	}


}
