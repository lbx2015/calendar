package net.riking.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import net.riking.service.getDateService;
@Service("getDateService")
public class GetDateServiceImpl implements getDateService {
	@Override
	public String getDate() {
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
	}

	@Override
	public Map<String, Set<String>> getMounthWeek(String date) {
		Map<String, Set<String>> map = new HashMap<>();
		int year = Integer.parseInt(date.substring(0,4));
		int mouth = Integer.parseInt(date.substring(4,6));
		int size = getDaysByYearMonth(year, mouth);
		for (int i = 0; i < size; i++) {
			String oneDate ;
			int n=i+1;
			if (i<9) {
				oneDate = date+"0"+n;
			}else {
				oneDate = date+n;
			}
			String weekDay = getDayOfWeekByDate(oneDate);
			System.err.println(oneDate+"是"+weekDay);
			if(!map.containsKey(weekDay)){
				 Set<String> set = new HashSet<>();
				   set.add(oneDate);
			    map.put(weekDay,set);
			   }else{
				   Set<String> set = map.get(weekDay);
				   set.add(oneDate);
			    map.put(weekDay, set);
			   }
		}
		return map;
	}
	
	 public static int getDaysByYearMonth(int year, int month) {  
         
	        Calendar a = Calendar.getInstance();  
	        a.set(Calendar.YEAR, year);  
	        a.set(Calendar.MONTH, month - 1);  
	        a.set(Calendar.DATE, 1);  
	        a.roll(Calendar.DATE, -1);  
	        int maxDate = a.get(Calendar.DATE);  
	        return maxDate;  
	    }  
	      
	    /** 
	     * 根据日期 找到对应日期的 星期 
	     */  
	    public static String getDayOfWeekByDate(String date) {  
	        String dayOfweek = "-1";  
	        try {  
	            SimpleDateFormat myFormatter = new SimpleDateFormat("yyyyMMdd");  
	            Date myDate = myFormatter.parse(date);  
	            SimpleDateFormat formatter = new SimpleDateFormat("E");  
	            String str = formatter.format(myDate);  
	            dayOfweek = str;  
	              
	        } catch (Exception e) {  
	            System.out.println("错误!");  
	        }  
	        return dayOfweek;  
	    }  
	
}
