package de.raulin.rosario.astar.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.raulin.rosario.astar.algorithm.AStarAlgorithm;
import de.raulin.rosario.astar.algorithm.PathFinder;

public final class GameMap {
	private final static Random RANDGEN = new Random();

	private final Field[][] data;
	private Field goal;

	public GameMap(final int dimension) {
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
	
	public int getDimension() {
		return data.length;
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
		if (x - 1 >= 0) {
			neighbours.add(data[x-1][y]);
		}
		if (y + 1 < data.length) {
			neighbours.add(data[x][y+1]);
		}
		if (y - 1 >= 0) {
			neighbours.add(data[x][y-1]);
		}
		
		return filter(neighbours);
	}
	
	public Iterable<Field> getNeighbours(final Field f) {
		return getNeighbours(f.getX(), f.getY());
	}
	
	public int getCosts(final Field to) {
		switch (to.getType()) {
		case FREE:
		case GOAL:
			return 1;
		case STONE:
			return 100;
		case FLOWER:
			return 5;
		}
		throw new RuntimeException("unknown fild type");
	}
	
	@Override
	public String toString() {
		final StringBuilder out = new StringBuilder();
		
		for (int i = 0; i < data.length; ++i) {
			out.append(i);
			out.append(" ");
		}
		
		out.append("\n\n");
		
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
		final GameMap m = new GameMap(10);
		System.out.println(m);
		
//		PathFinder pf = new AStarAlgorithm(m);
//		final Iterable<Field> path = pf.findShortestPath(m.get(0, 0));
//		
//		if (path == null) {
//			System.out.println("no path found");
//		} else {
//			System.out.println("Path found:");
//			for (final Field f : path) {
//				System.out.println(f.getX()+", "+f.getY());
//			}
//		}
	}
}
