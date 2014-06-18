/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author Steve-O
 * ChannelStore Object and Corresponding ChannelArray Object
 * ->stores channel specific settings and logs
 * ->primarily oriented towards markov interface
 */
public class ChannelStore {
    String name;                             // The name of the channel
    int chance;                              // The chance of markov spamming lines to that channel (1/chance)
    boolean speak;                           // will markov speak?
    String previousMessage;                  // previous message sent in this channel
    String lastUrl;                          // last url seen in this channel
    String secondLastUrl;                    // second last url seen in this channel
    private List<String> msgLog = new ArrayList<>(); // for use in s/find/replace commands
    
    ChannelStore(String aName) {
        this.name = aName;
        this.chance = 100;
        this.speak = true;
        this.previousMessage = null;
        this.lastUrl = "";
        this.secondLastUrl = "";
        this.msgLog.add("");
    }
    @Override
    public String toString(){
        return(this.name);
    }
    public void addMessageToLog(String msg){
        this.msgLog.add(msg);
        if (msgLog.size()>100)
            this.msgLog.remove(this.msgLog.size()-1);
    }
    public int getMessageLogSize(){
        return(this.msgLog.size());
    }
    public String getMessage(int i){
        return(this.msgLog.get(i));
    }
    public static class ChannelArray extends Vector<ChannelStore>{
        public int getChanIdx(String toCheck){
            int idx = -1;
            for(int i = 0; i < this.size(); i++) {
                if (this.get(i).name.equalsIgnoreCase(toCheck)) {
                    idx = i;
                    break;
                }
            }
            if (idx==-1){
                this.add(new ChannelStore(toCheck));
                idx = this.size();
            }
            return (idx);
        }
        public ChannelStore getChan(String toCheck){
            int idx = -1;
            for(int i = 0; i < this.size(); i++) {
                if (this.get(i).name.equalsIgnoreCase(toCheck)) {
                    idx = i;
                    break;
                }
            }
            if (idx==-1){
                this.add(new ChannelStore(toCheck));
                idx = this.size();
            }
            return (this.get(idx));
        }
    }
}
