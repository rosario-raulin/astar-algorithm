package de.raulin.rosario.astar.map;

public class Field {
	private final FieldType type;
	private final int x;
	private final int y;
	
	public Field(final FieldType type, int x, int y) {
		this.type = type;
		this.x = x;
		this.y = y;
	}
	
	public FieldType getType() {
		return type;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	@Override
	public String toString() {
		switch (type) {
		case STONE:
			return "X";
		case FREE:
			return "_";
		case FLOWER:
			return "~";
		case GOAL:
			return "G";
		}
		throw new RuntimeException("Field type unknown");
	}
}
