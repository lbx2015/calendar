package net.riking.service;

import java.util.Map;
import java.util.Set;

public interface getDateService {
	public String getDate();
	
	public Map<String, Set<String>> getMounthWeek(String date) ;
}
