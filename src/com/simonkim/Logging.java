package com.simonkim;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

public class Logging {

  private static final String logPath = "/Users/simonkim/OOAD-post/POST_datafile/OOAD_log.txt"; // 본인의 환경에 맞게 설정
  private static final SimpleDateFormat logTimeFormat = new SimpleDateFormat("yyyy/MM/dd a hh:mm:ss "); // 로그 시간 포맷

  protected static void writeLog(String event) throws IOException {
    // 현재 시스템의 시간과 이벤트 내용을 합쳐 로그파일에 덧붙인다.
    long nowTime = System.currentTimeMillis();
    PrintWriter log = new PrintWriter(new FileWriter(logPath, true));
    String text = logTimeFormat.format(nowTime) + event;
    log.println(text);
    System.out.println(text);
    log.close();
  }
}