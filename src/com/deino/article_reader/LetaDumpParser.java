package com.deino.article_reader;

import java.io.File;

/**
 * Created by Inwhite on 4/12/2015.
 */
public class LetaDumpParser {
    final String ROOT = "/leta_news";

    public void run() {

        File directory = new File(ROOT);
        File[] listOfDirectories = directory.listFiles();
        File[] directories;
        for (int i = 0; i < listOfDirectories.length; i++) {
            if (listOfDirectories[i].isDirectory()) {
                String curr_dir_name = listOfDirectories[i].getName();
                System.out.println();
                System.out.println("++++++++++++++++++++++++++++++++");
                System.out.println("Reading directory: " + curr_dir_name);
                System.out.println("++++++++++++++++++++++++++++++++");
                File current_dir = new File(ROOT + "/" + curr_dir_name);
                File[] listOfFiles = current_dir.listFiles();
                for(int k = 0; k < listOfFiles.length;k++){
                    System.out.println("Reading file: "+ listOfFiles[k].getName());

                }
                System.out.println();
            }
        }
    }
}
