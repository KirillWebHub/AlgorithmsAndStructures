package backend.algorithms;

import java.util.ArrayList;
import java.util.List;

public class KnuthMorrisPratt {

	/**
	 * Строит π-функцию (префикс-функцию) для шаблона p
	 * 
	 * @return массив длины m, где pi[i] = длина наибольшего собственного
	 *         префикса-суффикса для p[0..i]
	 */
	public int[] buildPrefixFunction(char[] p) {
		int m = p.length;
		int[] pi = new int[m];
		pi[0] = 0;
		int k = 0;
		for (int i = 1; i < m; i++) {
			while (k > 0 && p[k] != p[i]) {
				k = pi[k - 1];
			}
			if (p[k] == p[i]) {
				k++;
			}
			pi[i] = k;
		}
		return pi;
	}

	/**
	 * Ищет все вхождения pattern в text.
	 * 
	 * @return список стартовых индексов вхождений
	 */
	public List<Integer> search(char[] text, char[] pattern) {
		List<Integer> result = new ArrayList<>();
		int n = text.length, m = pattern.length;
		if (m == 0 || n < m)
			return result;

		int[] pi = buildPrefixFunction(pattern);
		int q = 0; // число символов, совпавших до текущего i
		for (int i = 0; i < n; i++) {
			while (q > 0 && pattern[q] != text[i]) {
				q = pi[q - 1];
			}
			if (pattern[q] == text[i]) {
				q++;
			}
			if (q == m) {
				// найдено вхождение, его конец — i, начало — i-m+1
				result.add(i - m + 1);
				q = pi[q - 1]; // готовимся искать дальше
			}
		}
		return result;
	}

	// Для первого тестирования добавим метод main.

	public static void main(String[] args) {
		String txt = "ababcabcabababd";
		String pat = "ababd";
		KnuthMorrisPratt kmp = new KnuthMorrisPratt();
		System.out.println("TXT = " + txt);
		System.out.println("PAT = " + pat);
		System.out.println("Matches at: " + kmp.search(txt.toCharArray(), pat.toCharArray()));
	}
}
