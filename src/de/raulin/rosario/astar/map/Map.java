package de.raulin.rosario.astar.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Map {
	private final static Random RANDGEN = new Random();

	private final Field[][] data;
	private Field goal;

	public Map(final int dimension) {
		this.data = new Field[dimension][dimension];
		fillWithRandomData();
		setGoal();
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
	
	private void setGoal() {
		final int x = RANDGEN.nextInt(data.length);
		final int y = RANDGEN.nextInt(data.length);
		
		this.goal = new Field(FieldType.GOAL, x, y);
		data[x][y] = this.goal;
	}
	
	public Field get(final int x, final int y) {
		return data[x][y];
	}
	
	public Field getGoal() {
		return goal;
	}
	
	private Iterable<Field> filter(final List<Field> fields) {
		// TODO: Given a list of fields, filter out stones!
		final List<Field> filtered = new ArrayList<Field>(fields.size());
		
		for (final Field f : fields) {
			if (f.getType() != FieldType.STONE) {
				filtered.add(f);
			}
		}
		
		return filtered;
	}
	
	public Iterable<Field> getNeighbours(final int x, final int y) {
		final List<Field> neighbours = new ArrayList<Field>();
		
		if (x + 1 < data.length) {
			neighbours.add(data[x+1][y]);
		}
		if (x - 1 > 0) {
			neighbours.add(data[x-1][y]);
		}
		if (y + 1 < data.length) {
			neighbours.add(data[x][y+1]);
		}
		if (y - 1 > 0) {
			neighbours.add(data[x][y-1]);
		}
		
		return filter(neighbours);
	}
	
	public Iterable<Field> getNeighbours(final Field f) {
		return getNeighbours(f.getX(), f.getY());
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
		
		for (final Field f : m.getNeighbours(m.getGoal())) {
			System.out.println(f);
		}
	}
}
