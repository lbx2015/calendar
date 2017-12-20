package net.riking.entity;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.riking.core.entity.ILog;
import net.riking.core.entity.LogConfig;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class ApiResp implements ILog {
	protected Object data;// 返回的数据

	protected Short code; // 状态码

	protected String codeDesc; // 状态码描述

	protected Integer runtime = 0; // 运行时长

	public ApiResp() {
		super();
	}

	/**
	 * default 200
	 * 
	 * @param _data
	 */
	public ApiResp(Object data) {
		this(data, (short) 200);
	}

	public ApiResp(Object data, Short code) {
		this(data, code, "");
	}

	public ApiResp(Object data, Short code, String codeDesc) {
		this.data = data;
		this.code = code;
		this.codeDesc = codeDesc;
	}

	public Object getdata() {
		return data;
	}

	public void setdata(String data) {
		this.data = data;
	}

	public Short getCode() {
		return code;
	}

	public ApiResp setCode(Short code) {
		this.code = code;
		return this;
	}

	public String getCodeDesc() {
		return codeDesc;
	}

	public ApiResp setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
		return this;
	}

	public Integer getRuntime() {
		return runtime;
	}

	public void setRuntime(Integer runtime) {
		this.runtime = runtime;
	}

	public String toJSON() throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(this);
	}

	@Override
	public LogConfig getLogConfig() {
		String res = this.getCode() == 200 ? "成功" : "失败";
		String log = String.format("执行结果:%s", res);
		if (200 != this.getCode() && StringUtils.isNotEmpty(getCodeDesc())) {
			log = String.format("%s.原因:%s", log, res);
		}
		LogConfig config = new LogConfig();
		config.setLog(log);
		return config;
	}

}
