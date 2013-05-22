package de.raulin.rosario.astar.help;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class Stack<T> implements Iterable<T> {

	private class Node<E> {
		private final E data;
		private final Node<E> next;
		
		public Node(final Node<E> _next, final E _data) {
			this.data = _data;
			this.next = _next;
		}
		
		public E data() {
			return data;
		}
		
		public Node<E> next() {
			return next;
		}
	}
	
	private Node<T> head = null;
	
	public void add(final T element) {
		final Node<T> next = new Node<T>(head, element);
		this.head = next;
	}
	
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {

			private Node<T> next = head;
			
			@Override
			public boolean hasNext() {
				return next != null;
			}

			@Override
			public T next() {
				if (hasNext()) {
					final T e = next.data();
					next = next.next();
					return e;
				}
				throw new NoSuchElementException();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
			
		};
	}
	
	public static void main(String[] args) {
		Stack<Integer> s = new Stack<Integer>();
		for (int i = 0; i < 10; ++i) {
			s.add(i);
		}
		for (final Integer i : s) {
			System.out.println(i);
		}
	}

}
