package com.simonkim;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.swing.*;

public class GUIproc {

  static int forChargePCNum = 0;
  static int forChargePCTime = 0;

  static Admin post;

  static {
    try {
      post = new Admin();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static JLabel pc01_Time;
  static JLabel pc02_Time;
  static JLabel pc03_Time;
  static JLabel pc04_Time;
  static JLabel pc05_Time;
  static JLabel pc06_Time;
  static JLabel pc07_Time;
  static JLabel pc08_Time;
  static JLabel pc09_Time;
  static JLabel pc10_Time;

  static Vector<String> order = new Vector<String>();

  static String[][] menu = new String[12][2];

  static SimpleDateFormat time = new SimpleDateFormat("mm");

  public static void main(String[] args) throws IOException {
    JFrame frame = new JFrame("PC cafe");
    frame.setSize(900, 600);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel chargePage = new JPanel();
    JPanel mainPage = new JPanel();
    JPanel payPage = new JPanel();
    JPanel sellPage = new JPanel();
    JPanel chgPCPayPage = new JPanel();
    JPanel salesPage = new JPanel();
    JPanel menuPage = new JPanel();
    JPanel payMenuPage = new JPanel();
    JPanel chgMenuPage = new JPanel();

    JLabel orderList = new JLabel("<html></html>");

    // 상품관리 패널

    chgMenuPage.setLayout(new GridLayout(7, 3));

    chgMenuPage.add(new JLabel("추가/삭제할 상품 정보를 입력하고 버튼을 누르세요. 삭제의 경우 상품명만 맞게 적으면 됩니다.", SwingConstants.CENTER));

    JPanel nameP = new JPanel();
    nameP.setLayout(new BoxLayout(nameP, BoxLayout.X_AXIS));
    nameP.add(new JLabel("추가/삭제할 상품이름 : "));
    JTextField menuName = new JTextField();
    nameP.add(menuName);

    JPanel priceP = new JPanel();
    priceP.setLayout(new BoxLayout(priceP, BoxLayout.X_AXIS));
    priceP.add(new JLabel("추가/삭제할 상품가격 : "));
    JTextField menuPrice = new JTextField();
    priceP.add(menuPrice);

    chgMenuPage.add(nameP);
    chgMenuPage.add(priceP);

    JButton addMenu = new JButton("추가하기");
    chgMenuPage.add(addMenu);

    addMenu.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!menuName.getText().equals("") && Integer.parseInt(menuPrice.getText()) >= 0) {
          post.menuTable.put(menuName.getText(), Integer.parseInt(menuPrice.getText()));
          try {
            post.writeData();
            Admin.writeLog("신규 메뉴 <" + menuName.getText() + ", " + menuPrice.getText() + "> 를 추가하였습니다.");
          } catch (IOException ex) {
            ex.printStackTrace();
          }
          menuName.setText("");
          menuPrice.setText("");
          frame.remove(chgMenuPage);
          frame.add(mainPage, BorderLayout.CENTER);
          frame.revalidate();
          frame.repaint();
        }
      }
    });

    JButton deleteMenu = new JButton("삭제하기");
    chgMenuPage.add(deleteMenu);

    deleteMenu.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(post.menuTable.containsKey(menuName.getText())) {
          String toDelete = menuName.getText();
          int deletePrice = post.menuTable.get(toDelete);
          try {
            post.menuTable.remove(toDelete);
            post.writeData();
            Admin.writeLog(
                "기존 메뉴 <" + toDelete + ", " + Integer.toString(deletePrice) + "> 를 삭제하였습니다.");
          } catch (IOException ex) {
            ex.printStackTrace();
          }
          menuName.setText("");
          menuPrice.setText("");
          frame.remove(chgMenuPage);
          frame.add(mainPage, BorderLayout.CENTER);
          frame.revalidate();
          frame.repaint();
        }
      }
    });

    JButton returnMenuChangeMain = new JButton("돌아가기");
    chgMenuPage.add(returnMenuChangeMain);

    returnMenuChangeMain.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        menuName.setText("");
        menuPrice.setText("");
        frame.remove(chgMenuPage);
        frame.add(mainPage, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
      }
    });

    // 상품관리 패널 끝

    // 상품결제 패널

    payMenuPage.setLayout(new GridLayout(7, 3));

    JPanel forMenuPay = new JPanel();
    forMenuPay.setLayout(new BoxLayout(forMenuPay, BoxLayout.X_AXIS));
    forMenuPay.add(new JLabel("결제할 금액 : "));
    JLabel payMenuMoney = new JLabel("");
    forMenuPay.add(payMenuMoney);

    JPanel recMenuPay = new JPanel();
    recMenuPay.setLayout(new BoxLayout(recMenuPay, BoxLayout.X_AXIS));
    recMenuPay.add(new JLabel("수령한 금액 : "));
    JTextField recMenuMoney = new JTextField();
    recMenuPay.add(recMenuMoney);

    payMenuPage.add(forMenuPay);
    payMenuPage.add(recMenuPay);

    JButton calMenuPay = new JButton("수령확인 / 거스름계산");
    payMenuPage.add(calMenuPay);

    JPanel chgMenuPay = new JPanel();
    chgMenuPay.setLayout(new BoxLayout(chgMenuPay, BoxLayout.X_AXIS));
    chgMenuPay.add(new JLabel("거스름 금액 : "));
    JLabel chgMenuMoney = new JLabel("");
    chgMenuPay.add(chgMenuMoney);
    payMenuPage.add(chgMenuPay);

    JButton successMenuPay = new JButton("결제완료");
    successMenuPay.setEnabled(false);
    payMenuPage.add(successMenuPay);

    JButton Menucancel = new JButton("취소하기");
    payMenuPage.add(Menucancel);

    Menucancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        payMenuMoney.setText("");
        recMenuMoney.setText("");
        chgMenuMoney.setText("");
        order.clear();
        successMenuPay.setEnabled(false);
        frame.remove(payMenuPage);
        frame.add(mainPage, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
      }
    });

    calMenuPay.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int requireMenuPay = Integer.parseInt(payMenuMoney.getText());
        int paidMenuPay = Integer.parseInt(recMenuMoney.getText());
        if (paidMenuPay < requireMenuPay) {
          chgMenuMoney.setText("금액부족, 결제불가능");
        } else {
          chgMenuMoney.setText(Integer.toString(paidMenuPay - requireMenuPay));
          successMenuPay.setEnabled(true);
        }
      }
    });

    successMenuPay.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(Integer.parseInt(recMenuMoney.getText()) >= Integer.parseInt(payMenuMoney.getText())) {
          try {
            post.payMenu(order, Integer.parseInt(payMenuMoney.getText()),
                Integer.parseInt(recMenuMoney.getText()));
          } catch (IOException ex) {
            ex.printStackTrace();
          }
          payMenuMoney.setText("");
          recMenuMoney.setText("");
          chgMenuMoney.setText("");
          order.clear();
          successMenuPay.setEnabled(false);
          frame.remove(payMenuPage);
          frame.add(mainPage, BorderLayout.CENTER);
          frame.revalidate();
          frame.repaint();
        }
      }
    });

    // 상품결제 패널 끝

    // 상품판매 패널

    menuPage.setLayout(new GridLayout(4, 4));

    JScrollPane scroller = new JScrollPane(orderList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    JButton menu1 = new JButton("");
    menuPage.add(menu1);
    menu1.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!menu1.getText().equals("")) {
          order.add(menu1.getText().split("<br>")[0].substring(6));
          orderList.setText(
              orderList.getText().substring(0, orderList.getText().length() - 7) + "<br>" + menu1
                  .getText().split("<br>")[0].substring(6) + "</html>");
        }

      }
    });
    JButton menu2 = new JButton("");
    menuPage.add(menu2);
    menu2.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!menu2.getText().equals("")) {
          order.add(menu2.getText().split("<br>")[0].substring(6));
          orderList.setText(
              orderList.getText().substring(0, orderList.getText().length() - 7) + "<br>" + menu2
                  .getText().split("<br>")[0].substring(6) + "</html>");
        }
      }
    });
    JButton menu3 = new JButton("");
    menuPage.add(menu3);
    menu3.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!menu3.getText().equals("")) {
          order.add(menu3.getText().split("<br>")[0].substring(6));
          orderList.setText(
              orderList.getText().substring(0, orderList.getText().length() - 7) + "<br>" + menu3
                  .getText().split("<br>")[0].substring(6) + "</html>");
        }
      }
    });
    JButton menu4 = new JButton("");
    menuPage.add(menu4);
    menu4.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!menu4.getText().equals("")) {
          order.add(menu4.getText().split("<br>")[0].substring(6));
          orderList.setText(
              orderList.getText().substring(0, orderList.getText().length() - 7) + "<br>" + menu4
                  .getText().split("<br>")[0].substring(6) + "</html>");
        }
      }
    });
    JButton menu5 = new JButton("");
    menuPage.add(menu5);
    menu5.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!menu5.getText().equals("")) {
          order.add(menu5.getText().split("<br>")[0].substring(6));
          orderList.setText(
              orderList.getText().substring(0, orderList.getText().length() - 7) + "<br>" + menu5
                  .getText().split("<br>")[0].substring(6) + "</html>");
        }
      }
    });
    JButton menu6 = new JButton("");
    menuPage.add(menu6);
    menu6.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!menu6.getText().equals("")) {
          order.add(menu6.getText().split("<br>")[0].substring(6));
          orderList.setText(
              orderList.getText().substring(0, orderList.getText().length() - 7) + "<br>" + menu6
                  .getText().split("<br>")[0].substring(6) + "</html>");
        }
      }
    });
    JButton menu7 = new JButton("");
    menuPage.add(menu7);
    menu7.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!menu7.getText().equals("")) {
          order.add(menu7.getText().split("<br>")[0].substring(6));
          orderList.setText(
              orderList.getText().substring(0, orderList.getText().length() - 7) + "<br>" + menu7
                  .getText().split("<br>")[0].substring(6) + "</html>");
        }
      }
    });
    JButton menu8 = new JButton("");
    menuPage.add(menu8);
    menu8.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!menu8.getText().equals("")) {
          order.add(menu8.getText().split("<br>")[0].substring(6));
          orderList.setText(
              orderList.getText().substring(0, orderList.getText().length() - 7) + "<br>" + menu8
                  .getText().split("<br>")[0].substring(6) + "</html>");
        }
      }
    });
    JButton menu9 = new JButton("");
    menuPage.add(menu9);
    menu9.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!menu9.getText().equals("")) {
          order.add(menu9.getText().split("<br>")[0].substring(6));
          orderList.setText(
              orderList.getText().substring(0, orderList.getText().length() - 7) + "<br>" + menu9
                  .getText().split("<br>")[0].substring(6) + "</html>");
        }
      }
    });
    JButton menu10 = new JButton("");
    menuPage.add(menu10);
    menu10.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!menu10.getText().equals("")) {
          order.add(menu10.getText().split("<br>")[0].substring(6));
          orderList.setText(
              orderList.getText().substring(0, orderList.getText().length() - 7) + "<br>" + menu10
                  .getText().split("<br>")[0].substring(6) + "</html>");
        }
      }
    });
    JButton menu11 = new JButton("");
    menuPage.add(menu11);
    menu11.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!menu11.getText().equals("")) {
          order.add(menu11.getText().split("<br>")[0].substring(6));
          orderList.setText(
              orderList.getText().substring(0, orderList.getText().length() - 7) + "<br>" + menu11
                  .getText().split("<br>")[0].substring(6) + "</html>");
        }
      }
    });
    JButton menu12 = new JButton("");
    menuPage.add(menu12);
    menu12.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!menu12.getText().equals("")) {
          order.add(menu12.getText().split("<br>")[0].substring(6));
          orderList.setText(
              orderList.getText().substring(0, orderList.getText().length() - 7) + "<br>" + menu12
                  .getText().split("<br>")[0].substring(6) + "</html>");
        }
      }
    });

    menuPage.add(scroller);

    JButton payOrder = new JButton("결제하기");
    menuPage.add(payOrder);
    payOrder.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(!orderList.equals("<html></html>")) {
          orderList.setText("<html></html>");
          try {
            payMenuMoney.setText(Integer.toString(post.calMenu(order)));
          } catch (IOException ex) {
            ex.printStackTrace();
          }
          frame.remove(menuPage);
          frame.add(payMenuPage, BorderLayout.CENTER);
          frame.revalidate();
          frame.repaint();
        }
      }
    });
    JButton manageOrder = new JButton("상품관리");
    menuPage.add(manageOrder);
    manageOrder.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        order.clear();
        orderList.setText("<html></html>");
        frame.remove(menuPage);
        frame.add(chgMenuPage, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
      }
    });
    JButton returnMenuMain = new JButton("돌아가기");
    menuPage.add(returnMenuMain);
    returnMenuMain.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        order.clear();
        orderList.setText("<html></html>");
        frame.remove(menuPage);
        frame.add(mainPage, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
      }
    });

    // 상품판매 패널 끝

    // 충전요금변경 패널

    chgPCPayPage.setLayout(new GridLayout(7, 3));

    JPanel nowPay = new JPanel();
    nowPay.setLayout(new BoxLayout(nowPay, BoxLayout.X_AXIS));
    nowPay.add(new JLabel("현재 이용 요금 : "));
    JLabel nowMoney = new JLabel("");
    nowPay.add(nowMoney);

    JPanel newPay = new JPanel();
    newPay.setLayout(new BoxLayout(newPay, BoxLayout.X_AXIS));
    newPay.add(new JLabel("바꿀 이용 요금 : "));
    JTextField newMoney = new JTextField();
    newPay.add(newMoney);

    chgPCPayPage.add(nowPay);
    chgPCPayPage.add(newPay);

    JButton changePay = new JButton("변경하기");
    chgPCPayPage.add(changePay);

    JButton returnChangeMain = new JButton("돌아가기");
    chgPCPayPage.add(returnChangeMain);

    returnChangeMain.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        newMoney.setText("");
        frame.remove(chgPCPayPage);
        frame.add(mainPage, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
      }
    });

    changePay.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (Integer.parseInt(newMoney.getText()) >= 0) {
          post.chargeFee = Integer.parseInt(newMoney.getText());
          try {
            post.writeData();
            Admin.writeLog("PC이용요금을 " + Integer.toString(post.chargeFee) + "원으로 변경하였습니다.");
          } catch (IOException ex) {
            ex.printStackTrace();
          }
          newMoney.setText("");
          frame.remove(chgPCPayPage);
          frame.add(mainPage, BorderLayout.CENTER);
          frame.revalidate();
          frame.repaint();
        }
      }
    });

    // 충전요금변경 패널 끝

    // 매출조회 패널

    salesPage.setLayout(new GridLayout(7, 3));

    JPanel nowSales = new JPanel();
    nowSales.setLayout(new BoxLayout(nowSales, BoxLayout.X_AXIS));
    nowSales.add(new JLabel("현재까지의 매출 : "));
    JLabel nowSalesMoney = new JLabel("");
    nowSales.add(nowSalesMoney);

    salesPage.add(nowSales);

    JButton returnSalesMain = new JButton("돌아가기");
    salesPage.add(returnSalesMain);

    returnSalesMain.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        nowSalesMoney.setText("");
        frame.remove(salesPage);
        frame.add(mainPage, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
      }
    });

    // 매출조회 패널 끝

    // 메인 패널

    mainPage.setLayout(new GridLayout(5, 5));

    mainPage.add(new JLabel("PC01", SwingConstants.CENTER));
    mainPage.add(new JLabel("PC02", SwingConstants.CENTER));
    mainPage.add(new JLabel("PC03", SwingConstants.CENTER));
    mainPage.add(new JLabel("PC04", SwingConstants.CENTER));
    mainPage.add(new JLabel("PC05", SwingConstants.CENTER));

    pc01_Time = new JLabel("null", SwingConstants.CENTER);
    mainPage.add(pc01_Time);
    pc02_Time = new JLabel("null", SwingConstants.CENTER);
    mainPage.add(pc02_Time);
    pc03_Time = new JLabel("null", SwingConstants.CENTER);
    mainPage.add(pc03_Time);
    pc04_Time = new JLabel("null", SwingConstants.CENTER);
    mainPage.add(pc04_Time);
    pc05_Time = new JLabel("null", SwingConstants.CENTER);
    mainPage.add(pc05_Time);

    mainPage.add(new JLabel("PC06", SwingConstants.CENTER));
    mainPage.add(new JLabel("PC07", SwingConstants.CENTER));
    mainPage.add(new JLabel("PC08", SwingConstants.CENTER));
    mainPage.add(new JLabel("PC09", SwingConstants.CENTER));
    mainPage.add(new JLabel("PC10", SwingConstants.CENTER));

    pc06_Time = new JLabel("null", SwingConstants.CENTER);
    mainPage.add(pc06_Time);
    pc07_Time = new JLabel("null", SwingConstants.CENTER);
    mainPage.add(pc07_Time);
    pc08_Time = new JLabel("null", SwingConstants.CENTER);
    mainPage.add(pc08_Time);
    pc09_Time = new JLabel("null", SwingConstants.CENTER);
    mainPage.add(pc09_Time);
    pc10_Time = new JLabel("null", SwingConstants.CENTER);
    mainPage.add(pc10_Time);

    JButton pcCharge = new JButton("PC충전");
    mainPage.add(pcCharge);
    JButton menuSell = new JButton("상품판매");
    mainPage.add(menuSell);
    JButton pcFeeChange = new JButton("PC요금변경");
    mainPage.add(pcFeeChange);
    JButton inquirySales = new JButton("매출조회");
    mainPage.add(inquirySales);
    JButton sellOff = new JButton("판매종료");
    mainPage.add(sellOff);

    pcCharge.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        frame.remove(mainPage);
        frame.add(chargePage, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
      }
    });

    menuSell.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        frame.remove(mainPage);
        frame.add(menuPage, BorderLayout.CENTER);
        String[] menus = post.menuTable.keySet().toArray(new String[0]);
        int i = 0;
        for (; i < menus.length; i++) {
          menu[i][0] = menus[i];
          menu[i][1] = Integer.toString(post.menuTable.get(menus[i]));
        }
        for (; i < 12; i++) {
          menu[i][0] = "";
          menu[i][1] = "";
        }
        if (!menu[0][0].equals("")) {
          menu1.setText("<html>" + menu[0][0] + "<br>" + menu[0][1] + "원</html>");
        } else {
          menu1.setText("<html></html>");
        }
        if (!menu[1][0].equals("")) {
          menu2.setText("<html>" + menu[1][0] + "<br>" + menu[1][1] + "원</html>");
        } else {
          menu2.setText("<html></html>");
        }
        if (!menu[2][0].equals("")) {
          menu3.setText("<html>" + menu[2][0] + "<br>" + menu[2][1] + "원</html>");
        } else {
          menu3.setText("<html></html>");
        }
        if (!menu[3][0].equals("")) {
          menu4.setText("<html>" + menu[3][0] + "<br>" + menu[3][1] + "원</html>");
        } else {
          menu4.setText("<html></html>");
        }
        if (!menu[4][0].equals("")) {
          menu5.setText("<html>" + menu[4][0] + "<br>" + menu[4][1] + "원</html>");
        } else {
          menu5.setText("<html></html>");
        }
        if (!menu[5][0].equals("")) {
          menu6.setText("<html>" + menu[5][0] + "<br>" + menu[5][1] + "원</html>");
        } else {
          menu6.setText("<html></html>");
        }
        if (!menu[6][0].equals("")) {
          menu7.setText("<html>" + menu[6][0] + "<br>" + menu[6][1] + "원</html>");
        } else {
          menu7.setText("<html></html>");
        }
        if (!menu[7][0].equals("")) {
          menu8.setText("<html>" + menu[7][0] + "<br>" + menu[7][1] + "원</html>");
        } else {
          menu8.setText("<html></html>");
        }
        if (!menu[8][0].equals("")) {
          menu9.setText("<html>" + menu[8][0] + "<br>" + menu[8][1] + "원</html>");
        } else {
          menu9.setText("<html></html>");
        }
        if (!menu[9][0].equals("")) {
          menu10.setText("<html>" + menu[9][0] + "<br>" + menu[9][1] + "원</html>");
        } else {
          menu10.setText("<html></html>");
        }
        if (!menu[10][0].equals("")) {
          menu11.setText("<html>" + menu[10][0] + "<br>" + menu[10][1] + "원</html>");
        } else {
          menu11.setText("<html></html>");
        }
        if (!menu[11][0].equals("")) {
          menu12.setText("<html>" + menu[11][0] + "<br>" + menu[11][1] + "원</html>");
        } else {
          menu12.setText("<html></html>");
        }

        frame.revalidate();
        frame.repaint();
      }
    });

    pcFeeChange.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        frame.remove(mainPage);
        frame.add(chgPCPayPage, BorderLayout.CENTER);
        nowMoney.setText(Integer.toString(post.chargeFee));
        frame.revalidate();
        frame.repaint();
      }
    });

    inquirySales.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        frame.remove(mainPage);
        frame.add(salesPage, BorderLayout.CENTER);
        nowSalesMoney.setText(Integer.toString(post.totalAmount));
        frame.revalidate();
        frame.repaint();
      }
    });

    sellOff.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          Admin.writeLog("판매를 종료합니다. 금번 매출은 " + Integer.toString(post.totalAmount) + "원 입니다.");
        } catch (IOException ex) {
          ex.printStackTrace();
        }
        System.exit(0);
      }
    });

    // 메인 패널 끝

    // 결제 패널

    payPage.setLayout(new GridLayout(7, 3));

    JPanel forPay = new JPanel();
    forPay.setLayout(new BoxLayout(forPay, BoxLayout.X_AXIS));
    forPay.add(new JLabel("결제할 금액 : "));
    JLabel payMoney = new JLabel("");
    forPay.add(payMoney);

    JPanel recPay = new JPanel();
    recPay.setLayout(new BoxLayout(recPay, BoxLayout.X_AXIS));
    recPay.add(new JLabel("수령한 금액 : "));
    JTextField recMoney = new JTextField();
    recPay.add(recMoney);

    payPage.add(forPay);
    payPage.add(recPay);

    JButton calPay = new JButton("수령확인 / 거스름계산");
    payPage.add(calPay);

    JPanel chgPay = new JPanel();
    chgPay.setLayout(new BoxLayout(chgPay, BoxLayout.X_AXIS));
    chgPay.add(new JLabel("거스름 금액 : "));
    JLabel chgMoney = new JLabel("");
    chgPay.add(chgMoney);
    payPage.add(chgPay);

    JButton successPay = new JButton("결제완료");
    successPay.setEnabled(false);
    payPage.add(successPay);

    JButton cancel = new JButton("취소하기");
    payPage.add(cancel);

    cancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        payMoney.setText("");
        recMoney.setText("");
        chgMoney.setText("");
        successPay.setEnabled(false);
        frame.remove(payPage);
        frame.add(mainPage, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
      }
    });

    calPay.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int requirePay = Integer.parseInt(payMoney.getText());
        int paidPay = Integer.parseInt(recMoney.getText());
        if (paidPay < requirePay) {
          chgMoney.setText("금액부족, 결제불가능");
        } else {
          chgMoney.setText(Integer.toString(paidPay - requirePay));
          successPay.setEnabled(true);
        }
      }
    });

    successPay.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(Integer.parseInt(recMoney.getText()) >= Integer.parseInt(payMoney.getText())) {
          try {
            post.chargePC(forChargePCNum, forChargePCTime, Integer.parseInt(recMoney.getText()));
            refreshMain(post, pc01_Time, pc02_Time, pc03_Time, pc04_Time, pc05_Time, pc06_Time,
                pc07_Time, pc08_Time, pc09_Time, pc10_Time);
            payMoney.setText("");
            recMoney.setText("");
            chgMoney.setText("");
            successPay.setEnabled(false);
            frame.remove(payPage);
            frame.add(mainPage, BorderLayout.CENTER);
            frame.revalidate();
            frame.repaint();
          } catch (IOException ex) {
            ex.printStackTrace();
          }
        }
      }
    });

    // 결제 패널 끝

    // 충전 패널

    chargePage.setLayout(new GridLayout(7, 3));

    JPanel pcNum = new JPanel();
    pcNum.setLayout(new BoxLayout(pcNum, BoxLayout.X_AXIS));
    pcNum.add(new JLabel("충전할 번호 : "));
    JTextField toNum = new JTextField();
    pcNum.add(toNum);

    JPanel pcTime = new JPanel();
    pcTime.setLayout(new BoxLayout(pcTime, BoxLayout.X_AXIS));
    pcTime.add(new JLabel("충전할 시간 : "));
    JTextField toTime = new JTextField();
    pcTime.add(toTime);

    chargePage.add(pcNum);
    chargePage.add(pcTime);

    JButton payment = new JButton("결제하기");
    chargePage.add(payment);

    JButton returnChargeMain = new JButton("돌아가기");
    chargePage.add(returnChargeMain);

    returnChargeMain.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        toNum.setText("");
        toTime.setText("");
        frame.remove(chargePage);
        frame.add(mainPage, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
      }
    });

    payment.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int temp1 = Integer.parseInt(toNum.getText());
        int temp2 = Integer.parseInt(toTime.getText());
        if (temp1 >= 1 && temp1 <= 10 && temp2 >= 1) {
          forChargePCNum = temp1;
          forChargePCTime = temp2;
          toNum.setText("");
          toTime.setText("");
          payMoney.setText(Integer.toString(post.calChargePC(forChargePCTime)));
          frame.remove(chargePage);
          frame.add(payPage, BorderLayout.CENTER);
          frame.revalidate();
          frame.repaint();
        }
      }
    });

    // 충전 패널 끝

    // 판매시작 패널

    sellPage.setLayout(new GridLayout(1, 1));

    JButton sellOn = new JButton("판매를 시작하려면 눌러주세요");
    sellPage.add(sellOn);

    sellOn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          post.init();
          Thread.sleep(500);
          refreshMain(post, pc01_Time, pc02_Time, pc03_Time, pc04_Time, pc05_Time, pc06_Time,
              pc07_Time,
              pc08_Time, pc09_Time, pc10_Time);
        } catch (IOException | InterruptedException ex) {
          ex.printStackTrace();
        }
        frame.remove(sellPage);
        frame.add(mainPage, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
        MultiThread[] mt = new MultiThread[1];
        mt[0] = new MultiThread();
        mt[0].start();
      }
    });

    frame.add(sellPage, BorderLayout.CENTER);

    // 판매시작 패널 끝

    frame.setVisible(true);

  }

  static void refreshMain(Admin post, JLabel pc01_Time, JLabel pc02_Time, JLabel pc03_Time,
      JLabel pc04_Time, JLabel pc05_Time, JLabel pc06_Time, JLabel pc07_Time, JLabel pc08_Time,
      JLabel pc09_Time, JLabel pc10_Time) {
    if (post.userPCs[0][0].equals("0")) {
      pc01_Time.setText("사용대기");
    } else {
      pc01_Time.setText("사용중 : " + post.userPCs[0][1] + "분 남음");
    }
    if (post.userPCs[1][0].equals("0")) {
      pc02_Time.setText("사용대기");
    } else {
      pc02_Time.setText("사용중 : " + post.userPCs[1][1] + "분 남음");
    }
    if (post.userPCs[2][0].equals("0")) {
      pc03_Time.setText("사용대기");
    } else {
      pc03_Time.setText("사용중 : " + post.userPCs[2][1] + "분 남음");
    }
    if (post.userPCs[3][0].equals("0")) {
      pc04_Time.setText("사용대기");
    } else {
      pc04_Time.setText("사용중 : " + post.userPCs[3][1] + "분 남음");
    }
    if (post.userPCs[4][0].equals("0")) {
      pc05_Time.setText("사용대기");
    } else {
      pc05_Time.setText("사용중 : " + post.userPCs[4][1] + "분 남음");
    }
    if (post.userPCs[5][0].equals("0")) {
      pc06_Time.setText("사용대기");
    } else {
      pc06_Time.setText("사용중 : " + post.userPCs[5][1] + "분 남음");
    }
    if (post.userPCs[6][0].equals("0")) {
      pc07_Time.setText("사용대기");
    } else {
      pc07_Time.setText("사용중 : " + post.userPCs[6][1] + "분 남음");
    }
    if (post.userPCs[7][0].equals("0")) {
      pc08_Time.setText("사용대기");
    } else {
      pc08_Time.setText("사용중 : " + post.userPCs[7][1] + "분 남음");
    }
    if (post.userPCs[8][0].equals("0")) {
      pc09_Time.setText("사용대기");
    } else {
      pc09_Time.setText("사용중 : " + post.userPCs[8][1] + "분 남음");
    }
    if (post.userPCs[9][0].equals("0")) {
      pc10_Time.setText("사용대기");
    } else {
      pc10_Time.setText("사용중 : " + post.userPCs[9][1] + "분 남음");
    }
  }

  static long nowTime = System.currentTimeMillis();
  static String prevMin = time.format(nowTime);

  static boolean minuteChangeChk() {
    nowTime = System.currentTimeMillis();
    String nowMin = time.format(nowTime);
    if (nowMin.equals(prevMin)) {
      return false;
    }
    prevMin = nowMin;
    return true;
  }
}

class MultiThread extends Thread {

  MultiThread() {
  }

  public void run() {
    while (true) {
      try {
        if (GUIproc.minuteChangeChk()) {
          GUIproc.post.refreshPC();
          Thread.sleep(500);
          GUIproc.refreshMain(GUIproc.post, GUIproc.pc01_Time, GUIproc.pc02_Time, GUIproc.pc03_Time,
              GUIproc.pc04_Time, GUIproc.pc05_Time, GUIproc.pc06_Time, GUIproc.pc07_Time,
              GUIproc.pc08_Time, GUIproc.pc09_Time, GUIproc.pc10_Time);
        }
        Thread.sleep(3000);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}