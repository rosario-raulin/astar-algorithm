package de.raulin.rosario.astar;

import java.util.Random;

public final class Map {
	private final static Random RANDGEN = new Random();

	private final Field[][] data;

	public Map(final int dimension) {
		this.data = new Field[dimension][dimension];
		fillWithRandomData();
	}
	
	private void fillWithRandomData() {
		for (int i = 0; i < data.length; ++i) {
			for (int j = 0; j < data.length; ++j) {
				int nextType = RANDGEN.nextInt(5);
				
				FieldType type = FieldType.FREE;
				switch (nextType) {
				case 0:
				case 1:
				case 2:
					type = FieldType.FREE;
					break;
				case 3:
					type = FieldType.FLOWER;
					break;
				case 4:
					type = FieldType.STONE;
					break;
				}
				
				data[i][j] = new Field(type, i, j);
			}
		}
	}
	
	public Field get(final int x, final int y) {
		return data[x][y];
	}
	
	@Override
	public String toString() {
		final StringBuilder out = new StringBuilder();
		
		for (int i = 0; i < data.length; ++i) {
			for (int j = 0; j < data.length; ++j) {
				out.append(data[i][j].toString());
				out.append(" ");
			}
			out.append("\n");
		}
		
		return out.toString();
	}
	
	public static void main(String[] args) {
		final Map m = new Map(10);
		System.out.println(m);
	}
}
