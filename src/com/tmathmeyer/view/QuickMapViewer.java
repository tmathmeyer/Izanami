package com.tmathmeyer.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.tmathmeyer.worldgen.Gen;
import com.tmathmeyer.worldgen.MapData;

public class QuickMapViewer extends JPanel{

	public static void main(String[] args) {
		new QuickMapViewer();
	}
	
	MapData map;
	int zoomLevel = 4;


	public QuickMapViewer() {
		super();
		int size = 1024;
		JFrame frame = new JFrame();
		map = Gen.ds(size*zoomLevel+1);
		
		
		this.setPreferredSize(new Dimension(size, size));
		frame.setLayout(new GridLayout(1,1));
		frame.add(this);
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	public void paintComponent(Graphics g) {
		for(int i = 0; i < map.map.length; i+= zoomLevel) {
			for(int j = 0; j < map.map[i].length; j+=zoomLevel){
				g.setColor(colorize(map.min, map.max, map.map[i][j]));
				g.drawRect(j/zoomLevel, i/zoomLevel, 1, 1);
			}
		}
	}
	
	public Color colorize(double min, double max, double val) {
		int b = 0;
		try {
			double mid = 4 * (min/7 + max/7);
			if (val < mid) {
				int g = (int)((val-min) * 150 / (max-min));
				b = (int)((val-min) * 254 / (max-min));
				return new Color(0, g, b);
			}
			int k = (int)((val-min) * 254 / (max-min));
			return new Color(k,k,k);
		}
		catch(Exception e) {
			System.out.println(b);
			return new Color(255,0,0);
		}
		
	}
}