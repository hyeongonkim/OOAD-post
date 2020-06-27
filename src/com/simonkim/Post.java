package com.simonkim;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Vector;
import javax.swing.*;
import javax.swing.text.*;

public class Post {
  public static Admin admin = null;
  public static JFrame mainFrame = null;
  public static boolean isMainSales = false;

  public static void main(String[] args) {
    mainFrame = new JFrame("PC cafe");
    mainFrame.setSize(900, 600);
    mainFrame.setResizable(false);
    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setVisible(true);
    startSales();
  }

  protected static void startSales() {
    JPanel startSales = new JPanel();
    startSales.setLayout(new GridLayout(1, 1));

    JButton sellOn = new JButton("판매를 시작하려면 눌러주세요");
    startSales.add(sellOn);
    sellOn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          admin = new Admin();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
        mainFrame.remove(startSales);
        new MultiThread().start();
        mainSales();
      }
    });

    mainFrame.add(startSales, BorderLayout.CENTER);
    mainFrame.revalidate();
    mainFrame.repaint();
  }

  protected static void mainSales() {
    isMainSales = true;
    JPanel mainSales = new JPanel();
    mainSales.setLayout(new GridLayout(5, 5));
    String[][] currPCs = admin.getUserPCs();

    for(int j = 0; j < 10; j += 5) {
      for(int i = j; i < j + 5; i++)
        mainSales.add(new JLabel("PC " + (i + 1), SwingConstants.CENTER));
      for(int i = j; i < j + 5; i++) {
        String status = "";
        if (currPCs[i][0].equals("0")) {
          status = "사용대기";
        } else {
          status = "사용중 : " + currPCs[i][1] + "분 남음";
        }
        mainSales.add(new JLabel(status, SwingConstants.CENTER));
      }
    }

    JButton pcCharge = new JButton("PC충전");
    mainSales.add(pcCharge);
    pcCharge.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        isMainSales = false;
        mainFrame.remove(mainSales);
        chargePC();
      }
    });

    JButton menuSell = new JButton("상품판매");
    mainSales.add(menuSell);
    menuSell.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        isMainSales = false;
        mainFrame.remove(mainSales);
        saleFood();
      }
    });

    JButton pcFeeChange = new JButton("PC요금변경");
    mainSales.add(pcFeeChange);
    pcFeeChange.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        isMainSales = false;
        mainFrame.remove(mainSales);
        changePCPay();
      }
    });

    JButton inquerySales = new JButton("매출조회");
    mainSales.add(inquerySales);
    inquerySales.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        isMainSales = false;
        mainFrame.remove(mainSales);
        inquerySales();
      }
    });

    JButton sellOff = new JButton("판매종료");
    mainSales.add(sellOff);
    sellOff.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          admin.endSales();
        } catch (IOException ioException) {
          ioException.printStackTrace();
        }
        System.exit(0);
      }
    });

    mainFrame.add(mainSales, BorderLayout.CENTER);
    mainFrame.revalidate();
    mainFrame.repaint();
  }

  protected static void chargePC() {
    JPanel chargePC = new JPanel();
    chargePC.setLayout(new GridLayout(7, 3));

    JPanel pcNum = new JPanel();
    pcNum.setLayout(new BoxLayout(pcNum, BoxLayout.X_AXIS));
    pcNum.add(new JLabel("충전할 번호 : "));
    JTextField toNum = new JTextField();
    toNum.setDocument(new IntegerDocument());
    pcNum.add(toNum);
    chargePC.add(pcNum);

    JPanel pcTime = new JPanel();
    pcTime.setLayout(new BoxLayout(pcTime, BoxLayout.X_AXIS));
    pcTime.add(new JLabel("충전할 시간 : "));
    JTextField toTime = new JTextField();
    toTime.setDocument(new IntegerDocument());
    pcTime.add(toTime);
    chargePC.add(pcTime);

    JButton payment = new JButton("결제하기");
    chargePC.add(payment);
    payment.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(!toNum.getText().equals("") && !toTime.getText().equals("")) {
          int chargePCNum = Integer.parseInt(toNum.getText());
          int chargePCTime = Integer.parseInt(toTime.getText());
          if (chargePCNum >= 1 && chargePCNum <= 10 && chargePCTime >= 1) {
            mainFrame.remove(chargePC);
            payment(0, chargePCNum, chargePCTime, null);
          }
        }
      }
    });

    JButton returnToMain = new JButton("돌아가기");
    chargePC.add(returnToMain);
    returnToMain.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mainFrame.remove(chargePC);
        mainSales();
      }
    });

    mainFrame.add(chargePC, BorderLayout.CENTER);
    mainFrame.revalidate();
    mainFrame.repaint();
  }

  protected static void saleFood() {
    JPanel saleFood = new JPanel();
    saleFood.setLayout(new GridLayout(4, 4));

    JLabel orderList = new JLabel("<html>##선택한상품##</html>");
    Vector<String> order = new Vector<String>();
    Map<String, Integer> menuTable = admin.getFoodTable();

    menuTable.forEach((key, value) ->
    {
      JButton temp = new JButton("<html>" + key + "<br>" + Integer.toString(value) + "원</html>");
      saleFood.add(temp);
      temp.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          order.add(key);
          orderList.setText(orderList.getText().substring(0, orderList.getText().length() - 7) + "<br>" + key + "</html>");
        }
      });
    });

    for(int i = menuTable.size(); i < 12; i++)
      saleFood.add(new JButton("<html></html>"));

    JScrollPane scroller = new JScrollPane(orderList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    saleFood.add(scroller);

    JButton payOrder = new JButton("결제하기");
    saleFood.add(payOrder);
    payOrder.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(!order.isEmpty()) {
          mainFrame.remove(saleFood);
          payment(1, 0, 0, order);
        }
      }
    });

    JButton manageOrder = new JButton("상품관리");
    saleFood.add(manageOrder);
    manageOrder.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mainFrame.remove(saleFood);
        manageFood();
      }
    });

    JButton returnToMain = new JButton("돌아가기");
    saleFood.add(returnToMain);
    returnToMain.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mainFrame.remove(saleFood);
        mainSales();
      }
    });

    mainFrame.add(saleFood, BorderLayout.CENTER);
    mainFrame.revalidate();
    mainFrame.repaint();
  }

  protected static void payment(int type, int pcNum, int pcTime, Vector<String> order) {
    JPanel payment = new JPanel();
    payment.setLayout(new GridLayout(7, 3));

    JPanel forPay = new JPanel();
    forPay.setLayout(new BoxLayout(forPay, BoxLayout.X_AXIS));
    forPay.add(new JLabel("결제할 금액 : "));
    JLabel payMoney = new JLabel(Integer.toString(admin.calculatePrice(type, pcTime, order)));
    forPay.add(payMoney);
    payment.add(forPay);

    JPanel recPay = new JPanel();
    recPay.setLayout(new BoxLayout(recPay, BoxLayout.X_AXIS));
    recPay.add(new JLabel("수령한 금액 : "));
    JTextField recMoney = new JTextField();
    recMoney.setDocument(new IntegerDocument());
    recPay.add(recMoney);
    payment.add(recPay);

    JButton calPay = new JButton("수령확인 / 거스름계산");
    payment.add(calPay);

    JPanel chgPay = new JPanel();
    chgPay.setLayout(new BoxLayout(chgPay, BoxLayout.X_AXIS));
    chgPay.add(new JLabel("거스름 금액 : "));
    JLabel chgMoney = new JLabel("");
    chgPay.add(chgMoney);
    payment.add(chgPay);

    JButton successPay = new JButton("결제완료");
    successPay.setEnabled(false);
    payment.add(successPay);
    successPay.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if(!recMoney.getText().equals("") && Integer.parseInt(recMoney.getText()) >= Integer.parseInt(payMoney.getText())) {
          try {
            if(admin.completePay(type, pcNum, pcTime, order, Integer.parseInt(payMoney.getText()), Integer.parseInt(recMoney.getText()))) {
              mainFrame.remove(payment);
              mainSales();
            }
          } catch (IOException ex) {
            ex.printStackTrace();
          }
        } else {
          chgMoney.setText("금액부족, 결제불가능");
          successPay.setEnabled(false);
        }
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

    JButton cancel = new JButton("취소하기");
    payment.add(cancel);
    cancel.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mainFrame.remove(payment);
        mainSales();
      }
    });

    mainFrame.add(payment, BorderLayout.CENTER);
    mainFrame.revalidate();
    mainFrame.repaint();
  }

  protected static void manageFood() {
    JPanel manageFood = new JPanel();
    manageFood.setLayout(new GridLayout(7, 3));

    manageFood.add(new JLabel("추가/삭제할 상품 정보를 입력하고 버튼을 누르세요. 삭제의 경우 상품명만 맞게 적으면 됩니다.", SwingConstants.CENTER));

    JPanel nameP = new JPanel();
    nameP.setLayout(new BoxLayout(nameP, BoxLayout.X_AXIS));
    nameP.add(new JLabel("추가/삭제할 상품이름 : "));
    JTextField menuName = new JTextField();
    nameP.add(menuName);
    manageFood.add(nameP);

    JPanel priceP = new JPanel();
    priceP.setLayout(new BoxLayout(priceP, BoxLayout.X_AXIS));
    priceP.add(new JLabel("추가/삭제할 상품가격 : "));
    JTextField menuPrice = new JTextField();
    menuPrice.setDocument(new IntegerDocument());
    priceP.add(menuPrice);
    manageFood.add(priceP);

    JButton addMenu = new JButton("추가하기");
    manageFood.add(addMenu);
    addMenu.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!menuName.getText().equals("") && Integer.parseInt(menuPrice.getText()) >= 0) {
          try {
            boolean success = admin.addFood(menuName.getText(), menuPrice.getText());
            if(success) {
              mainFrame.remove(manageFood);
              mainSales();
            }
          } catch (IOException ioException) {
            ioException.printStackTrace();
          }
        }
      }
    });

    JButton deleteMenu = new JButton("삭제하기");
    manageFood.add(deleteMenu);
    deleteMenu.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!menuName.getText().equals("")) {
          try {
            boolean success = admin.deleteFood(menuName.getText());
            if(success) {
              mainFrame.remove(manageFood);
              mainSales();
            }
          } catch (IOException ioException) {
            ioException.printStackTrace();
          }
        }
      }
    });

    JButton returnToMain = new JButton("돌아가기");
    manageFood.add(returnToMain);
    returnToMain.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mainFrame.remove(manageFood);
        mainSales();
      }
    });

    mainFrame.add(manageFood, BorderLayout.CENTER);
    mainFrame.revalidate();
    mainFrame.repaint();
  }

  protected static void changePCPay() {
    JPanel changePCPay = new JPanel();
    changePCPay.setLayout(new GridLayout(7, 3));

    JPanel nowPay = new JPanel();
    nowPay.setLayout(new BoxLayout(nowPay, BoxLayout.X_AXIS));
    nowPay.add(new JLabel("현재 이용 요금 : "));
    nowPay.add(new JLabel(Integer.toString(admin.getChargeFee())));
    changePCPay.add(nowPay);

    JPanel newPay = new JPanel();
    newPay.setLayout(new BoxLayout(newPay, BoxLayout.X_AXIS));
    newPay.add(new JLabel("바꿀 이용 요금 : "));
    JTextField newMoney = new JTextField();
    newMoney.setDocument(new IntegerDocument());
    newPay.add(newMoney);
    changePCPay.add(newPay);

    JButton changePay = new JButton("변경하기");
    changePCPay.add(changePay);
    changePay.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (Integer.parseInt(newMoney.getText()) >= 0) {
          try {
            boolean success = admin.changeFee(Integer.parseInt(newMoney.getText()));
            if(success) {
              mainFrame.remove(changePCPay);
              mainSales();
            }
          } catch (IOException ioException) {
            ioException.printStackTrace();
          }
        }
      }
    });

    JButton returnToMain = new JButton("돌아가기");
    changePCPay.add(returnToMain);
    returnToMain.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mainFrame.remove(changePCPay);
        mainSales();
      }
    });

    mainFrame.add(changePCPay, BorderLayout.CENTER);
    mainFrame.revalidate();
    mainFrame.repaint();
  }

  protected static void inquerySales() {
    JPanel inquerySales = new JPanel();
    inquerySales.setLayout(new GridLayout(7, 3));

    JPanel nowSales = new JPanel();
    nowSales.setLayout(new BoxLayout(nowSales, BoxLayout.X_AXIS));
    nowSales.add(new JLabel("현재까지의 매출 : "));
    nowSales.add(new JLabel(Integer.toString(admin.getTotalSales())));
    inquerySales.add(nowSales);

    JButton returnToMain = new JButton("돌아가기");
    inquerySales.add(returnToMain);
    returnToMain.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        mainFrame.remove(inquerySales);
        mainSales();
      }
    });

    mainFrame.add(inquerySales, BorderLayout.CENTER);
    mainFrame.revalidate();
    mainFrame.repaint();
  }
}

class MultiThread extends Thread {
  private long nowTime;
  private String prevMin = null;
  private SimpleDateFormat extractMin = null;

  MultiThread() {
    extractMin = new SimpleDateFormat("mm");
    nowTime = System.currentTimeMillis();
    prevMin = extractMin.format(nowTime);
  }

  public void run() {
    while (true) {
      try {
        if (minuteChangeChk()) {
          Post.admin.deductionTime();
          if (Post.isMainSales) {
            Post.mainFrame.getContentPane().removeAll();
            Post.mainSales();
          }
        }
        Thread.sleep(3000);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public boolean minuteChangeChk() {
    nowTime = System.currentTimeMillis();
    String nowMin = extractMin.format(nowTime);
    if (nowMin.equals(prevMin))
      return false;
    prevMin = nowMin;
    return true;
  }
}

class IntegerDocument extends PlainDocument {

  int currentValue = 0;

  public IntegerDocument() {
  }

  public int getValue() {
    return currentValue;
  }

  public void insertString(int offset, String string,
      AttributeSet attributes) throws BadLocationException {

    if (string == null) {
      return;
    } else {
      String newValue;
      int length = getLength();
      if (length == 0) {
        newValue = string;
      } else {
        String currentContent = getText(0, length);
        StringBuffer currentBuffer =
            new StringBuffer(currentContent);
        currentBuffer.insert(offset, string);
        newValue = currentBuffer.toString();
      }
      currentValue = checkInput(newValue, offset);
      super.insertString(offset, string, attributes);
    }
  }
  public void remove(int offset, int length)
      throws BadLocationException {
    int currentLength = getLength();
    String currentContent = getText(0, currentLength);
    String before = currentContent.substring(0, offset);
    String after = currentContent.substring(length+offset,
        currentLength);
    String newValue = before + after;
    currentValue = checkInput(newValue, offset);
    super.remove(offset, length);
  }
  public int checkInput(String proposedValue, int offset)
      throws BadLocationException {
    if (proposedValue.length() > 0) {
      try {
        int newValue = Integer.parseInt(proposedValue);
        return newValue;
      } catch (NumberFormatException e) {
        throw new BadLocationException(proposedValue, offset);
      }
    } else {
      return 0;
    }
  }
}