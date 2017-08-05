package net.riking.service.repo.impl;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Service;
@Service("getDateService")
public class GetDateServiceImpl {

	public String getDate() {
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(getDate());
		
	}
}
