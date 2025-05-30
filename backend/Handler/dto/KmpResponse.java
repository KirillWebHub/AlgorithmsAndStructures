package backend.Handler.dto;

import java.util.List;

public class KmpResponse {
	private List<Integer> matches;
	// (по желанию) добавьте поля comparisons, shifts и т.п.

	public List<Integer> getMatches() {
		return matches;
	}

	public void setMatches(List<Integer> matches) {
		this.matches = matches;
	}
}
