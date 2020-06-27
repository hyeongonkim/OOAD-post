package com.simonkim;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

public class Logging {

  private static String logPath = "/Users/simonkim/OOAD-post/POST_datafile/OOAD_log.txt";
  private static SimpleDateFormat logTimeFormat = new SimpleDateFormat("yyyy/MM/dd a hh:mm:ss ");

  protected static void writeLog(String event) throws IOException {
    long nowTime = System.currentTimeMillis();
    PrintWriter log = new PrintWriter(new FileWriter(logPath, true));
    String text = logTimeFormat.format(nowTime) + event;
    log.println(text);
    System.out.println(text);
    log.close();
  }
}