package com.tmathmeyer.worldgen;

import java.util.Random;

public class Gen {
	
	int width;
	int height;

	Random seed = new Random();


	public Gen() {

	}


	public double[][] getMap() {
		double[][] map = new double[height][width];
		
		return map;
	}





	public static MapData ds(int size) {
		final int DATA_SIZE = size;
		final double SEED = 1000.0;
		
		double h = 500.0;
		double min = h;
		double max = h;
		
		double[][] data = new double[DATA_SIZE][DATA_SIZE];
		data[0][0] = data[0][DATA_SIZE-1] = data[DATA_SIZE-1][0] = data[DATA_SIZE-1][DATA_SIZE-1] = SEED;
		
		Random r = new Random();
		for(int sideLength = DATA_SIZE-1;
			sideLength >= 2;
			sideLength /=2, h/= 2.0){
			int halfSide = sideLength/2;
			for(int x=0;x<DATA_SIZE-1;x+=sideLength){
				for(int y=0;y<DATA_SIZE-1;y+=sideLength){
					double avg = data[x][y] +
					data[x+sideLength][y] +
					data[x][y+sideLength] +
					data[x+sideLength][y+sideLength];
					avg /= 4.0;
					double temp = avg + (r.nextDouble()*2*h) - h;
					data[x+halfSide][y+halfSide] = temp;
					if (temp > max)max=temp;
					if (temp < min)min=temp;
				}
			}

			for(int x=0;x<DATA_SIZE-1;x+=halfSide){
				for(int y=(x+halfSide)%sideLength;y<DATA_SIZE-1;y+=sideLength){
					double avg = 
					        data[(x-halfSide+DATA_SIZE-1)%(DATA_SIZE-1)][y] +
					        data[(x+halfSide)%(DATA_SIZE-1)][y] +
					        data[x][(y+halfSide)%(DATA_SIZE-1)] +
					        data[x][(y-halfSide+DATA_SIZE-1)%(DATA_SIZE-1)];
					avg /= 4.0;
					double temp = avg + (r.nextDouble()*2*h) - h;
					data[x][y] = temp;
					if (temp > max)max=temp;
					if (temp < min)min=temp;
					
					if(x == 0)	data[DATA_SIZE-1][y] = temp;
					if(y == 0)	data[x][DATA_SIZE-1] = temp;
				}
			}
		}

		return new MapData(min, max, data);
	}
}