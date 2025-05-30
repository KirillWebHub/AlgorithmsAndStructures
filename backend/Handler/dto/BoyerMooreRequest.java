package backend.Handler.dto;

public class BoyerMooreRequest {
	private int[] array;
	private int[] pattern;

	public int[] getArray() {
		return array;
	}

	public void setArray(int[] array) {
		this.array = array;
	}

	public int[] getPattern() {
		return pattern;
	}

	public void setPattern(int[] pattern) {
		this.pattern = pattern;
	}
}