/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Vector;

/**
 *
 * @author Steve-O
 */
public class Throttle {
    private LinkedList<Long> timeLog = new LinkedList<Long>();
    private int maxLog = 5;
    private long maxTime = 100*1000;
    String type;
    public Throttle (String name){
        this.type = name;
        
    }
    public void setMaxLog(int maxCalls){
        this.maxLog=maxCalls;
    }
    public void setMaxTime(int timeLimit){
        this.maxTime=timeLimit;
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
    public static class ThrottleArray extends Vector<Throttle>{
        public Throttle getThrottleByType(String get){
            int idx = -1;
            for(int i = 0; i < this.size(); i++) {
                if (this.get(i).type.equalsIgnoreCase(get)) {
                    idx = i;
                    break;
                }
            }
            if (idx==-1){
                this.add(new Throttle(get));
                idx = this.size();
            }
            return (this.get(idx));
        }
        public boolean containsType(String type){
            for(int i = 0; i < this.size(); i++) {
                if (this.get(i).type.equalsIgnoreCase(type)) {
                    return(true);
                }
            }
            return(false);
        }
        public ArrayList<String> getAllThrottleTypes(){
            ArrayList<String> typesContained = new ArrayList<>();
            for(int i = 0; i < this.size(); i++) {
                if (!typesContained.contains(this.get(i).type)) {
                    typesContained.add(this.get(i).type);
                }
            }
            return(typesContained);
        }
    }
}