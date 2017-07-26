package net.riking.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.riking.entity.model.Branch;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.riking.config.SsoConfig;
import net.riking.core.cache.RCache;
import net.riking.core.entity.Resp;
import net.riking.core.entity.TokenHolder;
import net.riking.util.ApiCall;

@Service("userBranchServiceImpl")
public class UserBranchServiceImpl {
	private static RCache<List<String>> cache = new RCache<List<String>>(60*30);

	@Autowired
	private SsoConfig ssoConfig;
		
	public void put(Long userId) throws Exception{
		String getTreeUrl = "/branch/getTree";
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Auth", TokenHolder.get().getToken());
		String result = ApiCall.get(ssoConfig.getUrl() + getTreeUrl, headers);
		
		ObjectMapper mapper = new ObjectMapper();
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class,Map.class);
		Resp resp = mapper.readValue(result, Resp.class);
		List<Map<String,String>> list = mapper.readValue(resp.get_data(), javaType);
		
		List<String> branchCodes = new ArrayList<String>();
		for (Map<String, String> map : list) {
			branchCodes.add(map.get("extra"));
		}
		cache.put(userId + "", branchCodes);
	}
	
	
	public void put(Long userId,String token) throws Exception{
		String getTreeUrl = "/branch/getTree";
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Auth", token);
		String result = ApiCall.get(ssoConfig.getUrl() + getTreeUrl, headers);
		ObjectMapper mapper = new ObjectMapper();
		JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class,Map.class);
		Resp resp = mapper.readValue(result, Resp.class);
		List<Map<String,String>> list = mapper.readValue(resp.get_data(), javaType);
		
		List<String> branchCodes = new ArrayList<String>();
		for (Map<String, String> map : list) {
			branchCodes.add(map.get("extra"));
		}
		cache.put(userId + "", branchCodes);
	}
	
	public List<String> get(Long userId){
		List<String> list = cache.get(userId + "");
		if(list != null){
			return list;
		}else{
			try {
				this.put(userId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return cache.get("" + userId);
		}
	}
	
	public List<String> get(Long userId,String token){
		List<String> list = cache.get(userId + "");
		if(list != null){
			return list;
		}else{
			try {
				this.put(userId,token);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return cache.get("" + userId);
		}
	}

	public Branch getBranch(String jgbm,String token) throws Exception {
		String url = ssoConfig.getUrl() + "/branch/getByCode?branchCode="+jgbm;
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Auth", token);
		String result = ApiCall.get(url, headers);
		ObjectMapper mapper = new ObjectMapper();
		Resp resp=mapper.readValue(result,Resp.class);
		Branch branchN = new Branch();
		if(null == resp.getCode() ||  resp.getCode() != 200 || StringUtils.isBlank(resp.get_data())){
			return branchN;
		}
		JSONArray jsonArray = JSONArray.fromObject(resp.get_data());
		if(null != jsonArray && jsonArray.size() > 0){
			branchN.setBranchCode(jsonArray.getJSONObject(0).getString("branchCode"));
			branchN.setId(jsonArray.getJSONObject(0).getLong("id"));
		}
		return branchN;
	}

	public Branch getBranch(Long id,String token) throws Exception {
		String url = ssoConfig.getUrl() + "/branch/get?id="+id;
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("Auth", token);
		String result = ApiCall.get(url, headers);
		ObjectMapper mapper = new ObjectMapper();
		Resp resp=mapper.readValue(result,Resp.class);
		Branch branchN = new Branch();
		if(null == resp.getCode() ||  resp.getCode() != 200 || StringUtils.isBlank(resp.get_data())){
			return branchN;
		}
		JSONObject object=JSONObject.fromObject(resp.get_data());

		branchN.setBranchCode(object.getString("branchCode"));
		branchN.setBranchName(object.getString("branchName"));
		branchN.setAmlBranchCode(object.getString("amlBranchCode"));
		branchN.setId(object.getLong("id"));
		return branchN;
	}
}
