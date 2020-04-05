package com.simonkim;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.HashMap;
import java.io.*;

public class Admin {

  static String logPath = "/Users/simonkim/Desktop/OOAD_POST/OOAD_log.txt";
  String dataPath = "/Users/simonkim/Desktop/OOAD_POST/OOAD_data.txt";
  String pcPath = "/Users/simonkim/Desktop/OOAD_POST/PC/";

  boolean orderAble;
  String[][] userPCs;
  int chargeFee;
  int totalAmount;
  Map<String, Integer> menuTable;

  static SimpleDateFormat time = new SimpleDateFormat("yyyy/MM/dd a hh:mm:ss ");

  Admin() throws IOException {
    orderAble = false;
    userPCs = new String[10][2];
    chargeFee = 0;
    totalAmount = 0;
    menuTable = new HashMap<String, Integer>();

    writeLog("포스가 켜졌습니다.");
  }

  void init() throws IOException {
    writeLog("초기 환경설정을 시작합니다...");

    readData(dataPath);
    writeLog("시간당 이용 금액은 " + Integer.toString(chargeFee) + "원 입니다.");
    writeLog("판매 메뉴는 " + String.valueOf(menuTable.keySet()) + "입니다.");

    for (int i = 1; i <= 10; i++) {
      BufferedReader bufReader = new BufferedReader(
          new FileReader(pcPath + Integer.toString(i) + ".txt"));
      userPCs[i - 1][0] = bufReader.readLine();
      userPCs[i - 1][1] = bufReader.readLine();
      bufReader.close();
    }
    writeLog("PC 10대 동기화가 완료되었습니다.");
    writeLog("초기 환경설정이 끝났습니다...");
  }

  void refreshPC() throws IOException {
    for (int i = 1; i <= 10; i++) {
      if (!userPCs[i - 1][1].equals("0") && userPCs[i - 1][0].equals("1")) {
        int reTime = Integer.parseInt(userPCs[i - 1][1]);
        reTime--;
        if (reTime <= 0) {
          writeLog("PC " + Integer.toString(i) + "번이 시간 소진으로 사용 종료되었습니다.");
          writePCStatus("0", "0", Integer.toString(i), pcPath);
        } else {
          writePCStatus(userPCs[i - 1][0], Integer.toString(reTime), Integer.toString(i), pcPath);
        }
      }
      BufferedReader bufReader = new BufferedReader(
          new FileReader(pcPath + Integer.toString(i) + ".txt"));
      userPCs[i - 1][0] = bufReader.readLine();
      userPCs[i - 1][1] = bufReader.readLine();
      bufReader.close();
    }
  }

  int calChargePC(int time) {
    return (time * chargeFee / 60) / 10 * 10;
  }

  void chargePC(int target, int time, int receiveMoney) throws IOException {
    PCcharged(target, time); // 1원 단위 버림
    writeLog(
        Integer.toString(target) + "번 PC 충전 [" + Integer.toString((time * chargeFee / 60) / 10 * 10)
            + "원 수령 / " + Integer.toString((time * chargeFee / 60) / 10 * 10) + "원 결제 / " + Integer
            .toString(receiveMoney - (time * chargeFee / 60) / 10 * 10) + "원 거스름] 결제되었습니다.");
  }

  int calMenu(String[] order) throws IOException {
    //상품 판매, 주문을 연속해서 받고 결제버튼을 누르면 합산 가격 출력 및 결제(receive로 받은 현금, 거스름돈 출력)
    //상품 가액 계산
    int price = 0;
    for (int i = 0; i < order.length; i++) {
      price += menuTable.get(order[i]);
    }
    return price;
  }

  void payMenu(String[] order, int price, int receiveMoney) throws IOException {
    totalAmount += price;
    String menu = "";
    for (int i = 0; i < order.length - 1; i++) {
      menu += order[i] + ", ";
    }
    menu += order[order.length - 1];
    writeLog("상품 주문 [<" + menu + "> 주문 / " + Integer.toString(receiveMoney) + "원 수령 / " + Integer
        .toString(price) + "원 결제 / " + Integer.toString(receiveMoney - price) + "원 거스름] 결제되었습니다.");
  }

  static void writeLog(String input) throws IOException {
    long nowTime = System.currentTimeMillis();
    PrintWriter log = new PrintWriter(new FileWriter(logPath, true));
    String text = time.format(nowTime) + input;
    log.println(text);
    System.out.println(text);
    log.close();
  }

  void readData(String path) throws IOException {
    BufferedReader bufReader = new BufferedReader(new FileReader(path));
    String fee = bufReader.readLine();
    if (fee != null) {
      chargeFee = Integer.parseInt(fee);
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

  void writeData() throws IOException {
    PrintWriter statusFile = new PrintWriter(new FileWriter(dataPath));
    statusFile.println(chargeFee);
    menuTable.forEach((key, value) -> {
      statusFile.println(key);
      statusFile.println(value);
    });
    statusFile.close();
  }

  void PCcharged(int target, int time) throws IOException {

    BufferedReader bufReader = new BufferedReader(
        new FileReader(pcPath + Integer.toString(target) + ".txt"));
    bufReader.readLine();
    int nowTime = Integer.parseInt(bufReader.readLine());
    bufReader.close();

    totalAmount += (time * chargeFee / 60) / 10 * 10;
    String chargedTime = Integer.toString(nowTime + time);
    userPCs[target - 1][0] = "1";
    userPCs[target - 1][1] = chargedTime;
    writePCStatus("1", chargedTime, Integer.toString(target), pcPath);
    writeLog(Integer.toString(target) + "번 PC에 " + time + "분을 충전하였습니다. (" + Integer
        .toString((time * chargeFee / 60) / 10 * 10) + "원)");
  }

  void writePCStatus(String status, String remainTime, String pcNum, String path)
      throws IOException {
    PrintWriter statusFile = new PrintWriter(new FileWriter(path + pcNum + ".txt"));
    statusFile.println(status);
    statusFile.println(remainTime);
    statusFile.close();
  }
}
