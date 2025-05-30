package backend.service;

import backend.algorithms.BoyerMoore;

public class BoyerMooreService {
	public static int[] search(int[] array, int[] pattern) {
		return BoyerMoore.search(array, pattern);
	}
}