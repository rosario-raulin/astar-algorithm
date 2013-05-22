package de.raulin.rosario.astar.algorithm;

import de.raulin.rosario.astar.map.Field;

public interface PathFinder {
	public Iterable<Field> findShortestPath(final Field from);
}
