/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.objects;

import java.io.File;
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
 *
 * Version: 0.5
 *
 */
public class Throttle extends Settings{
    
    private int maxLog = 5;
    private long maxTime = 100*1000;
    
    Map <String, Map<String, LinkedList<Long>>> timeLog = Collections.synchronizedMap(new TreeMap<String, Map<String, LinkedList<Long>>>(  ));
//String.CASE_INSENSITIVE_ORDER
    //   CHAN        TYPE     TIMELOG
    
    public Throttle(String filename){
        super(filename);
    }
    
    public Throttle(File file){
        super(file);
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
        allSet.addAll(setKeys);
        
        Iterator<String> keyIterator = allSet.iterator();
        
        while (keyIterator.hasNext()){
            
            String key = keyIterator.next();
            if (key.toLowerCase().endsWith("log")){
                
                key = key.split("log")[0];
                
                if (!types.contains(key)){
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
        
        
        if(timeLog.get(channel).get(type+"log").size() >= inputMaxLog) {
            while(timeLog.get(channel).get(type+"log").size()>0 && currentTime - timeLog.get(channel).get(type+"log").getLast() > inputMaxTime) {
                timeLog.get(channel).get(type+"log").pollLast();
            }
            if(timeLog.get(channel).get(type+"log").size() >= inputMaxLog) {
                return(true);
            }
        }
        timeLog.get(channel).get(type+"log").addFirst(d.getTime());
        return(false);
    }
    
    private void createMaxTime(String type, String maxTime, String channel) {
        this.create(type+"time", maxTime, channel);
    }
    
    private void addChannelToLog(String channel){
        LinkedList<Long> logList = new LinkedList<>();
        Map<String,LinkedList<Long>> log = new TreeMap<>();
        
        log.put(new String(),logList);
        if (!timeLog.containsKey(channel.toLowerCase()))
            timeLog.put(channel.toLowerCase(), log);
    }
    
    private void createMaxLog(String type, String maxLog, String channel) {
        if (!this.contains(type+"log",channel))
            this.create(type+"log", maxLog, channel);
    }
    
    private void addTypeToLog(String channel, String type) {
        
        LinkedList<Long> log = new LinkedList<>();
        
        timeLog.get(channel).put(type+"log", log);
    }
}