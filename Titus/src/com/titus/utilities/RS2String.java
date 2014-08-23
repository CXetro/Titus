package com.titus.utilities;

import io.netty.buffer.ByteBuf;

/**
 * 
 * @author RandQm
 *
 */

public class RS2String {
	
	
	/**
	 * Builds a runescape 2 string.
	 * 
	 * @param buf The byte buffer.
	 * 
	 * @return The built string.
	 */
	public static String buildRS2String(final ByteBuf buffer) {
		final StringBuilder bldr = new StringBuilder();
		byte b;
		
		while (buffer.isReadable() && (b = buffer.readByte()) != 10)
			bldr.append((char) b);
		return bldr.toString();
	}
	
	/**
	 * Formats a runescape 2 string.
	 * 
	 * @param rs2String The runescape 2 string.
	 * 
	 * @return The formatted string.
	 */
	public static String formatPlayerName(String rs2String) {
		for (int i = 0; i < rs2String.length(); i++) {
			if (i == 0)
				rs2String = String.format("%s%s", Character.toUpperCase(
						rs2String.charAt(0)), rs2String.substring(1));
			
			if (!Character.isLetterOrDigit(rs2String.charAt(i))) {
				if (i + 1 < rs2String.length()) {
					rs2String = String.format("%s%s%s", rs2String.subSequence(0, i + 1),
							Character.toUpperCase(rs2String.charAt(i + 1)),
							rs2String.substring(i + 2));
				}
			}
		}
		return rs2String.replace("_", " ");
	}

}