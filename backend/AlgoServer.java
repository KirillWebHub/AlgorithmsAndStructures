package backend;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpServer;

import backend.Handler.BoyerMooreHandler;
import backend.Handler.HeapSortHandler;
import backend.Handler.KmpHandler;
import backend.Handler.StaticFileHandlear;

public class AlgoServer {
	private static final Logger logger = Logger.getLogger(AlgoServer.class.getName());
	private static final int PORT = 8080;

	public static void main(String[] args) {
		try {
			HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", PORT), 0);

			// Регистрируем обработчики
			server.createContext("/", new StaticFileHandlear("frontend"));
			server.createContext("/api/heap-sort", new HeapSortHandler());
			server.createContext("/api/boyer-moore", new BoyerMooreHandler());
			server.createContext("/api/kmp", new KmpHandler());

			server.setExecutor(null);
			server.start();

			logger.info("Сервер запущен на порту " + PORT);
		} catch (IOException e) {
			logger.severe("Ошибка при запуске сервера: " + e.getMessage());
		}
	}
}