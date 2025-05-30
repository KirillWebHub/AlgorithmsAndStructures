package backend.Handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.nio.file.*;

public class StaticFileHandlear implements HttpHandler {
	private final Path rootDir;

	public StaticFileHandlear(String rootDir) {
		this.rootDir = Paths.get(rootDir).toAbsolutePath().normalize();
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		URI uri = exchange.getRequestURI();
		String path = uri.getPath();
		if (path.equals("/")) {
			path = "/index.html"; // точка входа
		}
		Path filePath = rootDir.resolve(path.substring(1)).normalize();

		// Защита от выхода за корень и от директорий
		if (!filePath.startsWith(rootDir) || !Files.exists(filePath) || Files.isDirectory(filePath)) {
			send404(exchange);
			return;
		}

		String contentType = Files.probeContentType(filePath);
		if (contentType == null) {
			contentType = "application/octet-stream";
		}
		exchange.getResponseHeaders().set("Content-Type", contentType + "; charset=UTF-8");
		byte[] bytes = Files.readAllBytes(filePath);
		exchange.sendResponseHeaders(200, bytes.length);
		try (OutputStream os = exchange.getResponseBody()) {
			os.write(bytes);
		}
	}

	private void send404(HttpExchange ex) throws IOException {
		String msg = "404 Not Found";
		ex.sendResponseHeaders(404, msg.length());
		try (OutputStream os = ex.getResponseBody()) {
			os.write(msg.getBytes());
		}
	}
}
