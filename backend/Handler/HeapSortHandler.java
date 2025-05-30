package backend.Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import backend.Handler.dto.SortRequest;
import backend.algorithms.HeapSort.SortResult;
import backend.service.HeapSortService;
import backend.utils.JsonUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HeapSortHandler implements HttpHandler {
	private final HeapSortService service = new HeapSortService();
	// Регэксп для поля "structure":"array" или "deque"
	private static final Pattern STRUCT_PATTERN = Pattern.compile("\"structure\"\\s*:\\s*\"(array|deque)\"",
			Pattern.CASE_INSENSITIVE);

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
			sendJson(exchange, 405, "{\"error\":\"Метод не поддерживается\"}");
			return;
		}

		String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
		try {
			// 1) Вырезаем JSON-массив данных между первой '[' и ']'
			int start = body.indexOf('[');
			int end = body.indexOf(']', start);
			if (start < 0 || end < 0)
				throw new IllegalArgumentException();
			List<Integer> data = JsonUtils.parseJsonArray(body.substring(start, end + 1));

			// 2) Определяем структуру (array по умолчанию)
			Matcher m = STRUCT_PATTERN.matcher(body);
			String structure = m.find() ? m.group(1).toLowerCase() : "array";

			// 3) Сортировка с анализом
			SortRequest req = new SortRequest();
			req.data = data;
			req.structure = structure;
			SortResult result = service.sortWithAnalysis(req);

			// 4) Формируем JSON вручную
			StringBuilder json = new StringBuilder("{");
			json.append("\"sortedArray\":").append(JsonUtils.toJsonArray(result.sortedArray)).append(',');
			json.append("\"comparisons\":").append(result.comparisons).append(',');
			json.append("\"swaps\":").append(result.swaps).append(',');
			json.append("\"heapifyCalls\":").append(result.heapifyCalls).append(',');
			json.append("\"timeNanos\":").append(result.timeNanos);
			json.append("}");

			sendJson(exchange, 200, json.toString());
		} catch (Exception e) {
			sendJson(exchange, 400, "{\"error\":\"Неверный формат JSON или данных\"}");
		}
	}

	private void sendJson(HttpExchange exchange, int code, String response) throws IOException {
		var headers = exchange.getResponseHeaders();
		headers.set("Content-Type", "application/json; charset=UTF-8");
		byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
		exchange.sendResponseHeaders(code, bytes.length);
		try (OutputStream os = exchange.getResponseBody()) {
			os.write(bytes);
		}
	}
}
