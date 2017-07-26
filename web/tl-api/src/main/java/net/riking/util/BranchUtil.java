package net.riking.util;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.riking.config.SsoConfig;
import net.riking.core.entity.Resp;
import net.riking.core.entity.Token;
import net.riking.entity.model.Branch;
import net.sf.json.JSONObject;

public class BranchUtil {

	public static Branch getBranch(SsoConfig ssoConfig,Token token) throws Exception {
		Long jgid = token.getBranchId();
		String url = ssoConfig.getUrl() + "/branch/get?id=" + String.valueOf(jgid != null ? jgid : 4L);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Auth", token.getToken());
		String result = ApiCall.get(url, headers);
		ObjectMapper mapper = new ObjectMapper();
		Resp resp = mapper.readValue(result, Resp.class);
		JSONObject object = JSONObject.fromObject(resp.get_data());
		Branch branch = (Branch) JSONObject.toBean(object, Branch.class);
		return branch;
	}
}
