package net.riking.service;

import java.util.Map;
import java.util.Set;

import net.riking.entity.model.Period;

public interface SysDateService {
	public String getDate();

	public Map<String, Set<String>> getMounthWeek(String date);

	public Period getDate(String date, String type);
}
