package backend.service;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import backend.Handler.dto.SortRequest;
import backend.algorithms.HeapSort; // ваш класс с sortWithAnalysis()
import backend.algorithms.HeapSort.SortResult;

public class HeapSortService {

	/**
	 * Принимает DTO с полями data и structure,
	 * читает в массив (или через Deque → в массив),
	 * вызывает анализатор и возвращает SortResult.
	 */
	public SortResult sortWithAnalysis(SortRequest req) {
		List<Integer> data = req.data;
		int[] arr;

		if ("deque".equalsIgnoreCase(req.structure)) {
			// считываем из Deque
			Deque<Integer> dq = new LinkedList<>(data);
			arr = new int[dq.size()];
			for (int i = 0; i < arr.length; i++) {
				arr[i] = dq.pollFirst();
			}
		} else {
			// обычный статический (или динамический) массив
			arr = data.stream().mapToInt(Integer::intValue).toArray();
		}

		// вызвать вашу существующую сортировку с анализом
		return HeapSort.sortWithAnalysis(arr);
	}

	public SortResult sortWithAnalysis(List<Integer> data, String structure) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'sortWithAnalysis'");
	}
}
