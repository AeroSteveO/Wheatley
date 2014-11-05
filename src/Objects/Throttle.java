/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects;

import java.util.Date;
import java.util.LinkedList;

/**
 *
 * @author Steve-O
 * 
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    N/A
 * - Linked Classes
 *    N/A
 * 
 */
public class Throttle {
    private LinkedList<Long> timeLog = new LinkedList<Long>();
    private int maxLog = 5;
    private long maxTime = 100*1000;
    private String channel;
    private String type;
    
    public Throttle (String name){
        this.type = name;
    }
    
    public Throttle (String name, String channel){
        this.type = name;
        this.channel = channel;
    }
    
    public Throttle (){
        this.type = "general";
    }
    
    public void setMaxLog(int maxCalls){
        this.maxLog=maxCalls;
    }
    
    public void setMaxTime(int timeLimit){
        this.maxTime=timeLimit;
    }
    
    public String getChannel(){
        return(this.channel);
    }
    
    public String getType(){
        return (this.type);
    }
    
    public boolean isThrottleActive(){
        Date d = new Date();
        long currentTime = d.getTime();
        if(timeLog.size() > maxLog) {
            while(timeLog.size()>0 && currentTime - timeLog.getLast() > maxTime) {
                timeLog.pollLast();
            }
            if(timeLog.size()>maxLog) {
                return(true);
            }
        }
        return(false);
    }
}