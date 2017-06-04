/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package rapternet.irc.bots.wheatley.objects;

import java.util.Vector;

/**
 *
 * @author stephen
 *
 *
 *
 */
public class BanLog {
    private String hostmask;
    private String banner;
    private String time;
    private String channel;
    
    public BanLog(String hostmask, String channel){
        this.hostmask = hostmask;
        this.channel = channel;
    }
    public BanLog(String hostmask, String channel,String banner){
        this.hostmask=hostmask;
        this.banner=banner;
        this.channel = channel;
    }
    public BanLog(String hostmask, String channel,String banner, String time){
        this.hostmask=hostmask;
        this.banner=banner;
        this.time=time;
        this.channel=channel;
    }
    
    private String getHostmask(){
        return this.hostmask;
    }
    private String getChannel(){
        return this.channel;
    }
    public String getFormattedResponse(){
        return(this.hostmask+" was banned by "+this.banner+" from "+this.channel+" at "+this.time);
    }
    public String getSimpleInfo(){
        return("Hostmask: "+this.hostmask+" Channel: "+this.channel + " From: "+this.time);
    }
    public static class BanList extends Vector<BanLog>{
        
        public BanLog getBan(String hostmask,String channel){
            return (this.get(this.getBanIdx(hostmask, channel)));
        }
        public int getBanIdx(String hostmask, String channel){
            int idx = -1;
            for(int i = 0; i < this.size(); i++) {
                if (this.get(i).getHostmask().equalsIgnoreCase(hostmask)&&this.get(i).getChannel().equalsIgnoreCase(channel)) {
                    idx = i;
                    break;
                }
            }
            return (idx);
        }
        public void safeAdd(String hostmask, String channel){ // more like quick add
            if (!containsBan(hostmask, channel)){
                this.add(new BanLog(hostmask, channel));
            }
        }
        public void safeAdd(BanLog ban){
            if (!containsBan(ban.getHostmask(), ban.getChannel())){
                this.add(ban);
            }
        }
        public void safeRemove(String hostmask, String channel){
            for(int i = 0; i < this.size(); i++) {
                if (this.get(i).getHostmask().equalsIgnoreCase(hostmask)&&this.get(i).getChannel().equalsIgnoreCase(channel)) {
                    this.remove(i);
                    i--;
                }
            }
        }
        public boolean containsBan(String hostmask, String channel){
            for(int i = 0; i < this.size(); i++) {
                if (this.get(i).getHostmask().equalsIgnoreCase(hostmask)&&this.get(i).getChannel().equalsIgnoreCase(channel)) {
                    return(true);
                }
            }
            return (false);
        }
    }
}
