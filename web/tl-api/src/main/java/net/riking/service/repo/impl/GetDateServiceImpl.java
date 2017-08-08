package net.riking.service.repo.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;
@Service("getDateService")
public class GetDateServiceImpl {

	public String getDate() {
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
		
	}
}
