package de.raulin.rosario.astar.algorithm;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

import de.raulin.rosario.astar.map.Field;
import de.raulin.rosario.astar.map.GameMap;

public final class AStarAlgorithm implements PathFinder {

	private final GameMap map;
	private Map<Field, Integer> costs;
	private final Map<Field, Integer> fValues;

	public AStarAlgorithm(final GameMap map) {
		this.map = map;
		this.fValues = new HashMap<Field,Integer>();
	}

	private void initCosts(final Field from) {
		this.fValues.clear();
		
		int dimension = map.getDimension();
		costs = new HashMap<Field, Integer>();
		
		for (int i = 0; i < dimension; ++i) {
			for (int j = 0; j < dimension; ++j) {
				costs.put(map.get(i, j), Integer.MAX_VALUE);
			}
		}
		costs.put(from, 0);
	}

	private int h(final Field to) {
		final Field goal = map.getGoal();
		return Math.abs(goal.getX() - to.getX()) + Math.abs(goal.getY() - to.getY());
	}
	
	private void expandNode(final Field curr,
			final PriorityQueue<Field> openList, final Set<Field> closedList,
			final Map<Field, Field> preds) {
		for (final Field succ : map.getNeighbours(curr)) {
			if (!closedList.contains(succ)) {
				int newCosts = costs.get(curr) + map.getCosts(succ);

				if (!openList.contains(succ) || newCosts < costs.get(succ)) {
					preds.put(succ, curr);
					costs.put(succ, newCosts);
					fValues.put(succ, newCosts + h(succ));
					
					if (openList.contains(succ)) {
						openList.remove(succ);
					}
					openList.add(succ);
				}
			}
		}
	}
	
	public Iterable<Field> backtrack(final Map<Field, Field> preds) {
		final Stack<Field> s = new Stack<Field>();
		
		Field curr = map.getGoal();
		s.push(curr);
		while (preds.get(curr) != null) {
			curr = preds.get(curr);
			s.push(curr);
		}
		
		return s;
	}

	@Override
	public Iterable<Field> findShortestPath(final Field from) {
		PriorityQueue<Field> openList = new PriorityQueue<Field>(map.getDimension(), new Comparator<Field>() {

			@Override
			public int compare(Field o1, Field o2) {
				return fValues.get(o1) - fValues.get(o2);
			}
		});
		Set<Field> closedList = new HashSet<Field>();
		Map<Field, Field> preds = new HashMap<Field,Field>();

		preds.put(from, null);
		initCosts(from);
		openList.add(from);

		do {
			final Field curr = openList.poll();
			if (curr == map.getGoal()) {
				return backtrack(preds);
			}
			closedList.add(curr);
			expandNode(curr, openList, closedList, preds);
		} while (!openList.isEmpty());
		return null; // TODO: better error handling
	}
}
