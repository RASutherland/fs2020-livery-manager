package com.github.lolkilee.liverymanager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LiveryContainer {

    private String original_location;
    private String author;
    private String category;
    private String livery_name;
    private String texture_folder_name;
    private String plane_name;
    private String livery_thumbnail;

    //Lines of the livery.cfg file
    private ArrayList<String> config_lines = new ArrayList<String>();

    public LiveryContainer(String folder) throws FileNotFoundException {
        original_location = folder;
        livery_thumbnail = original_location + "\\textures\\thumbnail.jpg";
        File file = new File(original_location);
        livery_name = file.getName();
        File f = new File(file.getParent());
        plane_name = f.getName();

        //Read the inst.cfg file
        Scanner fileReader = new Scanner(new File(original_location + "\\inst.cfg"));
        while(fileReader.hasNextLine()) {
            String line = fileReader.nextLine();
            //System.out.println(line);
            if(!line.contains(";") && !line.isEmpty()) {
                if(line.contains("author"))
                    author = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
                else if(line.contains("category"))
                    category = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
            }
        }
        fileReader.close();

        //Read the livery.cfg file
        fileReader = new Scanner(new File(original_location + "\\livery.cfg"));
        while(fileReader.hasNextLine()) {
            String line = fileReader.nextLine();
            if(line.contains("texture")) {
                texture_folder_name = line.substring(line.indexOf("\"") + 1, line.indexOf("\"", line.indexOf("\"") + 1));
            }
            config_lines.add(line);
        }
        fileReader.close();

        System.out.println("[" + original_location + "]: \nauthor: " + author + "\ncategory: " + category + "\n" +
                "texture folder: " + texture_folder_name);

        System.out.println("Added " + original_location + " as livery container");
    }

    public ArrayList<String> getConfig_lines() {return config_lines;}
    public String getAuthor() {return author;}
    public String getCategory() {return category;}
    public String getPath() {return original_location;}
    public String getLiveryName() {return livery_name;}
    public String getTextureFolderName() {return texture_folder_name;}
    public String getPlaneName() {return plane_name;}
    //TODO: add gui part which shows this thumbnail
    public String getLiveryThumbnail() {return livery_thumbnail;}
}
