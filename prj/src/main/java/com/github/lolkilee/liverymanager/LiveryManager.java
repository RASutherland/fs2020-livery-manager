package com.github.lolkilee.liverymanager;

import com.google.gson.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.lib.ProgressMonitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;


public class LiveryManager implements Runnable{

    public static String jar_path;
    public static String install_folder_path;
    public static String download_folder_path;

    //For every airplane there should be an arraylist containing that plane's liveries
    private ArrayList<ArrayList<LiveryContainer>> liveryContainers = new ArrayList<>();
    private ArrayList<String> aircraftFolderNames = new ArrayList<>();

    //Install passed files to the right path
    public void installFiles(String folder_path, ArrayList<LiveryContainer> containersToInstall) throws IOException {
        moveStandardFilesToFolder(folder_path);
        for(LiveryContainer container : containersToInstall) {
            System.out.println("Installing container to: " + folder_path + "\\SimObjects\\Airplanes\\" + container.getPlaneName());
            moveLiveryFilesToFolder(folder_path + "\\SimObjects\\Airplanes\\" + container.getPlaneName(),
                    container);
        }

        updateLayoutFile(folder_path);
    }

    //Clone the livery repository for local use
    public void clonePublicRepo() throws GitAPIException {
        System.out.println("Cloning public repository to " + download_folder_path);
        Git.cloneRepository()
                .setDirectory(new File(download_folder_path))
                .setURI("https://github.com/Lolkilee/fs2020-megapack-liveries.git")
                .setProgressMonitor(new ProgressMonitor() {
                    @Override
                    public void start(int i) {
                        System.out.println("Loading repo...");
                    }
                    @Override
                    public void beginTask(String s, int i) {}
                    @Override
                    public void update(int i) {Main.window.updateLoadingBar(i);}
                    @Override
                    public void endTask() {Main.window.doneLoading();}
                    @Override
                    public boolean isCancelled() {return false;}
                })
                .call();
    }

    //Load the repository data into the liverycontainers
    public void loadLiveryData() throws IOException {
        File[] airplaneFolders = new File(download_folder_path + "\\Liveries\\").listFiles();
        for(File airplaneFolder : airplaneFolders) {
            ArrayList<LiveryContainer> livery_containers = new ArrayList<LiveryContainer>();
            aircraftFolderNames.add(airplaneFolder.getName());
            File[] liveryFolders = new File(airplaneFolder.getCanonicalPath()).listFiles();
            for(File liveryFolder : liveryFolders) {
                livery_containers.add(new LiveryContainer(liveryFolder.getCanonicalPath()));
            }
            liveryContainers.add(livery_containers);
        }
    }

    //Move default files to correct folder
    //pack_path is the main folder of the megapack (within community)
    public void moveStandardFilesToFolder(String pack_path) throws IOException {
        File dir = new File(pack_path + "\\SimObjects\\Airplanes\\");
        if(!dir.exists()) dir.mkdirs();

        //Create directories if missing
        for(String s : aircraftFolderNames) {
            dir = new File(pack_path + "\\SimObjects\\Airplanes\\" + s);
            if(!dir.exists()) dir.mkdirs();
        }

        File f = new File(download_folder_path + "\\community-folder-std\\");
        FileUtils.copyDirectory(f, new File(pack_path));
    }

    //Move livery container to right location in community folder & append to aircraft.cfg
    //Folder path is the main path of the airplane e.g. Asobo_A320_NEO
    public void moveLiveryFilesToFolder(String folder_path, LiveryContainer container) throws IOException {
        System.out.println("Moving " + container.getLiveryName() + " to: " + folder_path);
        File textureDir = new File(folder_path + "\\TEXTURE." + container.getTextureFolderName().toUpperCase());
        textureDir.mkdirs();
        File[] textureFiles = new File(container.getPath()).listFiles();
        for(File f : textureFiles) {
            if(f.isDirectory()) {
                File[] directoryFiles = f.listFiles();
                for(File f2 : directoryFiles) {
                    copyFile(f2, folder_path + "\\TEXTURE." + container.getTextureFolderName().toUpperCase() + "\\", true) ;
                }
            }
        }

        appendToConfig(container.getConfig_lines(), new File(folder_path + "\\aircraft.cfg"));
    }

    //Update layout.json file for the whole package
    public void updateLayoutFile(String filePath) throws IOException {
        ArrayList<String> filePaths = new ArrayList<>();
        List<File> filesInDir = Files.walk(Paths.get(filePath))
                .filter(Files::isRegularFile)
                .map(Path::toFile)
                .collect(Collectors.toList());
        for(File f : filesInDir) {
            String s = f.getCanonicalPath();
            s = s.substring(filePath.length());
            s = s.replace("\\", "/");
            //s = s.substring(1);
            if(!s.contains("layout.json") && !s.contains("manifest.json")) filePaths.add(s);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonObject jsonObject = new JsonObject();
        JsonArray pathsArray = new JsonArray();

        for(String path : filePaths) {
            JsonObject tObject = new JsonObject();
            File f = new File(filePath + "\\" + path);
            tObject.addProperty("path", path);
            tObject.addProperty("size", FileUtils.sizeOf(f));
            tObject.addProperty("date", 132398508779749484L);
            pathsArray.add(tObject);
        }

        jsonObject.add("content", pathsArray);
        FileWriter fileWriter = new FileWriter(filePath + "\\layout.json");
        gson.toJson(jsonObject, fileWriter);
        fileWriter.close();

    }

    //Move a file to location
    public void copyFile(File f, String location, boolean useName) throws IOException {
        File dir = new File(location);
        if(!dir.exists()) dir.mkdirs();
        if(useName)
            Files.copy(Paths.get(f.getCanonicalPath()), Paths.get(location + "\\" + f.getName()), StandardCopyOption.REPLACE_EXISTING);
        else
            Files.copy(Paths.get(f.getCanonicalPath()), Paths.get(location), StandardCopyOption.REPLACE_EXISTING);
    }

    //Append lines to config of airplane
    public void appendToConfig(ArrayList<String> lines, File configFile) throws IOException {
        System.out.println("Appending to config file: " + configFile.getCanonicalPath());
        FileWriter fileWriter = new FileWriter(configFile, true);
        fileWriter.append("\n");
        for(String line : lines) {
            //System.out.println("Line: " + line);
            if(line.contains("%n%"))
                line = line.replace("%n%", Integer.toString(getNum(configFile)));
            fileWriter.append(line + "\n");
        }
        fileWriter.close();
    }

    //Get the n for the [FLTSIM.xx]
    public int getNum(File configFile) throws FileNotFoundException {
        Scanner s = new Scanner(configFile);
        int n = 0;
        while(s.hasNextLine()) {
            String line = s.nextLine();
            if(line.contains("[FLTSIM"))
                n++;
        }
        return n;
    }

    @Override
    public void run() {
        try {
            jar_path = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
            install_folder_path = jar_path.substring(0, jar_path.indexOf("livery-manager"));
            download_folder_path = install_folder_path + "tmp\\";
            System.out.println("Jar location: " + jar_path);
            System.out.println("Install location: " + install_folder_path);
            System.out.println("Download location: " + download_folder_path);

            File downloadDirectory = new File(download_folder_path);
            if (!downloadDirectory.exists()) downloadDirectory.mkdir();
            else FileUtils.cleanDirectory(downloadDirectory);

            clonePublicRepo();
            loadLiveryData();

            Main.window.setAvailableLiveries(liveryContainers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
