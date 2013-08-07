package com.tmathmeyer.voronoi;

import java.util.ArrayList;
import java.util.List;

import com.tmathmeyer.globe.GeoPoint;

public class VoronoiPoint {
	GeoPoint location;
	List<VoronoiVertex> verticies = new ArrayList<VoronoiVertex>();
	
	public VoronoiPoint(GeoPoint p) {
		this.location = p;
	}
	
	
}
