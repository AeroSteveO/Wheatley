/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Steve-O
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    Settings
 * - Linked Classes
 *    N/A
 *
 */
public class Throttle extends Settings{

    private int maxLog = 5;
    private long maxTime = 100*1000;
    
    private String filename = "doNotSave";

    Map <String, Map<String, LinkedList<Long>>> timeLog = Collections.synchronizedMap(new TreeMap<String, Map<String, LinkedList<Long>>>());
    //   CHAN        TYPE     TIMELOG
    
    public Throttle (){
//        this.this = settings;
    }
    
    public void setMaxLog(String type, int maxCalls, String channel){
        this.set(type+"log", String.valueOf(maxCalls), channel);
    }
    
    public void setMaxTime(String type, long timeLimit, String channel){
        this.set(type+"time", String.valueOf(timeLimit), channel);
    }
    
//  Map <String, Map<String, LinkedList<Long>>> timeLog = Collections.synchronizedMap(new TreeMap<String, Map<String, LinkedList<Long>>>());
//        CHAN        TYPE     TIMELOG

    public boolean isThrottleActive(String type, String channel){
        Date d = new Date();
        
        int inputMaxLog = Integer.parseInt(this.get(type+"log",channel)); // Max number of logs for the input type
        int inputMaxTime = Integer.parseInt(this.get(type+"time",channel)); // Max number of logs for the input type
        long currentTime = d.getTime();
        
        if (!timeLog.containsKey(channel)){
            addChannelToLog(channel);
        }
        
        if(!timeLog.get(channel).containsKey(type+"log")){
            addTypeToLog(channel,type);
        }
        
        if(timeLog.get(channel).get(type+"log").size() > inputMaxLog) {
            while(timeLog.get(channel).get(type+"log").size()>0 && currentTime - timeLog.get(channel).get(type+"log").getLast() > inputMaxTime) {
                timeLog.get(channel).get(type+"log").pollLast();
            }
            if(timeLog.get(channel).get(type+"log").size()>inputMaxLog) {
//                System.out.println("THROTTLED! BAM");
                return(true);
            }
        }
        timeLog.get(channel).get(type+"log").addFirst(d.getTime());
//        System.out.println("NOT THROTTLED YET");
        return(false);
    }
    
//    private boolean startSettings() {
//        try{
//            this.setFileName(filename);
//            this.loadFile();
//        }
//        catch (Exception ex){
//            System.out.println("SETTINGS FAILED TO LOAD");
//            ex.printStackTrace();
//            return false;
//        }
//        return true;
//    }
    
    public void createMaxTime(String type, String maxTime, String channel) {
        this.create(type+"time", maxTime, channel);
        System.out.println("YAY TIMES");
    }
    
    private void addChannelToLog(String channel){
        LinkedList<Long> logList = new LinkedList<>();
        Map<String,LinkedList<Long>> log = new TreeMap<>();
        String thing = new String();
        log.put(thing,logList);
        timeLog.put(channel, log);
        
    }
    
    public void createMaxLog(String type, String maxLog, String channel) {
        this.create(type+"log", maxLog, channel);
        
//        ArrayList<String> channels = this.getChannelList();
//        
//        for (int i=0;i<channels.size();i++){
//            LinkedList<Long> logList = new LinkedList<>();
//            Map<String,LinkedList<Long>> log = new TreeMap<>();
//            
//            log.put(type+"log",logList);
//            timeLog.put(channels.get(i), log);
//            System.out.println("YAY LOGS");
//        }
    }

    private void addTypeToLog(String channel, String type) {
//                LinkedList<Long> logList = new LinkedList<>();
//        Map<String,LinkedList<Long>> log = new TreeMap<>();
//        String thing = new String();
//        log.put(type,logList);
        
        LinkedList<Long> log = new LinkedList<>();
        
        timeLog.get(channel).put(type+"log", log);
    }
}