package com.tmathmeyer.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.tmathmeyer.worldgen.Gen;

public class RayTraceMapViewer extends JPanel{
	int viewWidthPX;
	int viewHeightPX;

	float viewWidthAngle;
	float viewHeightAngle;

	float raytraceDensity;

	Globe globe;
	Camera cam;
	
	
	
	
	public RayTraceMapViewer() {
		super();
		JFrame frame = new JFrame();
		
		globe = new Globe(10);
		cam = new Camera(globe, 30, new GeoPoint(0, 90));
		
		this.setPreferredSize(new Dimension(500, 500));
		frame.setLayout(new GridLayout(1,1));
		frame.add(this);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	public void paintComponent(Graphics g) {
		pixel k = new Pixel
	}
	
}