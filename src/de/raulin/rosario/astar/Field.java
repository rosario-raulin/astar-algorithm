package de.raulin.rosario.astar;

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
			return "S";
		case FREE:
			return "_";
		case FLOWER:
			return "~";
		}
		throw new RuntimeException("Field type unknown");
	}
}
