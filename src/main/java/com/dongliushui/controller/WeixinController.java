package com.dongliushui.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dongliushui.bean.TextMsg;
import com.dongliushui.util.SignUtil;
import com.dongliushui.xmlparser.TextMsgParserHandler;
import com.dongliushui.xmlwriter.TextMsgWriter;

/**
 * @ClassName: WeixinController
 * @Description: 响应Controller
 * @author zhutulang
 * @date 2016年1月4日
 * @version V1.0
 */
@Controller  
@RequestMapping("/weixinCon") 
public class WeixinController {
	
	 private Logger log = Logger.getLogger(WeixinController.class);
	 
	 @RequestMapping(method = RequestMethod.GET)  
	 public void get(HttpServletRequest request, HttpServletResponse response) {  
		    log.info("请求进来了...");
	        // 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。  
	        String signature = request.getParameter("signature");  
	        // 时间戳  
	        String timestamp = request.getParameter("timestamp");  
	        // 随机数  
	        String nonce = request.getParameter("nonce");  
	        // 随机字符串  
	        String echostr = request.getParameter("echostr");  
	  
	        PrintWriter out = null;  
	        try {  
	            out = response.getWriter();  
	            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，否则接入失败  
	            if (SignUtil.checkSignature(signature, timestamp, nonce)) {  
	                out.print(echostr);  
	            } 
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        } finally {  
	            out.close();  
	            out = null;  
	        }  
	    }  
	  
	    @RequestMapping(method = RequestMethod.POST)  
	    public void post(HttpServletRequest request, HttpServletResponse response) {
	    	try {
				request.setCharacterEncoding("UTF-8");
				response.setCharacterEncoding("UTF-8");
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	    	PrintWriter out = null;  
	        try { 
	        	out = response.getWriter();  
	        	String responseMsg = null;
	          
	        	TextMsgParserHandler textMsgParserHandler = new TextMsgParserHandler();
	        	TextMsg textMsg = textMsgParserHandler.getTextMsg(request.getInputStream());
	        	String content = textMsg.getContent();
	        	log.info("收到的文本信息是："+content);
	        	if(content.equals("1")){
	        		responseMsg = "you are the only one!";
	        	}else{
	        		responseMsg = "你发给我的信息是："+content;
	        	}
	            textMsg.setContent(responseMsg);
	            
	            //注意这里发送者和接收者交换！！！
	            String fromUserName = textMsg.getFromUserName();
	            String toUserName = textMsg.getToUserName();
	            textMsg.setFromUserName(toUserName);
	            textMsg.setToUserName(fromUserName);
	            
	            //将textMsg 转换成xml字符串
	            String responseXml = TextMsgWriter.getXmlString(textMsg);
	        	out.print(responseXml);
	            log.info("response="+responseXml);   
	        } catch (Exception e) {
	            e.printStackTrace();  
	        } finally {  
	            out.close();  
	            out = null;  
	        }  
	    }  
}
