package net.riking.entity;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.riking.core.entity.ILog;
import net.riking.core.entity.LogConfig;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class AppResp implements ILog {
	protected Object _data;
	protected Short code; // 状态码
	protected String codeDesc; // 状态码描述
	protected Integer runtime = 0; // 运行时长

	public AppResp() {
		super();
	}

	/**
	 * default 200
	 * 
	 * @param _data
	 */
	public AppResp(Object _data) {
		this(_data, (short) 200);
	}

	public AppResp(Object _data, Short code) {
		this(_data, code, "");
	}

	public AppResp(Object _data, Short code, String codeDesc) {
		this._data =_data;
		this.code = code;
		this.codeDesc = codeDesc;
	}
	
	/*@JsonIgnore
	public Object getData() {
		return _data;
	}

	public void setData(Object data) {
		if (data != null) {
			this._data = data;
		}
	}*/

	public Object get_data() {
		return _data;
	}

	public void set_data(String _data) {
		this._data = _data;
	}

	public Short getCode() {
		return code;
	}

	public AppResp setCode(Short code) {
		this.code = code;
		return this;
	}

	public String getCodeDesc() {
		return codeDesc;
	}

	public AppResp setCodeDesc(String codeDesc) {
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
