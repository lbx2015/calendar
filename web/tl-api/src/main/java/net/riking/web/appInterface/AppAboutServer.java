package net.riking.web.appInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import net.riking.config.CodeDef;
import net.riking.config.Const;
import net.riking.entity.AppResp;
/**
 * app关于的html5页面跳转
 * @author you.fei
 * @version crateTime：2017年8月10日 下午7:06:05
 * @used TODO
 */
@RestController
public class AppAboutServer {
	
//	@ApiOperation(value = "跳转<关于>html5页面", notes = "POST")
//	@RequestMapping(value = "/aboutApp", method = RequestMethod.POST)	
//	public void aboutApp(HttpServletResponse response) {
//		try {
//			FileInputStream fis = new FileInputStream(new File(this.getClass().getResource("/").getPath()+Const.TL_STATIC_HTML5_ABOUT_PATH));
//			StringBuilder sb = new StringBuilder();
//			byte [] buf = new byte[1024*1024];
//			int len = 0;
//			while((len=fis.read(buf))>-1){
//				buf = Arrays.copyOfRange(buf, 0, len);
//				sb.append(new String(buf,"UTF-8"));
//			}
//			fis.close();
//			System.err.println(sb.toString());
//			response.getWriter().write(sb.toString());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	
	@ApiOperation(value = "跳转<关于>html5页面", notes = "POST")
	@RequestMapping(value = "/aboutApp", method = RequestMethod.POST)	
	public AppResp aboutApp() {
		return new AppResp(Const.TL_ABOUT_HTML5_PATH,CodeDef.SUCCESS);
	}
	
	@ApiOperation(value = "跳转<app使用协议>html5页面", notes = "POST")
	@RequestMapping(value = "/agreementApp", method = RequestMethod.POST)	
	public AppResp agreementApp() {
		return new AppResp(Const.TL_AGREEMENT_HTML5_PATH,CodeDef.SUCCESS);
	}
}
