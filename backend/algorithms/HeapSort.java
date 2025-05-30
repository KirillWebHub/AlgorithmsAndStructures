package backend.algorithms;

/**
 * Реализация пирамидальной сортировки (HeapSort) с анализом сложности
 */
public class HeapSort {
	// Счетчики операций для анализа сложности
	private static long comparisonCount;
	private static long swapCount;
	private static long heapifyCallCount;

	/**
	 * Публичный метод для сортировки с анализом сложности
	 * 
	 * @param arr массив для сортировки
	 * @return объект с результатами сортировки и метриками
	 */
	public static SortResult sortWithAnalysis(int[] arr) {
		resetCounters();
		long startTime = System.nanoTime();

		sort(arr);

		long endTime = System.nanoTime();
		return new SortResult(
				arr,
				comparisonCount,
				swapCount,
				heapifyCallCount,
				endTime - startTime);
	}

	/**
	 * Основной метод сортировки
	 */
	public static void sort(int[] arr) {
		int n = arr.length;

		// Построение кучи (перегруппируем массив)
		for (int i = n / 2 - 1; i >= 0; i--) {
			heapify(arr, n, i);
		}

		// Один за другим извлекаем элементы из кучи
		for (int i = n - 1; i > 0; i--) {
			// Перемещаем текущий корень в конец
			swap(arr, 0, i);
			swapCount++;

			// Вызываем heapify на уменьшенной куче
			heapify(arr, i, 0);
		}
	}

	private static void heapify(int[] arr, int n, int i) {
		heapifyCallCount++;
		int largest = i; // Инициализируем наибольший элемент как корень
		int l = 2 * i + 1; // левый = 2*i + 1
		int r = 2 * i + 2; // правый = 2*i + 2

		// Если левый дочерний элемент больше корня
		if (l < n) {
			comparisonCount++;
			if (arr[l] > arr[largest]) {
				largest = l;
			}
		}

		// Если правый дочерний элемент больше, чем самый большой элемент на данный
		// момент
		if (r < n) {
			comparisonCount++;
			if (arr[r] > arr[largest]) {
				largest = r;
			}
		}

		// Если самый большой элемент не корень
		if (largest != i) {
			comparisonCount++;
			swap(arr, i, largest);
			swapCount++;

			// Рекурсивно преобразуем в двоичную кучу затронутое поддерево
			heapify(arr, n, largest);
		}
	}

	private static void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	private static void resetCounters() {
		comparisonCount = 0;
		swapCount = 0;
		heapifyCallCount = 0;
	}

	/**
	 * Класс для хранения результатов сортировки
	 */
	public static class SortResult {
		public final int[] sortedArray;
		public final long comparisons;
		public final long swaps;
		public final long heapifyCalls;
		public final long timeNanos;

		public SortResult(int[] sortedArray, long comparisons, long swaps,
				long heapifyCalls, long timeNanos) {
			this.sortedArray = sortedArray;
			this.comparisons = comparisons;
			this.swaps = swaps;
			this.heapifyCalls = heapifyCalls;
			this.timeNanos = timeNanos;
		}
	}
}
