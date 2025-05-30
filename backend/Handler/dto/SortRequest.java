package backend.Handler.dto;

import java.util.List;

public class SortRequest {
	// массив чисел
	public List<Integer> data;
	// либо "array", либо "deque"
	public String structure;
}
