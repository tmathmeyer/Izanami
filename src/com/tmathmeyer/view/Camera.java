package com.tmathmeyer.view;

public class Camera {
	Globe globe;
	float cameraRadius;
	GeoPoint location;
	
	public Camera(Globe globe, float radius, GeoPoint loc) {
		this.globe = globe;
		this.cameraRadius = radius;
		this.location = loc;
	}
}
