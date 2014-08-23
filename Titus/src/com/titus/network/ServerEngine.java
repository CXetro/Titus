package com.titus.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;
import java.util.logging.Logger;

import com.titus.network.codecs.RS2ProtocolConnectionDecoder;
import com.titus.network.codecs.RS2ProtocolEncoder;

/**
 *  Reactor for the main thread.
 * 
 * @author Braeden Dempsey
 * @author Stuart Murphy
 *
 */

public class ServerEngine implements Runnable {
	
	/** Creates the {@link ServerEngine} singleton. */
	private static ServerEngine singleton;
	
	/** Used to output server information. */
	private static Logger logger = Logger.getLogger(ServerEngine.class.getSimpleName());
	
	/** Boss {@link EventLoopGroup}. */
	private static EventLoopGroup bossGroup = new NioEventLoopGroup();

	/** Worker {@link EventLoopGroup}.*/
	private static EventLoopGroup workerGroup = new NioEventLoopGroup();
	
	private InetSocketAddress address;
	private String host;
	private int port, cycle;
	private byte world;
	
	/**
	 *  Constructs a new {@link ServerEngine}.
	 * @param host 
	 * 		the address allocated to the server.
	 * @param port
	 * 		the port bound to the server.
	 * @param cycle
	 * 		the rate in which the server cycles.
	 * @param world
	 * 		the world byte of the server.
	 */
	public ServerEngine(final String host, final int port, final int cycle, final byte world) {
		this.host = host;
		this.port = port;
		this.cycle = cycle;
		this.world = world;
	}
	

	@Override
	public void run() {
		
		try {
			
		address = new InetSocketAddress(host, port);
		logger.info("Starting Titius...");
		
		initialize();
		logger.info("Titus is online!");
		
		while (true) {
			process();
		}
			
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
	}
	
	private void initialize() {
		try {
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(bossGroup, workerGroup)
		.channel(NioServerSocketChannel.class)
		.handler(new LoggingHandler(LogLevel.INFO))
		.childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel channel) throws Exception {
				ChannelPipeline pipeline = channel.pipeline();
				pipeline.addLast("rs2-encoder", new RS2ProtocolEncoder());
				pipeline.addLast("rs2-connection-decoder", new RS2ProtocolConnectionDecoder());
			}
			
		});
		
		ChannelFuture future = bootstrap.bind(port);
		future.sync();
		Channel channel = future.channel();
		channel.closeFuture();
		future.sync();
		
		} catch (Exception e) {
			
		}
		
	}
	
	/**
	 *  Handles all cycled processes.
	 */
	private void process() {
		try {
			// TODO: Player Updating
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 *  Creates a public instance of the {@link ServerEngine} singleton.
	 * @return
	 * 		the singleton.
	 */
	public static ServerEngine getSingleton() {
		return singleton;
	}

	/**
	 *  Creates a public set instance of the {@link ServerEngine} singleton.
	 * @param singleton
	 * 		the new singleton.
	 */
	public static void setSingleton(ServerEngine singleton) {
		if (singleton == null)
			throw new IllegalStateException("[ERROR] Singleton has not been written.");
		ServerEngine.singleton = singleton;
	}

}
