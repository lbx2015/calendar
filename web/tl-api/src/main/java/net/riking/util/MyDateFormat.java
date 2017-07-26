package net.riking.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDateFormat extends SimpleDateFormat {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3983856621801355508L;


	@Override
	public Date parse(String source, ParsePosition pos) {
		try{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			return format.parse(source);
		}catch(Exception e){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				return format.parse(source);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return null;
	}
	
	
	
}
