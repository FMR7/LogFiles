package com.persistence;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Easier log files managament.
 * @author Fernando
 * @version 0.1
 */
public class LogFiles extends Thread{
    private String pv_fileName;
    private final String pv_fileExt;

    /**
     * Default constructor with default extension. "log"
     * @since 0.1
     */
    public LogFiles() {
        this.pv_fileExt = "log";
    }
    
    /**
     * Create the file, and write the "lines". If the file exists, append the lines.
     * File will look like this: l_name_yyyyMMdd_HHmmss.log
     * @param l_name The prefix name.
     * @param l_lines Lines to write, ex.: "Hello\nWorld".
     * @since 0.1
     */
    public synchronized void createLog(String l_name, String l_lines){
        this.pv_fileName = l_name;
        BufferedWriter writer = null;
        try {
            System.out.print("Creating log file...");
            String timeLog = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            File logFile = new File(pv_fileName + "_" + timeLog + "." + pv_fileExt);

            
            writer = new BufferedWriter(new FileWriter(logFile, true));
            
            String[] ln = l_lines.split("\n");
            for(int i = 0; i < ln.length; i++){
                writer.write(ln[i]);
                writer.newLine();
            }
            System.out.println("Created.");
            System.out.println("Log File Location: " + logFile.getCanonicalPath());
        } catch (Exception e) {
            System.out.println("WARN: Fail creating the file !!!");
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
    }
    
    /**
     * Delete the logs older than l_datetime.
     * @param l_name Filename prefix to look for.
     * @param l_datetime Date, formatted like this "yyyyMMdd_HHmmss"
     * @since 0.1
     */
    public synchronized void deleteOldLogs(String l_name, Date l_datetime){
        this.pv_fileName = l_name;
        BufferedWriter writer = null;
        File folder = new File("./");
        try {
            System.out.print("INFO: Looking for old log files to delete...");
            List<String> logs = new ArrayList();
            int c = 0; // logs counter
            boolean filesFound = false;
            for (final File logFile : folder.listFiles()) {
                if (logFile.isDirectory()) {
                    
                } else {
                    boolean isLog = false;
                    boolean isName = false;
                    boolean isSizeCorrect = false;
                    String fileName = logFile.getName();
                    try{
                        String name = fileName.substring(0, l_name.length()); // Looking for prefix l_name
                        String ext = fileName.substring(fileName.length()-3, fileName.length()); // Looking for "log" extension.
                        isLog = ext.equals(this.pv_fileExt);
                        isName = name.equals(l_name);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    String fileDateTime = "";
                    if(fileName.length() >= 17){ // Check length
                        fileDateTime = fileName.substring(fileName.length()-19, fileName.length()-(this.pv_fileExt.length()+1));
                        isSizeCorrect = fileDateTime.length() == 15;
                    }
                    if(isLog && isName && isSizeCorrect){
                        boolean isInTime = false;
                        try{
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                            Date fileDate = simpleDateFormat.parse(fileDateTime);

                            isInTime = fileDate.before(l_datetime);
                        }catch(Exception e){
                        }
                        if(isInTime){
                            filesFound = true;
                            logs.add(c, fileName);
                        }
                    }
                }
            }
            
            if(filesFound){ // Remove files
                System.out.println("Files found.");
                System.out.println("Log Files: ");
                for(int i = 0; i < logs.size();i++){
                    System.out.print(" " + logs.get(i));
                    File delFile = new File(logs.get(i));
                    
                    boolean isDeleted = delFile.delete();
                    if(isDeleted){
                        System.out.println(" | REMOVED");
                    }
                }
            }else{
                System.out.println("Old logs not found. That's good !!!");
            }
            
        } catch (Exception e) {
            System.out.println("WARN: Fail reading logs !!!");
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (Exception e) {
            }
        }
    }
}
