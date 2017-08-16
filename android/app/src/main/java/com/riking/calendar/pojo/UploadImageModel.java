package com.riking.calendar.pojo;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MultipartBody;

/**
 * Created by zw.zhang on 2017/8/16.
 */

public class UploadImageModel {
    //image url
    public String _data;// 返回的数据
    public Short code; // 状态码
    public String codeDesc; // 状态码描述
    public Integer runtime = 0; // 运行时长

}
