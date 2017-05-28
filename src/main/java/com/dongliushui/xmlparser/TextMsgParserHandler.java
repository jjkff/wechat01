package com.dongliushui.xmlparser;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.dongliushui.bean.TextMsg;
/** 
 * @ClassName: TextMsgParserHandler
 * @Description: 文本消息解析handler
 * @author zhutulang
 * @date 2016年1月3日
 * @version V1.0
 */
public class TextMsgParserHandler extends DefaultHandler {

	 private TextMsg textMsg;
	 //用来存放每次遍历后的元素名称(节点名称)  
	 private String tagName;
	
	 @Override  
	 public void startDocument() throws SAXException {  
		
	 }
	 
	 @Override  
	 public void startElement(String uri, String localName, String qName,  
	            Attributes attributes) throws SAXException {  
		 if(qName.equals("xml")){
			 textMsg = new TextMsg();
		 }
		 this.tagName = qName;
	 }
	 
	 @Override  
	 public void endElement(String uri, String localName, String qName)  
	 throws SAXException { 
		 this.tagName = null;
	 }
	 
	 @Override  
	 public void endDocument() throws SAXException {
		 
	 }
	 
	 @Override  
	 public void characters(char[] ch, int start, int length) throws SAXException {  
		 if(tagName != null){
			 String data = new String(ch,start,length);
			 if(tagName.equals("ToUserName")){
				 textMsg.setToUserName(data);
			 }else if(tagName.equals("FromUserName")){
				 textMsg.setFromUserName(data);
			 }else if(tagName.equals("CreateTime")){
				 textMsg.setCreateTime(data);
			 }else if(tagName.equals("MsgType")){
				 textMsg.setMsgType(data);
			 }else if(tagName.equals("Content")){
				 textMsg.setContent(data); 
			 }else if(tagName.equals("MsgId")){
				 textMsg.setMsgId(data);
			 }
		 }
	 }

	/**
	 * <p>Title: getTextMsg<／p>
	 * <p>Description: 解析返回TextMsg<／p>
	 * @param is
	 * @return
	 * @author zhutulang
	 * @version 1.0
	 */
	public TextMsg getTextMsg(InputStream is) {
		try {
			SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
			saxParser.parse(is, this);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return textMsg;
	}
}
