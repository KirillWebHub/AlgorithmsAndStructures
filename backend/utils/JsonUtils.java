package backend.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import backend.Handler.dto.BoyerMooreRequest;
import backend.Handler.dto.BoyerMooreResponse;
import backend.Handler.dto.KmpRequest;
import backend.Handler.dto.KmpResponse;

public class JsonUtils {

	// ─── Парсер простых JSON-массивов ─────────────────────────────────

	/** Простейший парсер JSON-массива чисел вида "[1,2,3]". */
	public static List<Integer> parseJsonArray(String json) {
		List<Integer> out = new ArrayList<>();
		String inner = json.trim().replaceAll("^\\[|\\]$", "").trim();
		if (inner.isEmpty())
			return out;
		for (String part : inner.split(",")) {
			out.add(Integer.parseInt(part.trim()));
		}
		return out;
	}

	/** Собирает из List<Integer> JSON-массив "[1,2,3]". */
	public static String toJsonArray(List<Integer> list) {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i));
			if (i < list.size() - 1)
				sb.append(',');
		}
		return sb.append(']').toString();
	}

	/** Собирает из int[] JSON-массив "[1,2,3]". */
	public static String toJsonArray(int[] arr) {
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < arr.length; i++) {
			sb.append(arr[i]);
			if (i < arr.length - 1)
				sb.append(',');
		}
		return sb.append(']').toString();
	}

	// ─── Универсальные fromJson / toJson ────────────────────────────────

	/**
	 * Десериализация JSON из InputStream в указанный класс DTO.
	 * Поддерживает BoyerMooreRequest и KmpRequest.
	 */
	public static <T> T fromJson(InputStream is, Class<T> cls) throws IOException {
		String json = readAll(is);
		if (cls.equals(BoyerMooreRequest.class)) {
			@SuppressWarnings("unchecked")
			T r = (T) jsonToBoyerMooreRequest(json);
			return r;
		}
		if (cls.equals(KmpRequest.class)) {
			@SuppressWarnings("unchecked")
			T r = (T) jsonToKmpRequest(json);
			return r;
		}
		throw new IllegalArgumentException("JsonUtils.fromJson: unsupported class " + cls);
	}

	private static BoyerMooreRequest jsonToBoyerMooreRequest(String json) {
		List<Integer> array = parseJsonField(json, "array");
		List<Integer> pattern = parseJsonField(json, "pattern");
		BoyerMooreRequest r = new BoyerMooreRequest();
		r.setArray(array.stream().mapToInt(i -> i).toArray());
		r.setPattern(pattern.stream().mapToInt(i -> i).toArray());
		return r;
	}

	private static KmpRequest jsonToKmpRequest(String json) {
		String text = extractString(json, "text");
		String pattern = extractString(json, "pattern");
		KmpRequest r = new KmpRequest();
		r.setText(text);
		r.setPattern(pattern);
		return r;
	}

	/**
	 * Сериализация DTO в JSON-строку.
	 * Поддерживает BoyerMooreResponse и KmpResponse.
	 */
	public static String toJson(Object obj) {
		if (obj instanceof BoyerMooreResponse) {
			BoyerMooreResponse resp = (BoyerMooreResponse) obj;
			return "{\"foundIndices\":" + toJsonArray(resp.getFoundIndices()) + "}";
		}
		if (obj instanceof KmpResponse) {
			KmpResponse resp = (KmpResponse) obj;
			return "{\"matches\":" + toJsonArray(resp.getMatches()) + "}";
		}
		throw new IllegalArgumentException("JsonUtils.toJson: unsupported type " + obj.getClass());
	}

	// ─── Вспомогательные методы ────────────────────────────────────────

	/** Считывает весь InputStream в одну строку (UTF-8). */
	private static String readAll(InputStream is) throws IOException {
		try (BufferedReader rd = new BufferedReader(
				new InputStreamReader(is, StandardCharsets.UTF_8))) {
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString().trim();
		}
	}

	/**
	 * Извлекает из JSON-плейнстроки поле-массив по ключу "key":[…].
	 * Возвращает список чисел.
	 */
	private static List<Integer> parseJsonField(String json, String key) {
		Pattern p = Pattern.compile("\"" + key + "\"\\s*:\\s*(\\[.*?\\])");
		Matcher m = p.matcher(json);
		if (m.find()) {
			return parseJsonArray(m.group(1));
		}
		return new ArrayList<>();
	}

	/**
	 * Извлекает из JSON-плейнстроки поле-строку по ключу "key":"value".
	 * Возвращает содержимое value.
	 */
	private static String extractString(String json, String key) {
		Pattern p = Pattern.compile("\"" + key + "\"\\s*:\\s*\"(.*?)\"", Pattern.DOTALL);
		Matcher m = p.matcher(json);
		return m.find() ? m.group(1) : "";
	}
}
