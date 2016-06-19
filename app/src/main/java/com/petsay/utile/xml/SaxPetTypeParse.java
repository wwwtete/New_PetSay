package com.petsay.utile.xml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.content.Context;

import com.petsay.vo.user.PetType;

public class SaxPetTypeParse {
//	private static SaxPetTypeParse _instance;
//	public static SaxPetTypeParse getSingleton(){
//		if (null==_instance) {
//			_instance=new SaxPetTypeParse();
//		}
//		return _instance;
//	}

	public List<PetType> parse(InputStream is) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();  //取得SAXParserFactory实例  
        SAXParser parser = factory.newSAXParser();                  //从factory获取SAXParser实例  
        MyHandler handler = new MyHandler();                        //实例化自定义Handler  
        parser.parse(is, handler);                                  //根据自定义Handler规则解析输入流  
        return handler.getPetTypes();  
	}
	
	public static List<PetType> parse(Context context) throws Exception {
		InputStream is=context.getResources().getAssets().open("petCategory.xml");
		SAXParserFactory factory = SAXParserFactory.newInstance();  //取得SAXParserFactory实例  
        SAXParser parser = factory.newSAXParser();                  //从factory获取SAXParser实例  
        MyHandler handler = new MyHandler();                        //实例化自定义Handler  
        parser.parse(is, handler);                                  //根据自定义Handler规则解析输入流  
        return handler.getPetTypes();  
	}

	
	//需要重写DefaultHandler的方法  
    private static class MyHandler extends DefaultHandler {  
  
        private List<PetType> petTypes;  
        private PetType petType;  
        private StringBuilder builder;  
        private List<String> petNames;
        private List<Integer> petIds;
        private boolean isNameNode=false;
          
        public List<PetType> getPetTypes() { 
//        	for (int i = 0; i < petTypes.size(); i++) {
////        		System.out.println("size:"+ petTypes.size());
//        		PetType petType=petTypes.get(i);
////        		System.err.println("typeName:"+petType.getTypeName()+"   index:"+petType.getTypeId());
////        		for (int j = 0; j <petType.getId().length; j++) {
////					System.err.println("     id:"+petType.getId()[j]+"   name:"+petType.getName()[j]);
////				}
//			}
            return petTypes;  
        }  
        
        @Override  
        public void startDocument() throws SAXException {  
            super.startDocument();  
            petTypes = new ArrayList<PetType>();  
//            builder = new StringBuilder();  
        }  
          
        @Override  
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {  
            super.startElement(uri, localName, qName, attributes);  
            
//            System.out.println("localName:"+localName+"   qName:"+qName);
            if (localName.equals("node")) {  
            	isNameNode=false;
            	 petType = new PetType();  
            	 petNames=new ArrayList<String>();
            	 petIds=new ArrayList<Integer>();
            	petType.setTypeId(Integer.parseInt(attributes.getValue("index")));
            	petType.setTypeName(attributes.getValue("name"));
//            	System.out.println("length:"+attributes.get);
//            
            	
            }else if (localName.equals("name")) {
            	isNameNode=true;
//        		System.out.println("value:"+attributes.getLocalName(0));
            	petIds.add(Integer.parseInt(attributes.getValue("id")));
            	
			}
//            builder.setLength(0);   //将字符长度设置为0 以便重新开始读取元素内的字符节点  
        }  
          
        @Override  
        public void characters(char[] ch, int start, int length) throws SAXException {  
            super.characters(ch, start, length);  
            String string=new String(ch, start, length);
            if (!string.trim().equals("")) {
//            	 builder.append(ch, start, length);  //将读取的字符数组追加到builder中  
                 if (isNameNode) {
                 	 petNames.add(new String(ch, start, length));
     			}
			}
        }  
          
        @Override  
        public void endElement(String uri, String localName, String qName) throws SAXException {  
            super.endElement(uri, localName, qName);  
            if (localName.equals("node")) {
            	int len=petIds.size();
            	int[] petIdArray=new int[len];
            	String[] petNameArray=new String[len];
            	for (int j = 0; j < len; j++) {
            		petIdArray[j]=petIds.get(j);
            		petNameArray[j]=petNames.get(j);
//					System.err.println(""+petIds.get(j)+"   "+petNames.get(j));
				}
            	petType.setId(petIdArray);
            	petType.setName(petNameArray);
            	petTypes.add(petType);
//            	PetType tempPetType=petType;
			}
     
        }  
        
        @Override
        public void endDocument() throws SAXException {
        	super.endDocument();
        	
        }
    }  
} 

