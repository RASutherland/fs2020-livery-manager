package com.github.lolkilee.liverymanager;

public class Main {

    /*
        Livery manager by Lolkilee,
        in cooperation with Clink123
    */

    static LiveryManager liveryManager;

    public static void main(String[] args){
        try {
            liveryManager = new LiveryManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
