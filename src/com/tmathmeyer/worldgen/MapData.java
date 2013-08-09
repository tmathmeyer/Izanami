package com.tmathmeyer.worldgen;

public class MapData {
	public float min, max;
	public float[][] map;
	public MapData(float min, float max, float[][] map) {
		this.min = min;
		this.max = max;
		this.map = map;
	}
	
	public void unitize(float f) {
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[i].length; j++) {
				map[i][j] = (map[i][j] - min) / (max - min);
				map[i][j] *= f - 40;
			}
		}
	}
}
