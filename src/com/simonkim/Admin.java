package com.simonkim;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.io.*;

public class Admin {

  String logPath = "/Users/simonkim/Desktop/OOAD_POST/OOAD_log.txt";
  String dataPath = "/Users/simonkim/Desktop/OOAD_POST/OOAD_data.txt";
  String pcPath = "/Users/simonkim/Desktop/OOAD_POST/PC/";

  boolean orderAble, chargeAble;
  String[][] userPCs;
  int chargeFee;
  int totalAmount;
  Map<String, Integer> menuTable;

  static Date today = new Date();
  static SimpleDateFormat time = new SimpleDateFormat("yyyy/MM/dd a hh:mm:ss ");

  Admin() throws IOException {
    orderAble = false;
    chargeAble = false;
    userPCs = new String[10][2];
    chargeFee = 0;
    totalAmount = 0;
    menuTable = new HashMap<String, Integer>();

    writeLog("관리자 포스가 켜졌습니다.", logPath);
  }

  void sellOff() throws IOException {
    //판매모드 종료, 작동 중 매상 출력
  }

  void init() throws IOException {
    writeLog("초기 환경설정을 시작합니다...", logPath);

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

  void refreshPC(String path) throws IOException {
    //리프레시하다가 남은시간 0이 되는 PC가 발생하면 즉각 사용종료 명령 호출 필요
    //writePCStatus("0", "0", Integer.toString(pcNum), pcPath); 사용종료
    for (int i = 1; i <= 10; i++) {
      BufferedReader bufReader = new BufferedReader(
          new FileReader(path + Integer.toString(i) + ".txt"));
      userPCs[i - 1][0] = bufReader.readLine();
      userPCs[i - 1][1] = bufReader.readLine();
      bufReader.close();
    }
  }

  void chargePC(String targetNum, String time, int receiveMoney) throws IOException {
    //요금 충전, 시작시간과 남은시간 기록 필요
  }

  void orderMenu(String[] order, int receive) throws IOException {
    //상품 판매, 주문을 연속해서 받고 결제버튼을 누르면 합산 가격 출력 및 결제(receive로 받은 현금, 거스름돈 출력)
  }

  static void writeLog(String input, String path) throws IOException {
    PrintWriter log = new PrintWriter(new FileWriter(path, true));
    String text = time.format(today) + input;
    log.println(text);
    System.out.println(text);
    log.close();
  }

  void readData(String path) throws IOException {
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

  void PCcharged(int pay) throws IOException {
    for (int i = 1; i <= 10; i++) {
      BufferedReader bufReader = new BufferedReader(
          new FileReader(pcPath + Integer.toString(i) + ".txt"));
      String status = bufReader.readLine();
      bufReader.close();
      if (status.equals("0")) {
        String chargedTime = Integer.toString((int)((double)pay/chargeFee*60));
        writePCStatus("1", chargedTime, Integer.toString(i), pcPath);
        writeLog(Integer.toString(i) + "번 PC에 " + chargedTime + "분을 충전하였습니다. 사용을 시작합니다.", logPath);
        break;
      }
    }
  }

  void writePCStatus(String status, String remainTime, String pcNum, String path)
      throws IOException {
    PrintWriter statusFile = new PrintWriter(new FileWriter(path + pcNum + ".txt"));
    statusFile.println(status);
    statusFile.println(remainTime);
    statusFile.close();
  }
}
