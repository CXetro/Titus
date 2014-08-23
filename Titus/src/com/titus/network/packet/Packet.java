package com.titus.network.packet;

import com.titus.network.crypto.ISAACCipher;

import io.netty.buffer.ByteBuf;

/**
 * 
 * @author RandQm
 *
 */

public class Packet {
	
	/**
	 * The opcode of the packet.
	 */
	private int opcode;
	
	/**
	 * The bit position in the buffer.
	 */
	private int bitPosition = 0;
	
	/**
	 * The length position in the buffer.
	 */
	private int lengthPosition = 0;
	
	/**
	 * The bit masks.
	 */
	private static final int[] BIT_MASK_INDEX = {
		0, 1, 3, 7, 15, 31, 63, 127, 255, 511,
		1023, 2047, 4095, 8191, 16383, 32767, 65535, 131071, 262143,
		524287, 1048575, 2097151, 4194303, 8388607, 16777215, 33554431,
		67108863, 134217727, 268435455, 536870911, 1073741823, 2147483647, -1
	};
	
	/**
	 * The packet's buffer.
	 */
	private ByteBuf buffer;
	
	
	/**
	 * The constructor.
	 * 
	 * @param opcode The packet's opcode.
	 */
	public Packet(int opcode, ByteBuf buffer) {
		this.opcode = opcode;
		this.buffer = buffer;
	}
	
	/**
	 * The constructor.
	 * 
	 * @param opcode The opcode of the packet.
	 * 
	 * @param buffer The buffer of the packet.
	 */
	public Packet(ByteBuf buffer) {
		this.buffer = buffer;
	}
	
	/**
	 * Writes a standard packet header.
	 * 
	 * @param cryptic The used cryptic.
	 * 
	 * @param opcode The packet opcode.
	 */
	public void writeHeader(ISAACCipher cryptic, int opcode) {
		writeHeader(PacketHeaderTypes.FIXED, cryptic, opcode);
	}
	
	/**
	 * Writes a packet header.
	 * 
	 * @param header The header type.
	 * 
	 * @param cryptic The used cryptic.
	 * 
	 * @param opcode The packet opcode.
	 */
	public void writeHeader(PacketHeaderTypes header, ISAACCipher cryptic, int opcode) {
		buffer.writeByte(opcode + cryptic.getNextValue());
		this.lengthPosition = buffer.writerIndex();

		switch (header) {
		case FIXED:
			break;
			
		case VARIABLE_BYTE:
			buffer.writeByte(0);
			break;
			
		case VARIABLE_SHORT:
			buffer.writeShort(0);
			break;
		}
	}
	
	/**
	 * Finishes the standard packet.
	 */
	public void finishPacket() {
		finishPacket(PacketHeaderTypes.FIXED);
	}
	
	/**
	 * Finishes the packet.
	 * 
	 * @param header The packet header type.
	 */
	public void finishPacket(PacketHeaderTypes header) {
		switch (header) {
		case FIXED:
			break;
			
		case VARIABLE_BYTE:
			buffer.setByte(lengthPosition, (buffer.writerIndex() - lengthPosition - 1));
			break;
			
		case VARIABLE_SHORT:
			buffer.setShort(lengthPosition, (buffer.writerIndex() - lengthPosition - 2));
			break;
		}
	}
	
	/**
	 * Initiates the bits access.
	 */
	public void initiateBitAccess() {
		bitPosition = (buffer.writerIndex() * 8);
	}

	/**
	 * Initiates the bytes access.
	 */
	public void initiateByteAccess() {
		buffer.writerIndex((bitPosition + 7) / 8);
	}
	
	/**
	 * Writes a standard byte.
	 * 
	 * @param payload The payload.
	 */
	public void writeByte(int payload) {
		buffer.writeByte(payload);
	}
	
	/**
	 * Reads a standard byte.
	 * 
	 * @return The read byte.
	 */
	public byte readByte() {
		return buffer.readByte();
	}
	
	/**
	 * Reads multiple bytes.
	 * 
	 * @param bytes The bytes.
	 */
	public void readBytes(byte[] bytes) {
		buffer.readBytes(bytes);
	}
	
	/**
	 * Reads a series of bytes.
	 * 
	 * @param bytes The bytes.
	 * 
	 * @param start Where to start reading.
	 * 
	 * @param length The length to read.
	 */
	public void readBytesSerie(byte[] bytes, int start, int length) {
		for (int i = 0; i < length; i++)
			bytes[start + i] = readByte();
	}
	
	/**
	 * Reads an additional byte.
	 * 
	 * @return The read byte.
	 */
	public byte readAdditionalByte() {
		return (byte) (readByte() - 128);
	}

	/**
	 * Reads a negated byte.
	 * 
	 * @return The read byte.
	 */
	public byte readNegatedByte() {
		return (byte) (-readByte());
	}

	/**
	 * Reads a subtrahend byte.
	 * 
	 * @return The read byte.
	 */
	public byte readSubtrahendByte() {
		return (byte) (128 - readByte());
	}
	
	/**
	 * Reads an integer.
	 * 
	 * @return The read integer.
	 */
	public int getInteger() {
		return buffer.readInt();
	}
	
	/**
	 * Writes a subtrahend byte.
	 * 
	 * @param payload the payload.
	 */
	public void writeSubtrahendByte(int payload) {
		buffer.writeByte(128 - payload);
	}
	
	/**
	 * Writes a negated byte.
	 * 
	 * @param payload the payload.
	 */
	public void writeNegatedByte(int payload) {
		buffer.writeByte(-payload);
	}
	
	/**
	 * Writes a standard short.
	 * 
	 * @param payload The payload.
	 */
	public void writeShort(int payload) {
		buffer.writeShort(payload);
	}
	
	/**
	 * Writes an additional short.
	 * 
	 * @param payload The payload.
	 */
	public void writeAdditionalShort(int payload) {
		buffer.writeByte(payload >> 8);
		buffer.writeByte(payload + 128);
	}
	
	/**
	 * Writes a little endian short.
	 * 
	 * @param payload The payload.
	 */
	public void writeLEShort(int payload) {
		buffer.writeByte(payload);
		buffer.writeByte(payload >> 8);
	}
	
	/**
	 * Writes a little endian additional short.
	 * 
	 * @param payload The payload.
	 */
	public void writeLEAShort(int payload) {
		buffer.writeByte(payload + 128);
		buffer.writeByte(payload >> 8);
	}
	
	/**
	 * Reads a little endian short.
	 * 
	 * @return The read short.
	 */
	public short getLEShort() {
		int i = (readByte() & 0xFF) | ((readByte() & 0xFF) << 8);
		
		if (i > 32767)
			i -= 0x10000;
		return (short) i;
	}

	/**
	 * Reads a little endian additional short.
	 * 
	 * @return The read short.
	 */
	public short getLEAShort() {
		int i = (readByte() - 128 & 0xFF) | ((readByte() & 0xFF) << 8);
		
		if (i > 32767)
			i -= 0x10000;
		return (short) i;
	}
	
	/**
	 * Reads a long.
	 * 
	 * @return The read long.
	 */
	public long readLong() {
		return buffer.readLong();
	}
	
	/**
	 * Reads reversed bytes series.
	 * 
	 * @param bytes The bytes.
	 * 
	 * @param start Where to start reading.
	 * 
	 * @param length The length to read.
	 */
	public void readReversedBytes(byte[] bytes, int start, int length) {
		for (int i = (start + length - 1); i >= start; i--)
			bytes[i] = readByte();
	}
	
	/**
	 * Reads reversed additional bytes series.
	 * 
	 * @param bytes The bytes.
	 * 
	 * @param start Where to start reading.
	 * 
	 * @param length The length to read.
	 */
	public void readReversedAdditionalBytes(byte[] bytes, int start, int length) {
		for (int i = (start + length - 1); i >= start; i--)
			bytes[i] = readAdditionalByte();
	}
	
	/**
	 * Reads a standard short.
	 * 
	 * @return The read short.
	 */
	public short readShort() {
		return buffer.readShort();
	}
	
	/**
	 * Reads an additional short.
	 * 
	 * @return The read short.
	 */
	public short readAdditionalShort() {
		int i = ((readByte() & 0xFF) << 8) | (readByte() - 128 & 0xFF);
		
		if (i > 32767)
			i -= 0x10000;
		return (short) i;
	}
	
	/**
	 * Reads a signed smart.
	 * 
	 * @return The read smart.
	 */
	public int getSignedSmart() {
		final int peek = buffer.getByte(buffer.readerIndex());
		
		if (peek < 128)
			return ((readByte() & 0xFF) - 64);
		else
			return ((readShort() & 0xFFFF) - 49152);
	}
	
	/**
	 * Reads a standard smart.
	 * 
	 * @return The read smart.
	 */
	public int getSmart() {
		final int peek = buffer.getByte(buffer.readerIndex());
		
		if (peek < 128)
			return (readByte() & 0xFF);
		else
			return (readShort() & 0xFFFF) - 32768;
	}
	
	/**
	 * Reads an unsigned byte.
	 * 
	 * @return The read byte.
	 */
	public int readUnsignedByte() {
		return buffer.readByte() & 0xff;
	}
	
	/**
	 * Reads an unsigned short.
	 * 
	 * @return The read short.
	 */
	public int getUnsignedShort() {
		int value = 0;
		value |= (readByte() & 0xff) << 8;
		value |= (readByte() & 0xff);
		return value;
	}

	/**
	 * Reads an unsigned additional short.
	 * 
	 * @return The read short.
	 */
	public int getUnsignedAdditionalShort() {
		int value = 0;
		value |= (readByte() & 0xff) << 8;
		value |= ((readByte() - 128) & 0xff);
		return value;
	}
	
	/**
	 * Writes bits to the buffer.
	 * 
	 * @param amount The amount of bits.
	 * 
	 * @param value The value of the bits.
	 */
	public void writeBits(int amount, int value) {
		int bytePosition = bitPosition >> 3;
		int bitOffset = 8 - (bitPosition & 7);
		int requiredSpace = bytePosition - buffer.writerIndex() + 1;
		
		this.bitPosition += amount;
		requiredSpace += (amount + 7) / 8;
		buffer.ensureWritable(requiredSpace);

		for (; amount > bitOffset; bitOffset = 8) {
			int allocation = buffer.getByte(bytePosition);
			allocation &= ~BIT_MASK_INDEX[bitOffset];
			allocation |= value >> amount - bitOffset & BIT_MASK_INDEX[bitOffset];
			buffer.setByte(bytePosition++, allocation);
			amount -= bitOffset;
		}
		if (amount == bitOffset) {
			int allocation = buffer.getByte(bytePosition);
			allocation &= ~BIT_MASK_INDEX[bitOffset];
			allocation |= value & BIT_MASK_INDEX[bitOffset];
			buffer.setByte(bytePosition, allocation);
		} else {
			int allocation = buffer.getByte(bytePosition);
			allocation &= ~(BIT_MASK_INDEX[amount] << bitOffset - amount);
			allocation |= (value & BIT_MASK_INDEX[amount]) << bitOffset - amount;
			buffer.setByte(bytePosition, allocation);
		}
	}
	
	
	/**
	 * Retrieves the buffer length.
	 * 
	 * @return The length.
	 */
	public int getLength() {
		return buffer.capacity();
	}
	
	/**
	 * Retrieves the packet's opcode.
	 * 
	 * @return The opcode.
	 */
	public int getOpcode() {
		return opcode;
	}
	
	/**
	 * Retrieves the packet's buffer.
	 * 
	 * @return The packet's buffer.
	 */
	public ByteBuf getBuffer() {
		return buffer;
	}

}
