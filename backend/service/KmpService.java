package backend.service;

import backend.Handler.dto.KmpRequest;
import backend.Handler.dto.KmpResponse;
import backend.algorithms.KnuthMorrisPratt;

public class KmpService {
	public KmpResponse search(KmpRequest req) {
		KnuthMorrisPratt kmp = new KnuthMorrisPratt();
		// превратим строки в массивы символов
		char[] text = req.getText().toCharArray();
		char[] pat = req.getPattern().toCharArray();
		KmpResponse resp = new KmpResponse();
		resp.setMatches(kmp.search(text, pat));
		return resp;
	}
}