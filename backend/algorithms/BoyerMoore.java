package backend.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoyerMoore {
	public static int[] search(int[] text, int[] pattern) {
		List<Integer> matches = new ArrayList<>();
		if (pattern.length == 0 || text.length < pattern.length) {
			return new int[0];
		}

		int[] badChar = makeBadCharTable(pattern);
		int[] goodSuffix = makeGoodSuffixTable(pattern);

		int i = 0;
		int n = text.length;
		int m = pattern.length;
		while (i <= n - m) {
			int j = m - 1;
			while (j >= 0 && pattern[j] == text[i + j]) {
				j--;
			}
			if (j < 0) {
				matches.add(i);
				i += goodSuffix[0]; // теперь goodSuffix[0] == m для непересекающихся
			} else {
				int bcShift = j - badChar[text[i + j]];
				int gsShift = goodSuffix[j];
				i += Math.max(1, Math.max(bcShift, gsShift));
			}
		}

		return matches.stream().mapToInt(Integer::intValue).toArray();
	}

	private static int[] makeBadCharTable(int[] pattern) {
		int[] table = new int[256];
		Arrays.fill(table, -1);
		for (int i = 0; i < pattern.length; i++) {
			int c = pattern[i];
			if (c >= 0 && c < 256) {
				table[c] = i;
			}
		}
		return table;
	}

	private static int[] makeGoodSuffixTable(int[] pattern) {
		int m = pattern.length;
		int[] f = new int[m + 1];
		int[] s = new int[m + 1];

		f[m] = m + 1;
		int j = m + 1;
		for (int i = m; i > 0; i--) {
			while (j <= m && pattern[i - 1] != pattern[j - 1]) {
				if (s[j] == 0) {
					s[j] = j - i;
				}
				j = f[j];
			}
			i--;
			j--;
			f[i] = j;
		}

		j = f[0];
		for (int k = 0; k <= m; k++) {
			if (s[k] == 0) {
				s[k] = j;
			}
			if (k == j) {
				j = f[j];
			}
		}

		int[] table = new int[m];
		for (int k = 0; k < m; k++) {
			table[k] = s[k + 1];
		}
		return table;
	}

	public static void main(String[] args) {
		int[] text = { 1, 2, 3, 1, 2, 3 };
		int[] pattern = { 1, 2, 3 };
		System.out.println(Arrays.toString(search(text, pattern))); // теперь [0, 3]
	}
}
