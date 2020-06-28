package com.simonkim;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Vector;

public class PC {

  private static final String dataPCPath = "/Users/simonkim/OOAD-post/POST_datafile/OOAD_dataPC.txt"; // 본인의 환경에 맞게 설정
  private static final String pcPath = "/Users/simonkim/OOAD-post/POST_datafile/PC/"; // 본인의 환경에 맞게 설정
  // 멤버 변수
  private String[][] userPCs = null;
  private int chargeFee;

  PC() throws IOException {
    // dataPC파일과 PC파일 10개를 읽어 시스템에 저장한다.
    BufferedReader bufReader = new BufferedReader(new FileReader(dataPCPath));
    String fee = bufReader.readLine();
    if (fee != null) {
      chargeFee = Integer.parseInt(fee);
    } else {
      chargeFee = 0;
    }
    bufReader.close();

    userPCs = new String[10][2];
    for (int i = 1; i <= 10; i++) {
      bufReader = new BufferedReader(new FileReader(pcPath + i + ".txt"));
      userPCs[i - 1][0] = bufReader.readLine();
      userPCs[i - 1][1] = bufReader.readLine();
      bufReader.close();
    }
  }

  protected String[][] getUserPCs() {
    return userPCs;
  }

  protected int getChargeFee() {
    return chargeFee;
  }

  protected boolean changeFee(int price) throws IOException {
    // PC요금변경, 0원 이상이 입력되었는지 검증한 뒤, 멤버 변수와 데이터 파일에 반영한다.
    if (price >= 0) {
      chargeFee = price;
      PrintWriter dataFile = new PrintWriter(new FileWriter(dataPCPath));
      dataFile.println(chargeFee);
      dataFile.close();
      return true;
    }
    return false;
  }

  protected void writePCStatus(String status, String remainTime, String target) throws IOException {
    // 특정 번호의 PC파일에 데이터를 수정할 때 사용한다.
    PrintWriter statusFile = new PrintWriter(new FileWriter(pcPath + target + ".txt"));
    statusFile.println(status);
    statusFile.println(remainTime);
    statusFile.close();
  }

  protected Vector<Integer> deductionTime() throws IOException {
    // 분 단위 시간이 변경되었을 때 호출된다. 모든 PC의 시간을 1씩 감소시키고, 시간이 0이 된 PC는 벡터에 담아 종료된 PC목록으로 리턴한다.
    Vector<Integer> endedPCs = new Vector<Integer>();
    for (int i = 1; i <= 10; i++) {
      if (!userPCs[i - 1][1].equals("0") && userPCs[i - 1][0].equals("1")) {
        int remainTime = Integer.parseInt(userPCs[i - 1][1]);
        remainTime--;
        if (remainTime <= 0) {
          endedPCs.addElement(i);
          writePCStatus("0", "0", Integer.toString(i));
          userPCs[i - 1][0] = "0";
          userPCs[i - 1][1] = "0";
        } else {
          writePCStatus(userPCs[i - 1][0], Integer.toString(remainTime), Integer.toString(i));
          userPCs[i - 1][1] = Integer.toString(remainTime);
        }
      }
    }
    return endedPCs;
  }

  protected int calculatePrice(int pcTime) {
    // 한 시간 단위로 되어있는 PC이용 요금을 바탕으로 결제할 금액을 계산하여 리턴한다.
    return (pcTime * chargeFee / 60) / 10 * 10;
  }

  protected boolean completePay(int pcNum, int pcTime, int price, int receiveMoney)
      throws IOException {
    // 받은 금액이 결제할 금액보다 많은지 한번 더 확인한 뒤, 결제한 시간만큼 추가하여 시스템에 반영한다.
    if (price > receiveMoney) {
      return false;
    }
    userPCs[pcNum - 1][0] = "1";
    userPCs[pcNum - 1][1] = Integer.toString(Integer.parseInt(userPCs[pcNum - 1][1]) + pcTime);
    writePCStatus("1", userPCs[pcNum - 1][1], Integer.toString(pcNum));
    return true;
  }
}
