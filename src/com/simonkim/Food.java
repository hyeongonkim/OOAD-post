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

  private static String dataFoodPath = "/Users/simonkim/OOAD-post/POST_datafile/OOAD_dataFood.txt";
  private Map<String, Integer> menuTable;

  Food() throws IOException {
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
    PrintWriter dataFile = new PrintWriter(new FileWriter(dataFoodPath));
    menuTable.forEach((key, value) ->
    {
      dataFile.println(key);
      dataFile.println(value);
    });
    dataFile.close();
  }

  protected int calculatePrice(Vector<String> order) {
    int price = 0;
    for (int i = 0; i < order.size(); i++) {
      price += menuTable.get(order.elementAt(i));
    }
    return price;
  }

  protected boolean completePay(Vector<String> order, int price, int receiveMoney) {
    for (int i = 0; i < order.size(); i++) {
      if (!menuTable.containsKey(order.elementAt(i))) {
        return false;
      }
    }
    return true;
  }

  protected boolean addFood(String name, String price) throws IOException {
    menuTable.put(name, Integer.parseInt(price));
    saveFoodTable();
    return true;
  }

  protected boolean deleteFood(String name) throws IOException {
    menuTable.remove(name);
    saveFoodTable();
    return true;
  }
}
