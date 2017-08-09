package net.riking.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

import net.riking.service.getDateService;
@Service("getDateService")
public class GetDateServiceImpl implements getDateService {

	public String getDate() {
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(new Date());
		
	}
}
