package com.github.lolkilee.liverymanager;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Cleanup extends Thread {

    private String tempFolderPath;

    //Should be run at System.exit
    public Cleanup(String tempFolderPath) {
        this.tempFolderPath = tempFolderPath;
    }

    //Remove all the files from the tmp download directory to save space
    public void run() {
        try {
            FileUtils.deleteDirectory(new File(tempFolderPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
