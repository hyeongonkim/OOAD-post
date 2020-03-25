// 1분 단위로 파일의 남은 시간(분 단위)을 수정할 것
// 남은 시간이 모두 동나면 powerOff를 호출할 것
// 남은 시간 표시할 것(on CUI)

package com.simonkim;

import java.io.*;

public class UserPC {

  static String pcPath = "/Users/simonkim/Desktop/OOAD_POST/PC/";

  static int pcNum = 0;
  static boolean power = false;
  static int remainMinutes = 0;

  public static void main(String[] args) throws IOException {
    powerOn(pcPath);
    powerOff(pcPath);
  }

  static void powerOn(String path) throws IOException {
    power = true;
    for (int i = 1; i <= 10; i++) {
      BufferedReader bufReader = new BufferedReader(
          new FileReader(path + Integer.toString(i) + ".txt"));
      String status = bufReader.readLine();
      bufReader.close();
      if (status.equals("0")) {
        pcNum = i;
        writePCStatus("1", "0", Integer.toString(pcNum), pcPath);
        System.out.println("귀하의 PC번호는 " + Integer.toString(pcNum) + "번 입니다.\n카운터에서 요금을 충전해주세요.");
        break;
      }
    }
  }

  static void powerOff(String path) throws IOException {
    writePCStatus("0", "0", Integer.toString(pcNum), pcPath);
    System.out.println("사용을 종료합니다. 안녕히가세요.");
  }

  static void writePCStatus(String status, String remainTime, String pcNum, String path)
      throws IOException {
    PrintWriter statusFile = new PrintWriter(new FileWriter(path + pcNum + ".txt"));
    statusFile.println(status);
    statusFile.println(remainTime);
    statusFile.close();
  }
}
