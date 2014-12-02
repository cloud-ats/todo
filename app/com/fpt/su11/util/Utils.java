package com.fpt.su11.util;

import play.Play;

public class Utils {

  public static String stackTraceToString(Throwable e) {
    StringBuilder sb = new StringBuilder();
    for (StackTraceElement element : e.getStackTrace()) {
      sb.append(element.toString());
      sb.append("\n");
    }
    return sb.toString();
  }

  public static String getProperty(String key, String _default) {
    String result ;
    try {
      result = Play.application().configuration().getString(key);
    } catch (Exception e) {
      // TODO: handle exception
      result=null;
    }
    return result == null ? _default : result;
  }
  public static Integer getProperty(String key, Integer _default) {
    Integer result;
    
    try {
      String temp = Play.application().configuration().getString(key);
      result=Integer.parseInt(temp);
    } catch (Exception e) {
      // TODO: handle exception
      result=null;
    }
    return result == null ? _default : result;
  }

}
