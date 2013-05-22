package de.raulin.rosario.astar.algorithm;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import de.raulin.rosario.astar.help.Stack;
import de.raulin.rosario.astar.map.Field;
import de.raulin.rosario.astar.map.GameMap;

public final class AStarAlgorithm implements PathFinder {

	private final GameMap map;
	private Map<Field, Integer> costs;
	private final Map<Field, Integer> fValues;

	private final Comparator<Field> FIELD_COMPARTOR = new Comparator<Field>() {
		// This comparator is used for our priority queue:
		// The order depends on f(x) where x is a field.
		//
		// f(x) = g(x) + h(x) where
		// g(x) = costs to x
		// h(x) = heuristic for x (in this case: Euclidean distance)
		//
		// f(x) is stored fValues (it maps a Field to an Integer).
		@Override
		public int compare(Field o1, Field o2) {
			return fValues.get(o1) - fValues.get(o2);
		}
	};

	public AStarAlgorithm(final GameMap map) {
		this.map = map;
		this.fValues = new HashMap<Field, Integer>();
	}

	private void initCosts(final Field from) {
		// In this method we set every distance (i. e. fValue and cost)
		// to INFINITY (= Integer.MAX_VALUE) except for our "from" Field.
		
		// First step: Reset everything!
		this.fValues.clear();
		costs = new HashMap<Field, Integer>();

		// Second step: Set every distance to INFINITY!
		for (int i = 0; i < map.getDimension(); ++i) {
			for (int j = 0; j < map.getDimension(); ++j) {
				costs.put(map.get(i, j), Integer.MAX_VALUE);
				fValues.put(map.get(i,j), Integer.MAX_VALUE);
			}
		}
		// Last step: Set distance from "from" to 0!
		costs.put(from, 0);
		fValues.put(from, 0);
	}

	private int h(final Field to) {
		// Unsere Heuristik:
		// Euklidischer Abstand, siehe:
		// http://de.wikipedia.org/wiki/Euklidischer_Abstand
		final Field goal = map.getGoal();
		final int diff = (int) Math.round(Math.sqrt(Math.pow(
				goal.getX() - to.getX(), 2)
				+ Math.pow(goal.getY() - to.getY(), 2)));

		return diff;
	}

	private void expandNode(final Field curr,
			final PriorityQueue<Field> openList, final Set<Field> closedList,
			final Map<Field, Field> preds) {
		for (final Field succ : map.getNeighbours(curr)) {
			if (!closedList.contains(succ)) { // we don't know the shortest path of this successor
				int newCosts = costs.get(curr) + map.getCosts(succ); // the total cost to get from "from" to succ

				if (!openList.contains(succ) || newCosts < costs.get(succ)) { // if we don't know a way to succ OR found a better one
					preds.put(succ, curr); // set succ's predecessor to curr
					costs.put(succ, newCosts); // update succ's costs
					fValues.put(succ, newCosts + h(succ)); // update f(succ)

					if (openList.contains(succ)) { // if we have an old way
						openList.remove(succ); // remove it
					}
					openList.add(succ); // add the new way
				}
			}
		}
	}

	public Iterable<Field> backtrack(final Map<Field, Field> preds) {
		// We use a strategy called backtracking here:
		// preds stores the predecessor of every Field in our shortest path.
		//
		// We start at our goal and track back the path to our starting point.
		// Putting this on a Stack gives us the right order.
		
		final Stack<Field> s = new Stack<Field>();

		Field curr = map.getGoal();
		s.add(curr);
		while (preds.get(curr) != null) {
			curr = preds.get(curr);
			s.add(curr);
		}

		return s;
	}

	@Override
	public Iterable<Field> findShortestPath(final Field from) {
		// The open list contains Fields we still need to find the shortest path for.
		final PriorityQueue<Field> openList = new PriorityQueue<Field>(
				map.getDimension(), FIELD_COMPARTOR);
		// The closed list contains those Fields for which we already know the shortest path.
		final Set<Field> closedList = new HashSet<Field>();
		// We need to store predecessors, i. e. those fields that lead to a shortest path.
		final Map<Field, Field> preds = new HashMap<Field, Field>();

		preds.put(from, null); // Obviously our starting point does not have a predecessor.
		initCosts(from); // see above
		openList.add(from); // We still need to edit our starting point.

		do {
			final Field curr = openList.poll(); // get the most promising Field
			if (curr == map.getGoal()) { // if that's our goal, we're done
				return backtrack(preds); // see above
			}
			closedList.add(curr); // we found the shortest path to curr
			expandNode(curr, openList, closedList, preds); // update distances, etc., see above
		} while (!openList.isEmpty());
		
		return null; // TODO: better error handling (null means there is no path)
	}
}
