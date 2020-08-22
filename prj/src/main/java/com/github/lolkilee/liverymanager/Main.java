package com.github.lolkilee.liverymanager;

import javax.swing.*;

public class Main {

    /*
        Livery manager by Lolkilee,
        in cooperation with Clink123
    */

    static LiveryManager liveryManager;
    static Window window;

    public static boolean isRunning = true;

    public static void main(String[] args) throws InterruptedException {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            window = new Window(700, 700);
            liveryManager = new LiveryManager();
        } catch (Exception e) {
            e.printStackTrace();
        }

        while(isRunning) {
            window.update();
            Thread.sleep(200);
        }

    }


}
