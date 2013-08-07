package com.tmathmeyer.globe;

public class GeoPoint {
	public static final float RADIUS = 100.0f;

	private float lat;
	private float lon;
	private float elevation = RADIUS;


	public GeoPoint(float lat, float lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public float getDistance(GeoPoint other) {
		float lat2 = other.getLatitude() * 1f;
		float lon2 = other.getLongitude() * 1f;
		float lat1 = this.getLatitude() * 1f;
		float lon1 = this.getLongitude() * 1f;

		float dLat = toRad(lat2-lat1);
		float dLon = toRad(lon2-lon1);

		lat1 = toRad(lat1);
		lat2 = toRad(lat2);

		float a = (float)((Math.sin(dLat/2) * Math.sin(dLat/2)) + (Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2))); 
		float c = 2.0f * (float)(Math.atan2(Math.sqrt(a), Math.sqrt(1-a))); 
		float d = RADIUS * c;
		return d;
	}

	private float toRad(float degrees) {
		return (float)((degrees * Math.PI) / 180f);
	}

	public float getLatitude() {
		return lat;
	}

	public float getLongitude() {
		return lon;
	}

	public static void main(String[] args) {
		java.util.Scanner reader = new java.util.Scanner(System.in);
		GeoPoint a = new GeoPoint(reader.nextFloat(), reader.nextFloat());
		GeoPoint b = new GeoPoint(reader.nextFloat(), reader.nextFloat());
		System.out.println(b.getDistance(a));
	}
}