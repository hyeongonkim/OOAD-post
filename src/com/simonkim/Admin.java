package com.simonkim;

import java.io.*;
import java.util.Map;
import java.util.Vector;

public class Admin {

  private int totalSales;
  private PC pc = null;
  private Food food = null;

  Admin() throws IOException {
    // 새로운 인스턴스가 생성되면 하위 객체를 생성하고 결과를 로그로 출력한다.
    Logging.writeLog("판매 정보를 불러옵니다.");
    totalSales = 0;
    pc = new PC();
    Logging.writeLog("시간당 이용 금액은 " + pc.getChargeFee() + "원 입니다.");
    food = new Food();
    Logging.writeLog("판매 메뉴는 " + String.valueOf(food.getFoodTable().keySet()) + " 입니다.");
    Logging.writeLog("판매를 시작합니다.");
  }

  protected int getTotalSales() {
    return totalSales;
  }

  protected void addTotalSales(int price) {
    totalSales += price;
  }

  protected int calculatePrice(int type, int pcTime, Vector<String> order) {
    // PC충전인지, 상품판매인지 타입을 받아 적절한 메소드를 호출한다.
    if (type == 0) {
      return pc.calculatePrice(pcTime);
    } else {
      return food.calculatePrice(order);
    }
  }

  protected boolean completePay(int type, int pcNum, int pcTime, Vector<String> order, int price,
      int receiveMoney) throws IOException {
    // 최종 결제를 시도한다. 결제가 성공적이라면 총매출을 증가시키고 로그를 작성한다.
    boolean success;
    if (type == 0) {
      success = pc.completePay(pcNum, pcTime, price, receiveMoney);
      if(success)
        addTotalSales(price);
        Logging.writeLog("PC충전 [<" + pcNum + "번 PC, " + pcTime + "분> / " + receiveMoney + "원 수령 / "+ price + "원 결제 / " + (receiveMoney - price) + "원 거스름] 결제되었습니다.");
      return success;
    } else {
      success = food.completePay(order, price, receiveMoney);
      if(success) {
        addTotalSales(price);
        String menu = "";
        for (int i = 0; i < order.size() - 1; i++)
          menu += order.elementAt(i) + ", ";
        menu += order.elementAt(order.size() - 1);
        Logging.writeLog("상품 주문 [<" + menu + "> 주문 / " + receiveMoney + "원 수령 / " + price + "원 결제 / " + (receiveMoney - price) + "원 거스름] 결제되었습니다.");
      }
      return success;
    }
  }

  protected void deductionTime() throws IOException {
    // 시간을 감소시키는 메소드를 호출하고, 종료된 PC를 벡터로 받아 로그에 작성한다.
    Vector<Integer> endedPCs = pc.deductionTime();
    for(int i = 0; i < endedPCs.size(); i++)
      Logging.writeLog("PC " + endedPCs.elementAt(i) + "번이 시간 소진으로 사용 종료되었습니다.");
  }

  protected boolean changeFee(int price) throws IOException {
    // PC이용요금 변경을 시도하고 성공할 경우 로그를 작성한다.
    boolean success = pc.changeFee(price);
    if(success)
      Logging.writeLog("PC이용요금을 " + price + "원으로 변경하였습니다.");
    return success;
  }

  protected boolean addFood(String name, String price) throws IOException {
    // 신규 판매 상품을 추가한다.
    boolean success = food.addFood(name, price);
    if(success)
      Logging.writeLog("신규 메뉴 <" + name + ", " + price + "> 추가하였습니다.");
    return success;
  }

  protected boolean deleteFood(String name) throws IOException {
    // 기존 메뉴를 제거한다.
    boolean success = food.deleteFood(name);
    if(success)
      Logging.writeLog("기존 메뉴 <" + name + "> 삭제하였습니다.");
    return success;
  }

  protected String[][] getUserPCs() {
    return pc.getUserPCs();
  }

  protected Map getFoodTable() {
    return food.getFoodTable();
  }

  protected int getChargeFee() {
    return pc.getChargeFee();
  }

  protected void endSales() throws IOException {
    Logging.writeLog("판매를 종료합니다. 금번 매출은 " + getTotalSales() + "원 입니다.");
  }
}
