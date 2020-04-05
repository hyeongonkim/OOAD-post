package com.simonkim;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

public class GUIproc {

  static int forChargePCNum = 0;
  static int forChargePCTime = 0;

  public static void main(String[] args) throws IOException {
    JFrame frame = new JFrame("PC cafe");
    frame.setSize(900, 600);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    Admin post = new Admin();

    JPanel chargePage = new JPanel();
    JPanel mainPage = new JPanel();
    JPanel payPage = new JPanel();
    JPanel sellPage = new JPanel();
    JPanel chgPCPayPage = new JPanel();
    JPanel salesPage = new JPanel();

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

    JLabel pc01_Time = new JLabel("null", SwingConstants.CENTER);
    mainPage.add(pc01_Time);
    JLabel pc02_Time = new JLabel("null", SwingConstants.CENTER);
    mainPage.add(pc02_Time);
    JLabel pc03_Time = new JLabel("null", SwingConstants.CENTER);
    mainPage.add(pc03_Time);
    JLabel pc04_Time = new JLabel("null", SwingConstants.CENTER);
    mainPage.add(pc04_Time);
    JLabel pc05_Time = new JLabel("null", SwingConstants.CENTER);
    mainPage.add(pc05_Time);

    mainPage.add(new JLabel("PC06", SwingConstants.CENTER));
    mainPage.add(new JLabel("PC07", SwingConstants.CENTER));
    mainPage.add(new JLabel("PC08", SwingConstants.CENTER));
    mainPage.add(new JLabel("PC09", SwingConstants.CENTER));
    mainPage.add(new JLabel("PC10", SwingConstants.CENTER));

    JLabel pc06_Time = new JLabel("null", SwingConstants.CENTER);
    mainPage.add(pc06_Time);
    JLabel pc07_Time = new JLabel("null", SwingConstants.CENTER);
    mainPage.add(pc07_Time);
    JLabel pc08_Time = new JLabel("null", SwingConstants.CENTER);
    mainPage.add(pc08_Time);
    JLabel pc09_Time = new JLabel("null", SwingConstants.CENTER);
    mainPage.add(pc09_Time);
    JLabel pc10_Time = new JLabel("null", SwingConstants.CENTER);
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
        try {
          post.chargePC(forChargePCNum, forChargePCTime, Integer.parseInt(recMoney.getText()));
          refreshMain(post, pc01_Time, pc02_Time, pc03_Time, pc04_Time, pc05_Time, pc06_Time,
              pc07_Time, pc08_Time, pc09_Time, pc10_Time);
          payMoney.setText("");
          recMoney.setText("");
          chgMoney.setText("");
          frame.remove(payPage);
          frame.add(mainPage, BorderLayout.CENTER);
          frame.revalidate();
          frame.repaint();
        } catch (IOException ex) {
          ex.printStackTrace();
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
      }
    });

    frame.add(sellPage, BorderLayout.CENTER);

    // 판매시작 패널 끝

    frame.setVisible(true);

  }

  private static void refreshMain(Admin post, JLabel pc01_Time, JLabel pc02_Time, JLabel pc03_Time,
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

  static boolean minuteChangeChk() {
    // 주기적(몇초마다)으로 돌리면서 분 단위가 바뀌는지 체크할 함수, 바뀐다면 PC상태 파일에 변경필요(리프레시 피씨)
    return true;
  }
}
