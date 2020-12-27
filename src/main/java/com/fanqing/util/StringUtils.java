package com.fanqing.util;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.springframework.util.StringUtils{

    /**
     * A String for a space character.
     *
     * @since 3.2
     */
    public static final String SPACE = " ";

    /**
     * The empty String {@code ""}.
     * @since 2.0
     */
    public static final String EMPTY = "";
    
    /** 
     * 转换为下划线 
     *  
     * @param camelCaseName 
     * @return 
     */  
    public static String underscoreName(String camelCaseName) {  
        StringBuilder result = new StringBuilder();  
        if (camelCaseName != null && camelCaseName.length() > 0) {  
            result.append(camelCaseName.substring(0, 1).toLowerCase());  
            for (int i = 1; i < camelCaseName.length(); i++) {  
                char ch = camelCaseName.charAt(i);  
                if (Character.isUpperCase(ch)) {  
                    result.append("_");  
                    result.append(Character.toLowerCase(ch));  
                } else {  
                    result.append(ch);  
                }  
            }  
        }  
        return result.toString();  
    }  
  
    /** 
     * 转换为驼峰 
     *  
     * @param underscoreName 
     * @return 
     */  
    public static String camelCaseName(String underscoreName) {  
        StringBuilder result = new StringBuilder();  
        if (underscoreName != null && underscoreName.length() > 0) {  
            boolean flag = false;  
            for (int i = 0; i < underscoreName.length(); i++) {  
                char ch = underscoreName.charAt(i);  
                if ("_".charAt(0) == ch) {  
                    flag = true;  
                } else {  
                    if (flag) {  
                        result.append(Character.toUpperCase(ch));  
                        flag = false;  
                    } else {  
                        result.append(ch);  
                    }  
                }  
            }  
        }  
        return result.toString();  
    }

    /**
     * 去除重复相邻空格
     * @param str "AA  B CC    DD  EE"
     * @return "AA B CC DD EE"
     */
    public static String dealStrSpace(String str) {
        str = str.trim();
        int index = -1;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if(c == ' ') {
                if(index > 0 && index == i - 1) {
                    index = i;
                    continue;
                }
                index = i;
            }
            sb.append(c);
        }
        return sb.toString();
    }
    
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isBlank(String str) {
        return !hasText(str);
    }

    public static boolean isNotBlank(String str) {
        return hasText(str);
    }
    
    public static String etn(String str) {
        return emptyToNull(str);
    }
    
    public static String emptyToNull(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }

        return str;
    }

    public static String nte(String str) {
        return nullToEmpty(str);
    }
    
    public static String nullToEmpty(String str) {
        if (null == str) {
            return "";
        }
        return str;
    }

    public static String trim(String string) {
        return trimWhitespace(string);
    }
    
    /**
     * 是否包含中文
     * */
    public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
    
    public static byte[] getBytes(String content, String charset)
    {
      if (isNull(content)) {
        content = "";
      }
      if (isBlank(charset))
        throw new IllegalArgumentException("charset can not null");
      try
      {
        return content.getBytes(charset); } catch (UnsupportedEncodingException e) {
      }
      throw new RuntimeException("charset is not valid,charset is:" + charset);
    }

    public static byte[] getBytes(String content)
    {
      if (isNull(content)) {
        content = "";
      }
      return content.getBytes();
    }

    public static String getString(byte[] binaryData, String charset) {
      try {
        return new String(binaryData, charset); } catch (UnsupportedEncodingException e) {
      }
      throw new RuntimeException("charset is not valid,charset is:" + charset);
    }
    
    public static boolean isNotNull(String str)
    {
      return str != null;
    }

    public static boolean isNull(String str)
    {
      return str == null;
    }
    
    public static String[] split(String toSplit, String delimiter) {
        return StringUtils.split(toSplit, delimiter);
    }
    
}
