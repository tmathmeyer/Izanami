package com.tmathmeyer.view;

import java.awt.Color;

public class Globe {
	float radius;
	//VoronoiPointSet surface;

	static GeoPoint getSurfacePoint(Camera camera, float heightOffSet, float widthOffSet) {
		float A = widthOffSet;
		float B = heightOffSet;
		float Z = camera.cameraRadius - camera.globe.radius;
		float R = camera.globe.radius;

		float lat = (float) (Math.atan((Math.tan(B)*Math.tan(A)*Z) / (R * Math.cos(A))));
		float lon = (float) (Math.acos((Math.tan(A) * Z) / R));

		while(lon > 180)
			lon -= 360;
		while(lon < -180)
			lon += 360;

		return new GeoPoint(lat, lon);
	}

	Color getLatColor(float lat, float lon) {
		int r = (int) (lat*255);
		r /= 180;

		int g = (int) ((lon+180) * 255);
		g /= 360;

		return new Color(r,g,0);
	}
	
	public Globe(float r){
		this.radius = r;
	}
}