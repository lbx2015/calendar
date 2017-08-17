package net.riking.web.appInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.core.annos.AuthPass;
import net.riking.entity.AppResp;
import net.riking.entity.model.AppUser;
import net.riking.service.repo.AppUserRepo;
/**
 * app用户信息操作
 * @author you.fei
 * @version crateTime：2017年8月5日 下午4:31:03
 * @used TODO
 */
@RestController
@RequestMapping(value = "/appUserApp")
public class AppUserServer {
	@Autowired
	AppUserRepo appUserRepo;
	
	@Autowired
	HttpServletRequest request;
	
	@ApiOperation(value = "得到<单个>用户信息", notes = "POST")
	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public AppResp get_(@RequestParam("id") String id) {
		AppUser appUser = appUserRepo.findOne(id);
		return new AppResp(appUser, CodeDef.SUCCESS);
	}
	
	
	@ApiOperation(value = "添加或者更新用户信息", notes = "POST")
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public AppResp addOrUpdate_(@RequestBody AppUser appUser) {
		if(StringUtils.isEmpty(appUser.getId())||StringUtils.isEmpty(appUser.getDeleteState())){
			appUser.setDeleteState("1");
		}
		AppUser dbUser = appUserRepo.findById(appUser.getId());
		try {
			merge(dbUser,appUser);
		} catch (Exception e) {
			return new AppResp(CodeDef.ERROR);
		}
		AppUser save = appUserRepo.save(dbUser);
		return new AppResp(save, CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "更新用户手机设备信息", notes = "POST")
	@RequestMapping(value = "/IsChangeMac", method = RequestMethod.POST)
	public AppResp IsChangeMac_(@RequestBody AppUser appUser) {
		AppUser appUser2 = appUserRepo.findOne(appUser.getId());
		String seqNum = appUser.getPhoneSeqNum();
		if(appUser2!=null && !appUser2.getPhoneSeqNum().equals(seqNum)){
			appUser2.setPhoneSeqNum(seqNum);
			appUser2.setPhoneType(appUser.getPhoneType());
			appUserRepo.save(appUser2);
			return new AppResp(true,CodeDef.SUCCESS);
		}
		return new AppResp(false,CodeDef.SUCCESS);
	}
	
	@AuthPass
	@ApiOperation(value = "上传头像", notes = "POST")
	@RequestMapping(value = "/upLoad", method = RequestMethod.POST)
	public AppResp upLoad(@RequestParam MultipartFile mFile, @RequestParam("id")String id) {
		String url = request.getRequestURL().toString();
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			is = mFile.getInputStream();
			String path = this.getClass().getResource("/").getPath()+ Const.TL_STATIC_PATH + Const.TL_PHOTO_PATH ;
			File dir = new File(path);
			if(!dir.exists()){
				dir.mkdirs();
			}
			String photoUrl =  path + mFile.getOriginalFilename();
			fos = new FileOutputStream(photoUrl);
			int len = 0;
			byte[] buf = new byte[1024*1024];
			while((len = is.read(buf))>-1){
				fos.write(buf, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new AppResp(false,CodeDef.ERROR);
		}finally {
			try {
				fos.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
				return new AppResp(false,CodeDef.ERROR);
			}
		}
		String photoUrl = getPortPath(url)+ Const.TL_PHOTO_PATH + mFile.getOriginalFilename();
		int rs = appUserRepo.updatePhoto(id,photoUrl);
		if(rs>0){
			return new AppResp(photoUrl, CodeDef.SUCCESS);
		}
		return new AppResp(CodeDef.ERROR);
	}
	
	private String getPortPath(String url){
		Pattern p = Pattern.compile("[a-zA-z]+://[^/]*");
		Matcher matcher = p.matcher(url);  
		if(matcher.find()){
			return matcher.group();  
		}
		return null;
	}
	
	private <T> T merge(T dbObj,T appObj) throws Exception{
		Field[] fields = dbObj.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			field.setAccessible(true);
			Object val = field.get(appObj);
			if(val!=null){
				field.set(dbObj, val);
			}
		}
		return dbObj;
	}
}
