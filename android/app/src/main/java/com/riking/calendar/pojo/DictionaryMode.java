package com.riking.calendar.pojo;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * 邮箱后缀：/emailSuffixApp/getAll
 * <p>
 * 部门枚举 ： /modelPropDictApp/T_APP_USER   参数  fileds =[ “DEPT”]
 * <p>
 * 证件类型枚举 ： /modelPropDictApp/T_APP_USER   参数  fileds =[ “IDTYPE”]
 * <p>
 * 用户性别枚举 ： /modelPropDictApp/T_APP_USER   参数  fileds =[ “SEX”]
 * <p>
 * <p>
 * 用户地址联动 ： /modelPropDictApp/getAddF   参数  keyword= "输入值"
 * /modelPropDictApp/getAddS   参数  key = "上级选中值"  keyword= "输入值"
 * <p>
 * <p>
 * 最后保存  ：/appUserApp/addOrUpdate
 */
public class DictionaryMode {
    public ArrayList<Dictionary> _data;
    public Short code; // 状态码
    public String codeDesc; // 状态码描述
    public Integer runtime = 0; // 运行时长

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
