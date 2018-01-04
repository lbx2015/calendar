package net.riking.entity;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomJsonDateTimeDeserializer extends JsonDeserializer<Date> {
	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date = jp.getText();
		if ("" != date) {
			try {
				// 验证是不是全数字
				Pattern pattern = Pattern.compile("[0-9]*");
				Matcher isNum = pattern.matcher(date);
				if (isNum.matches())
					return new Date(Long.parseLong(date));
				else
					return format.parse(date);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

}
