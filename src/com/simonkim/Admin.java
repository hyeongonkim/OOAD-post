// 충전(분 단위, 충전 대기 중인 PC만 리스팅), 상품 결제(복수 선택, 합산 금액과 거스름돈 계산까지)
// 1분 단위로 정확하게 새로고침
// 파워 종료 시 하루 총 매상액 계산
// UI를 어떻게 구현할 것인지(10대는 다 나오면서 동시에 상품도 나와야하고...)

package com.simonkim;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.io.*;

public class Admin {

  static String logPath = "/Users/simonkim/Desktop/OOAD_POST/OOAD_log.txt";
  static String dataPath = "/Users/simonkim/Desktop/OOAD_POST/OOAD_data.txt";
  static String pcPath = "/Users/simonkim/Desktop/OOAD_POST/PC/";

  static boolean power = false, orderAble = false, chargeAble = false;
  static String userPCs[][] = new String[10][2];
  static int chargeFee = 0;
  static int totalAmount = 0;
  static Map<String, Integer> menuTable = new HashMap<String, Integer>();

  static Date today = new Date();
  static SimpleDateFormat time = new SimpleDateFormat("yyyy/MM/dd a hh:mm:ss ");

  public static void main(String[] args) throws IOException {
    powerOn();
    init();
  }

  static void powerOn() throws IOException {
    power = true;
    writeLog("관리자 포스가 켜졌습니다.", logPath);
  }

  static void pawerOff() throws IOException {

  }

  static void init() throws IOException {
    writeLog("초기 환경설정 중입니다...", logPath);

    readData(dataPath);
    if (!chargeAble) {
      writeLog("!!!ERROR!!! 요금 데이터 로드에 실패하였습니다.", logPath);
    }
    if (!orderAble) {
      writeLog("!!!ERROR!!! 메뉴 데이터 로드에 실패하였습니다.", logPath);
    }
    if (chargeAble) {
      writeLog("시간당 이용 금액은 " + Integer.toString(chargeFee) + "원 입니다.", logPath);
    }
    if (orderAble) {
      writeLog("판매 메뉴는 " + String.valueOf(menuTable.keySet()) + "입니다.", logPath);
    }
    refreshPC(pcPath);
    writeLog("PC 10대 동기화가 완료되었습니다.", logPath);
    writeLog("초기 환경설정이 끝났습니다...", logPath);
  }

  static void refreshPC(String path) throws IOException {
    for (int i = 1; i <= 10; i++) {
      BufferedReader bufReader = new BufferedReader(
          new FileReader(path + Integer.toString(i) + ".txt"));
      userPCs[i - 1][0] = bufReader.readLine();
      userPCs[i - 1][1] = bufReader.readLine();
      bufReader.close();
    }
  }

  static void chargePC(String targetNum, String time, int receiveMoney) throws IOException {

  }

  static void orderMenu(String[] order, int receive) throws IOException {

  }

  static void writeLog(String input, String path) throws IOException {
    PrintWriter log = new PrintWriter(new FileWriter(path, true));
    String text = time.format(today) + input;
    log.println(text);
    System.out.println(text);
    log.close();
  }

  static void readData(String path) throws IOException {
    BufferedReader bufReader = new BufferedReader(new FileReader(path));
    String fee = bufReader.readLine();
    if (fee != null) {
      chargeFee = Integer.parseInt(fee);
      chargeAble = true;
    }
    while (true) {
      String menu = bufReader.readLine();
      if (menu == null) {
        break;
      }
      int price = Integer.parseInt(bufReader.readLine());
      menuTable.put(menu, price);
    }
    if (!menuTable.isEmpty()) {
      orderAble = true;
    }
    bufReader.close();
  }
}
