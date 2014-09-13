/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import Objects.Throttle.ThrottleArray;

/**
 *
 * @author Steve-O
 * ChannelStore Object and Corresponding ChannelArray Object
 * ->stores channel specific settings and logs
 * ->primarily oriented towards markov interface
 */
public class ChannelStore {
    private String name;                             // The name of the channel
    private int chance;                              // The chance of markov spamming lines to that channel (1/chance)
    private boolean speak;                           // will markov speak?
    private String previousMessage;                  // previous message sent in this channel
    private String lastUrl;                          // last url seen in this channel
    private String secondLastUrl;                    // second last url seen in this channel
    private List<String> msgLog = new ArrayList<>();                       // for use in s/find/replace commands
    private ArrayList<String> gameChanBlocks = getBlockedGameChannels();   // List of channels that games are blocked from
    private boolean gamesBlocked;                                          // Are games currently blocked in this channel
    private ThrottleArray throttle = new ThrottleArray();
    
    public ChannelStore(String aName) {
        this.name = aName;
        this.chance = 100;
        this.speak = true;
        this.previousMessage = null;
        this.lastUrl = "";
        this.secondLastUrl = "";
        this.msgLog.add("");
        this.gamesBlocked = isChanBlocked();
    }
    @Override
    public String toString(){
        return(this.name);
    }
    public void addMessageToLog(String msg){
        this.msgLog.add(msg);
        if (msgLog.size()>100){
            //this.msgLog.remove(this.msgLog.size()-1);
            this.msgLog.remove(0);
        }
    }
    public boolean canSpeak(){
        return(this.speak);
    }
    public int getChance(){
        return(this.chance);
    }
    public String getChanName(){
        return(this.name);
    }
    public String getPreviousMessage(){
        return(this.previousMessage);
    }
    public void setSpeakValue(boolean val){
        this.speak=val;
    }
    public void setLastUrl(String Url){
        this.lastUrl = Url;
    }
    public void setSecondLastUrl(String Url){
        this.secondLastUrl = Url;
    }
    public void setPreviousMessage(String newMessage){
        this.previousMessage = newMessage;
    }
    public String getSecondLastUrl(){
        return(this.secondLastUrl);
    }
    public String getLastUrl(){
        return(this.lastUrl);
    }
    public void setChanceValue(int val){
        this.chance = val;
    }
    public int getMessageLogSize(){
        return(this.msgLog.size());
    }
    public String getMessage(int i){
        return(this.msgLog.get(i));
    }
    public String getMessage(){
        return(this.msgLog.get(this.msgLog.size()-1));
    }
    public boolean getGameBlockStatus(){
        return(this.gamesBlocked);
    }
    private ArrayList<String> getBlockedGameChannels() {
        ArrayList<String> channelsBlocked = new ArrayList<String>();
        
        channelsBlocked.add("#dtella");
        channelsBlocked.add("#dtella2.0");
        
        return(channelsBlocked);
    }
    public void addBlockedChannel(String channelName){
        this.gameChanBlocks.add(channelName);
    }
    
    private boolean isChanBlocked() {
        if(gameChanBlocks.contains(this.name))
            return(true);
        else{
            return(false);
        }
    }
    public void addThrottleSetting(String type){
        this.throttle.add(new Throttle(type));
    }
    public static class ChannelArray extends Vector<ChannelStore>{
        public void addThrottleToAll(String throttleType){
            for(int i = 0; i < this.size(); i++) {
                this.get(i).addThrottleSetting(throttleType);
            }
        }
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
        public boolean areGamesBlocked(String channel){
            return(getChan(channel).getGameBlockStatus());
        }
        public boolean containsChan(String toCheck){
            for(int i = 0; i < this.size(); i++) {
                if (this.get(i).name.equalsIgnoreCase(toCheck)) {
                    return(true);
                }
            }
            return (false);
        }
        public void safeAdd(String chan){
            if (!containsChan(chan)){
                this.add(new ChannelStore(chan));
            }
        }
        public void safeRemove(String chan){
            for(int i = 0; i < this.size(); i++) {
                if (this.get(i).name.equalsIgnoreCase(chan)) {
                    this.remove(i);
                    i--;
                }
            }
        }
        public void removeDupes(){
            ArrayList<String> typesContained = new ArrayList<>();
            for(int i = 0; i < this.size(); i++) {
                if (!typesContained.contains(this.get(i).name)) {
                    typesContained.add(this.get(i).name);
                }
                else if (typesContained.contains(this.get(i).name)){
                    this.remove(i);
                    i--;
                }
            }
        }
    }
}