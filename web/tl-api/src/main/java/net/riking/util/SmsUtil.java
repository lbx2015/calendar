package net.riking.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import net.riking.config.CodeDef;
import net.riking.config.Config;
import net.riking.config.Const;
import net.riking.entity.model.AliSme;
@Component("smsUtil")
public class SmsUtil {
	private static final Logger logger = LogManager.getLogger("SmsUtil");
	@Autowired
	Config config;
	//产品名称:云通信短信API产品,开发者无需替换  
    static final String product = "Dysmsapi";  
    //产品域名,开发者无需替换  
    static final String domain = "dysmsapi.aliyuncs.com";  
    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)   
    public  SendSmsResponse sendSms(String phone, String verifyCode) throws ClientException {  
    	if(config.getIsOpenSms().intValue() == 0){
    		SendSmsResponse response = new SendSmsResponse();
    		response.setCode("OK");
    		return response;
    	}
    	
    	AliSme alims = new AliSme(phone, config.getSignName(), config.getCommonTemplateCode(), verifyCode);
    	
        //可自助调整超时时间  
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");  
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");  
  
        //初始化acsClient,暂不支持region化  
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", config.getAccessKeyId(), config.getAccessKeySecret());  
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);  
        IAcsClient acsClient = new DefaultAcsClient(profile);  
  
        //组装请求对象-具体描述见控制台-文档部分内容  
        SendSmsRequest request = new SendSmsRequest();  
        //必填:待发送手机号  
        request.setPhoneNumbers(alims.getPhoneNumbers());  
        //必填:短信签名-可在短信控制台中找到  
        request.setSignName(alims.getSignName());  
        //必填:短信模板-可在短信控制台中找到  
        request.setTemplateCode(alims.getTemplateCode());  
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为  
        request.setTemplateParam("{\"code\":\""+alims.getVerificationCode()+"\"}");  
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者  
        request.setOutId("yourOutId"); 
        //hint 此处可能会抛出异常，注意catch  
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);  
  
        return sendSmsResponse;  
    }  
  
  
    public  QuerySendDetailsResponse querySendDetails(String bizId) throws ClientException {  
  
        //可自助调整超时时间  
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");  
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");  
  
        //初始化acsClient,暂不支持region化  
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", config.getAccessKeyId(), config.getAccessKeySecret());  
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);  
        IAcsClient acsClient = new DefaultAcsClient(profile);  
  
        //组装请求对象  
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();  
        //必填-号码  
        request.setPhoneNumber("15000000000");  
        //可选-流水号  
        request.setBizId(bizId);  
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd  
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");  
        request.setSendDate(ft.format(new Date()));  
        //必填-页大小  
        request.setPageSize(10L);  
        //必填-当前页码从1开始计数  
        request.setCurrentPage(1L);  
  
        //hint 此处可能会抛出异常，注意catch  
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);  
  
        return querySendDetailsResponse;  
    }
    
    /***
     * 校验验证码
     * @author james.you
     * @version crateTime：2017年11月29日 下午2:07:09
     * @used TODO
     * @param phone 手机号
     * @param verifyCode 验证码
     * @return
     * @throws Exception
     */
    public boolean checkValidCode(String phone, String verifyCode) throws NullPointerException{
    	if(StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(verifyCode)){
    		Object o = RedisUtil.getInstall().getObject(Const.VALID_ + phone.trim());
    		String code = o == null? null : o.toString();
    		logger.info("code={}",code);
    		if(StringUtils.isNotBlank(code)){
	    		if(code.equals(verifyCode)){
	    			return true;
	    		}
    		}else{
    			throw new NullPointerException(CodeDef.EMP.CHECK_CODE_TIME_OUT+"");
    		}
    	}else{
    		throw new NullPointerException("phone={"+phone+"} and verifyCode={"+verifyCode+"} is empty");
    	}
    	return false;
    }
  
}
