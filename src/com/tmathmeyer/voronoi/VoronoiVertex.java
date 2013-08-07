package com.tmathmeyer.voronoi;

import java.util.ArrayList;
import java.util.List;

import com.tmathmeyer.globe.GeoPoint;

public class VoronoiVertex {
	GeoPoint location;
	
	List<VoronoiPoint> polygons = new ArrayList<VoronoiPoint>();
	
	public VoronoiVertex(GeoPoint gp) {
		this.location = gp;
	}
}
