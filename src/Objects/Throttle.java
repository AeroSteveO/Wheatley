/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
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
 * Object:
 *      Throttle
 * - Object that contains an int score for the input user, and keeps track of it
 * - Object can be completely controlled through the score array
 *
 * Methods:
 *      createMaxLog     - Creates the input channel/type/value pair in the map
 *      createMaxTime    - Creates the input channel/type/value pair in the map
 *     *isThrottleActive - Returns true if the input command should be blocked for throttling
 *     *setMaxLog        - Sets the maximum calls for throttling
 *     *setMaxTime       - Sets the maximum time an item will stay in the time log
 *      addChannelToLog  - Adds a channel to the time log mapping
 *      addTypeToLog     - Adds a throttle type to the time log mapping
 *      verifyExistance  -
 *
 * Note: Only commands marked with a * are available for use outside the object
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
    
    public ArrayList<String> getAllTypes(){
        ArrayList<String> types = new ArrayList<>();
        
        Set<String> logKeys = timeLog.keySet();
        Set<String> setKeys = this.keySet();
        
        Set<String> allSet = new HashSet<>();
        
        allSet.addAll(logKeys);
        
        System.out.println(allSet.size());
        
        allSet.addAll(setKeys);
        
        System.out.println(allSet.size());
        
        Iterator<String> keyIterator = allSet.iterator();
        
        while (keyIterator.hasNext()){
            
            String key = keyIterator.next();
            System.out.println(key);
            if (key.toLowerCase().endsWith("log")){
                
                System.out.println(key);
                key = key.split("log")[0];
                
                if (!types.contains(key)){
                    System.out.println(key);
                    types.add(key);
                }
            }
        }
        
        return types;
    }
    
//  Map <String, Map<String, LinkedList<Long>>> timeLog = Collections.synchronizedMap(new TreeMap<String, Map<String, LinkedList<Long>>>());
//        CHAN        TYPE     TIMELOG
    
    private void verifyExistance(String type, String channel){
        
        if (type.endsWith("time"))
            type = type.split("time")[0];
        
        if (type.endsWith("log"))
            type = type.split("log")[0];
        
        if (!timeLog.containsKey(channel)){
            addChannelToLog(channel);
        }
        
        if(!timeLog.get(channel).containsKey(type+"log")){
            addTypeToLog(channel,type);
        }
        
        if (!this.contains(type+"log",channel)){
            this.createMaxLog(type, String.valueOf(maxLog), channel);
        }
        
        if (!this.contains(type+"time",channel)){
            this.createMaxTime(type, String.valueOf(maxTime), channel);
        }
    }
    
    public boolean isThrottleActive(String type, String channel){
        Date d = new Date();
        
        verifyExistance(type,channel);
        
        int inputMaxLog = Integer.parseInt(this.get(type+"log",channel)); // Max number of logs for the input type
        int inputMaxTime = Integer.parseInt(this.get(type+"time",channel)); // Max number of logs for the input type
        long currentTime = d.getTime();
        
        
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
    
    private void createMaxTime(String type, String maxTime, String channel) {
        this.create(type+"time", maxTime, channel);
        System.out.println("YAY TIMES");
    }
    
    private void addChannelToLog(String channel){
        LinkedList<Long> logList = new LinkedList<>();
        Map<String,LinkedList<Long>> log = new TreeMap<>();
        String thing = new String();
        log.put(thing,logList);
        if (!timeLog.containsKey(channel))
            timeLog.put(channel, log);
    }
    
    private void createMaxLog(String type, String maxLog, String channel) {
        if (!this.contains(type+"log",channel))
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