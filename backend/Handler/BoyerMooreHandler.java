package backend.Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import backend.algorithms.BoyerMoore;
import backend.utils.JsonUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class BoyerMooreHandler implements HttpHandler {

	private static final Pattern ARRAY_PATTERN = Pattern.compile("\"array\"\\s*:\\s*(\\[.*?\\])", Pattern.DOTALL);
	private static final Pattern PATTERN_PATTERN = Pattern.compile("\"pattern\"\\s*:\\s*(\\[.*?\\])", Pattern.DOTALL);

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
			sendJson(exchange, 405, "{\"error\":\"Метод не поддерживается\"}");
			return;
		}

		String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
		System.out.println("[DEBUG] Raw JSON body: " + body); // лог

		try {
			Matcher arrayMatcher = ARRAY_PATTERN.matcher(body);
			boolean arrayFound = arrayMatcher.find();
			System.out.println("[DEBUG] array matched: " + arrayFound);
			if (!arrayFound)
				throw new IllegalArgumentException();
			String arrayStr = arrayMatcher.group(1);
			System.out.println("[DEBUG] matched array: " + arrayStr);
			int[] array = JsonUtils.parseJsonArray(arrayStr).stream().mapToInt(i -> i).toArray();

			Matcher patternMatcher = PATTERN_PATTERN.matcher(body);
			boolean patternFound = patternMatcher.find();
			System.out.println("[DEBUG] pattern matched: " + patternFound);
			if (!patternFound)
				throw new IllegalArgumentException();
			String patternStr = patternMatcher.group(1);
			System.out.println("[DEBUG] matched pattern: " + patternStr);
			int[] pattern = JsonUtils.parseJsonArray(patternStr).stream().mapToInt(i -> i).toArray();

			int[] foundIndices = BoyerMoore.search(array, pattern);

			StringBuilder json = new StringBuilder("{");
			json.append("\"foundIndices\":").append(JsonUtils.toJsonArray(foundIndices));
			json.append("}");

			sendJson(exchange, 200, json.toString());
		} catch (Exception e) {
			System.out.println("[ERROR] Exception during parsing or search: " + e);
			sendJson(exchange, 400, "{\"error\":\"Неверный формат JSON или данных\"}");
		}
	}

	private void sendJson(HttpExchange exchange, int code, String response) throws IOException {
		exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
		byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
		exchange.sendResponseHeaders(code, bytes.length);
		try (OutputStream os = exchange.getResponseBody()) {
			os.write(bytes);
		}
	}
}
