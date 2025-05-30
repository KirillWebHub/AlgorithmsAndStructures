package backend.Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import backend.algorithms.KnuthMorrisPratt;
import backend.utils.JsonUtils;

import backend.structures.RedBlackTree;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KmpHandler implements HttpHandler {
	private static final Pattern TEXT_PATTERN = Pattern.compile("\"text\"\\s*:\\s*\"(.*?)\"", Pattern.DOTALL);
	private static final Pattern PAT_PATTERN = Pattern.compile("\"pattern\"\\s*:\\s*\"(.*?)\"", Pattern.DOTALL);

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
			var headers = exchange.getResponseHeaders();
			headers.set("Access-Control-Allow-Origin", "*");
			headers.set("Access-Control-Allow-Methods", "POST, OPTIONS");
			headers.set("Access-Control-Allow-Headers", "Content-Type");
			exchange.sendResponseHeaders(204, -1); // Нет тела
			return;
		}

		if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
			sendJson(exchange, 405, "{\"error\":\"Метод не поддерживается\"}");
			return;
		}

		String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
		try {
			// 1) Извлекаем text
			Matcher tm = TEXT_PATTERN.matcher(body);
			if (!tm.find())
				throw new IllegalArgumentException();
			String text = tm.group(1);

			// 2) Извлекаем pattern
			Matcher pm = PAT_PATTERN.matcher(body);
			if (!pm.find())
				throw new IllegalArgumentException();
			String pattern = pm.group(1);

			// 3) Выполняем поиск
			KnuthMorrisPratt kmp = new KnuthMorrisPratt();
			// результат — список позиций
			var matches = kmp.search(text.toCharArray(), pattern.toCharArray());
			// Вставка результатов KMP в красно-чёрное дерево
			RedBlackTree tree = new RedBlackTree();
			for (int index : matches) {
				tree.insert(index);
			}
			String treeString = tree.getTreeString().replace("\n", "\\n");
			System.out.println("\nKMP → Red-Black Tree:");
			tree.printInOrder();

			// 4) Формируем JSON-ответ
			StringBuilder json = new StringBuilder("{");
			json.append("\"matches\":").append(JsonUtils.toJsonArray(matches)).append(",");
			json.append("\"tree\":\"").append(treeString).append("\"");
			json.append("}");

			sendJson(exchange, 200, json.toString());
		} catch (Exception e) {
			sendJson(exchange, 400, "{\"error\":\"Неверный формат JSON или данных\"}");
		}
	}

	private void sendJson(HttpExchange exchange, int code, String response) throws IOException {
		var headers = exchange.getResponseHeaders();
		headers.set("Content-Type", "application/json; charset=UTF-8");
		headers.set("Access-Control-Allow-Origin", "*");

		byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
		exchange.sendResponseHeaders(code, bytes.length);
		try (OutputStream os = exchange.getResponseBody()) {
			os.write(bytes);
		}
	}
}
