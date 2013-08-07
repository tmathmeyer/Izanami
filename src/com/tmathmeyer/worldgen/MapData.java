package com.tmathmeyer.worldgen;

public class MapData {
	public double min, max;
	public double[][] map;
	public MapData(double min, double max, double[][] map) {
		this.min = min;
		this.max = max;
		this.map = map;
	}
}
