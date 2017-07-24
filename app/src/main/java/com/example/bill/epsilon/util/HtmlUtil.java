package com.example.bill.epsilon.util;

/**
 * Created by Bill on 2017/7/16.
 */

public class HtmlUtil {
  private static final String TAG = "HtmlUtil";

  public static String removeP(String html) {
    String result = html;
    if (result.contains("<p>") && result.contains("</p>")) {
      result = result.replace("<p>", "");
      result = result.replace("</p>", "<br>");
      if (result.endsWith("<br>")) {
        result = result.substring(0, result.length() - 4);
      }
    }
    return result;
  }
}
