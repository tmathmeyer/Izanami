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
		final float SEED = 0.0f;
		
		float h = 500.0f;
		float min = h;
		float max = h;
		
		float[][] data = new float[DATA_SIZE][DATA_SIZE];
		for(int i = 0; i < DATA_SIZE; i++) {
			data[0][i] = SEED;
			data[i][0] = SEED;
			data[DATA_SIZE-1][i] = SEED;
			data[i][DATA_SIZE-1] = SEED;
		}
		
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
					float temp = (float) (avg + (r.nextDouble()*2*h) - h);
					if (data[x+halfSide][y+halfSide] == 0)data[x+halfSide][y+halfSide] = (float) temp;
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
					float temp = (float) (avg + (r.nextDouble()*2*h) - h);
					data[x][y] = (float) temp;
					if (temp > max)max=temp;
					if (temp < min)min=temp;
					
					if(x == 0)	data[DATA_SIZE-1][y] = (float) temp;
					if(y == 0)	data[x][DATA_SIZE-1] = (float) temp;
				}
			}
		}

		return new MapData(min, max, data);
	}
}