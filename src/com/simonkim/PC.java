package com.simonkim;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Vector;

public class PC {

  private static String dataPCPath = "/Users/simonkim/OOAD-post/POST_datafile/OOAD_dataPC.txt";
  private static String pcPath = "/Users/simonkim/OOAD-post/POST_datafile/PC/";
  private String[][] userPCs = null;
  private int chargeFee;

  PC() throws IOException {
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
    PrintWriter statusFile = new PrintWriter(new FileWriter(pcPath + target + ".txt"));
    statusFile.println(status);
    statusFile.println(remainTime);
    statusFile.close();
  }

  protected Vector<Integer> deductionTime() throws IOException {
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
    return (pcTime * chargeFee / 60) / 10 * 10;
  }

  protected boolean completePay(int pcNum, int pcTime, int price, int receiveMoney)
      throws IOException {
    if (price > receiveMoney) {
      return false;
    }
    userPCs[pcNum - 1][0] = "1";
    userPCs[pcNum - 1][1] = Integer.toString(Integer.parseInt(userPCs[pcNum - 1][1]) + pcTime);
    writePCStatus("1", userPCs[pcNum - 1][1], Integer.toString(pcNum));
    return true;
  }
}
