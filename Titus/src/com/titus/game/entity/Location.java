package com.titus.game.entity;

/**
 * 
 * @author Braeden Dempsey
 *
 */

public class Location {
	
	private int x, y, z;
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
	
	public int getRegionX() {
		return (x >> 3) - 6;
	}
	
	public int getRegionY() {
		return (y >> 3) - 6;
	}
	
	public void setLocation(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setLocation(Location location) {
		setLocation(location.getX(), location.getY(), location.getZ());
	}
	
	public Location(final int x, final int y, final int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

}
