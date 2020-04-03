package com.simonkim;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUIproc {
  public static void main(String[] args) throws IOException {
    JFrame frame = new JFrame("PC cafe");
    frame.setSize(800, 600);
    frame.setResizable(false);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // 판매시작 패널

    JPanel sellPage = new JPanel();
    sellPage.setLayout(new GridLayout(1, 1));

    JButton sellOn = new JButton("판매를 시작하려면 눌러주세요");
    sellPage.add(sellOn);

    Admin post = new Admin();

    sellOn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          post.init();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
        frame.remove(sellPage);
        frame.revalidate();
        frame.repaint();
      }
    });

    frame.add(sellPage, BorderLayout.CENTER);

    // 판매시작 패널 끝

    frame.setVisible(true);

  }

  static boolean minuteChangeChk() {
    // 주기적(몇초마다)으로 돌리면서 분 단위가 바뀌는지 체크할 함수, 바뀐다면 PC상태 파일에 변경필요(리프레시 피씨)
    return true;
  }
}
