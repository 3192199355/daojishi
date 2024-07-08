package com.a;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class CountdownTimerApp extends JFrame {
    private JLabel countdownLabel;
    private JButton startButton, pauseButton, resetButton;
    private int timeRemaining = 10; // 默认倒计时时间为60秒
    private boolean isPaused = true;
    private Clip clip; // 音频剪辑对象
    //设计倒计时输入框
    private JTextField inputmiao;
    private JTextField inputshi;
    private JTextField inputfen;

    public CountdownTimerApp() {
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File("bell.wav")));
        } catch (Exception e) {
            System.out.println("Error loading audio file.");
        }
        setTitle("倒计时工具");
        setSize(300, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel();
        JPanel panel2 = new JPanel();
        JPanel panel3 = new JPanel();
        JPanel panel4 = new JPanel();
        panel2.setLayout(new BorderLayout());
        panel.setLayout(new FlowLayout());
        inputmiao = new JTextField(2);
        inputmiao.setText("0");
        inputshi = new JTextField(2);
        inputshi.setText("0");
        inputfen = new JTextField(2);
        inputfen.setText("0");
        JLabel kaishi = new JLabel("倒计时：");

        panel4.add(kaishi);

        panel4.add(inputshi);
        JLabel shi = new JLabel("时");
        panel4.add(shi);
        panel4.add(inputfen);
        JLabel fen = new JLabel("分");
        panel4.add(fen);
        panel4.add(inputmiao);
        JLabel miao = new JLabel("秒");
        panel4.add(miao);
        panel.add(panel4);
        //panel.add(input);
        JButton inputBtn = new JButton("   输入   ");
        panel.add(inputBtn);
        inputBtn.addActionListener(e -> {
            int shii = Integer.parseInt(inputshi.getText());
            int fenn = Integer.parseInt(inputfen.getText());
            int miaoo = Integer.parseInt(inputmiao.getText());
            timeRemaining = (shii * 60 + fenn) * 60 + miaoo;
            updateCountdownLabel();
        });
        countdownLabel = new JLabel("默认剩余时间:" + timeRemaining+ "秒");
        //panel.add(countdownLabel);
        panel3.add(countdownLabel);
        panel.add(panel3);
        startButton = new JButton("开始");
        startButton.addActionListener(e -> {
            if (isPaused) {
                isPaused = false;
                startCountdown();
            }
        });
        //panel.add(startButton);
        panel2.add(startButton, BorderLayout.WEST);
        pauseButton = new JButton("暂停");
        pauseButton.addActionListener(e -> {
            if (!isPaused) {
                isPaused = true;
            }
        });
        //panel.add(pauseButton);
        panel2.add(pauseButton, BorderLayout.CENTER);
        resetButton = new JButton("重置");
        resetButton.addActionListener(e -> {
            timeRemaining = 60;
            updateCountdownLabel();
            isPaused = true;
        });
        //panel.add(resetButton);
        panel2.add(resetButton, BorderLayout.EAST);
        panel.add(panel2);
        add(panel);
        setVisible(true);
    }

    private void startCountdown() {
        Thread timerThread = new Thread(() -> {
            while (timeRemaining > 0 && !isPaused) {
                try {
                    TimeUnit.MILLISECONDS.sleep(999);
                    timeRemaining--;
                    updateCountdownLabel();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            if (timeRemaining == 0) {
                playBellSound();
                JOptionPane.showMessageDialog(this, "倒计时结束！");
                isPaused = true;
            }
        });
        timerThread.start();
    }

    private void playBellSound() {
        if (clip != null) {
            clip.setFramePosition(0); // 重置音频到开头
            clip.start(); // 播放音频
        }
    }

    private void updateCountdownLabel() {
        SwingUtilities.invokeLater(() -> {
            int hours = timeRemaining / 3600;
            int minutes = timeRemaining / 60-hours*60;
            int seconds = timeRemaining % 60;
            countdownLabel.setText("剩余时间: " + hours + "小时" + minutes + "分" + seconds + "秒");
        });
    }
    public static void main(String[] args) {
        new CountdownTimerApp();
    }
}