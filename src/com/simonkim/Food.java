package com.simonkim;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Food {

  private static String dataFoodPath = "/Users/simonkim/OOAD-post/POST_datafile/OOAD_dataFood.txt"; // 본인의 환경에 맞게 설정
  // 멤버 변수
  private Map<String, Integer> menuTable = null;

  Food() throws IOException {
    // dataFood 파일을 열어 판매 메뉴를 시스템에 로드한다.
    menuTable = new HashMap<String, Integer>();
    BufferedReader bufReader = new BufferedReader(new FileReader(dataFoodPath));
    while (true) {
      String menu = bufReader.readLine();
      if (menu == null) {
        break;
      }
      int price = Integer.parseInt(bufReader.readLine());
      menuTable.put(menu, price);
    }
    bufReader.close();
  }

  protected Map getFoodTable() {
    return menuTable;
  }

  protected void saveFoodTable() throws IOException {
    // 현재 시스템의 Map타입 멤버변수를 데이터 파일에 저장한다.
    PrintWriter dataFile = new PrintWriter(new FileWriter(dataFoodPath));
    menuTable.forEach((key, value) ->
    {
      dataFile.println(key);
      dataFile.println(value);
    });
    dataFile.close();
  }

  protected int calculatePrice(Vector<String> order) {
    // 주문한 메뉴 리스트를 받아 가격을 합산 계산하여 리턴한다.
    int price = 0;
    for (int i = 0; i < order.size(); i++) {
      price += menuTable.get(order.elementAt(i));
    }
    return price;
  }

  protected boolean completePay(Vector<String> order, int price, int receiveMoney) {
    // 주문이 정상인지, 수령한 금액이 적절한지 확인 후 결제 성공여부를 리턴한다.
    if(price > receiveMoney) {
      return false;
    }
    for (int i = 0; i < order.size(); i++) {
      if (!menuTable.containsKey(order.elementAt(i))) {
        return false;
      }
    }
    return true;
  }

  protected boolean addFood(String name, String price) throws IOException {
    // 신규 메뉴를 추가한다. 기존과 중복되지 않는 이름이라면 멤버 변수에 추가하고 데이터 파일에 저장한다.
    if(menuTable.containsKey(name))
      return false;
    menuTable.put(name, Integer.parseInt(price));
    saveFoodTable();
    return true;
  }

  protected boolean deleteFood(String name) throws IOException {
    // 기존 메뉴를 삭제한다. 기 등록된 이름과 동일한 경우 해당 키값을 찾아 제거하고 데이터 파일에 저장한다.
    if(!menuTable.containsKey(name))
      return false;
    menuTable.remove(name);
    saveFoodTable();
    return true;
  }
}
