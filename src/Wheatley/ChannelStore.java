/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Steve-O
 * KIDDO'S LEARNING ERBJERCTS
 */
public class ChannelStore {
    String name;                             // The name of the channel
    int chance;                              // The chance of markov spamming lines to that channel (1/chance)
    boolean speak;                           // will markov speak?
    String previousMessage;                  // previous message sent in this channel
    String lastUrl;                          // last url seen in this channel
    String secondLastUrl;                    // second last url seen in this channel
    List<String> msgLog = new ArrayList<>(); // for use in s/find/replace commands
    
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
    public void addMsg(String msg){
        this.previousMessage = msg;
    }
}
