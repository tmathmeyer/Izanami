package com.tmathmeyer.voronoi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.tmathmeyer.globe.GeoPoint;

public class VoronoiGlobeGenerator {
	
	
	public void Generate() {
		
		int polyCount = 100;
		Random r = new Random();
		List<VoronoiPoint> polys = new ArrayList<VoronoiPoint>();
		
		for(int i = 0; i < polyCount; i++) {
			polys.add(new VoronoiPoint(getRandomGeoPoint(r)));
		}
		
		
		for(int lat = 0; lat <= 180; lat++){
			for(int lon = -180; lon < 180; lon++) {
				//iterating over evenly spaced points on globe
				VoronoiList vh = new VoronoiList(polyCount, new GeoPoint(lat, lon));
				for(VoronoiPoint vp : polys) {
					vh.add(vp);
				}
				vh.sort();
				vh.addToPoly(10f);
				
			}
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	public static void main(String[] args) {
		new VoronoiGlobeGenerator().Generate();
	}
	
	public GeoPoint getRandomGeoPoint(Random r){
		float lat = r.nextFloat()*360f - 180f;
		float lon = r.nextFloat()*180f;
		return new GeoPoint(lat, lon);
	}
	
	
	
}

class VoronoiList {
	VDP[] vpds;
	GeoPoint center;
	int currentIndex = 0;
	
	public VoronoiList(int i, GeoPoint c) {
		this.vpds = new VDP[i];
		this.center = c;
	}
	
	public void add(VoronoiPoint vp) {
		float distance = center.getDistance(vp.location);
		VDP p = new VDP();
			p.distance = distance;
			p.vp = vp;
		vpds[currentIndex] = p;
		currentIndex++;
	}
	
	public void sort(){
		Arrays.sort(vpds);
	}

	public void printDisances() {
		for(VDP f : vpds){
			System.out.println(f.distance);
		}
	}
	
	public void addToPoly(float margin){
		int depth = 0;
		while(vpds[depth].distance-depth < margin) depth++;
		if (depth > 3){
			VoronoiVertex vv = new VoronoiVertex(center);
			for(int i = 0; i < depth; i++){
				vpds[i].vp.verticies.add(vv);
				vv.polygons.add(vpds[i].vp);
			}
			System.out.println(depth);
		}
	}
}

class VDP implements Comparable<VDP>{
	float distance; 
	VoronoiPoint vp;
	@Override
	public int compareTo(VDP o) {
		return this.distance > o.distance ? 1 : (this.distance < o.distance ? -1 : 0);
	}
}
