/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Commands;

import Objects.Command;
import Objects.CommandMetaData;
import Wheatley.Global;
import com.google.common.collect.ImmutableSortedSet;
import java.util.ArrayList;
import java.util.Iterator;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;

/**
 *
 * @author Stephen
 */
public class SysInfoCMD implements Command {
    
    @Override
    public String toString(){
        return("SYSINFO");
    }
    @Override
    public ArrayList<String> commandTerms(){
        ArrayList<String> a = new ArrayList<>();
        a.add("sysinfo");
        a.add("ram");
        a.add("status");
        a.add("threads");
        return a;
    }
    @Override
    public boolean isCommand(String toCheck){
        return false;
    }
    
    @Override
    public void processCommand(Event event){
        
        CommandMetaData data = new CommandMetaData(event,true);
        String caller = data.getCaller();
        String channel = data.getEventChannel();
        String respondTo = new String();
        String message = data.getMessage();
        
        if (channel==null)
            respondTo = caller;
        else
            respondTo = channel;
        
        boolean isVerified = data.isVerifiedBotOwner();
        
        if(!isVerified){
            event.getBot().sendIRC().notice(caller, Colors.BOLD+"SysInfo: "+Colors.NORMAL+"You don't have access to this command");
        }
        else{
            
            if (message.equalsIgnoreCase("!ram")){
                int usedRam = (int) (Runtime.getRuntime().totalMemory()/1024/1024); //make it MB
                int freeRam = (int) (Runtime.getRuntime().freeMemory()/1024/1024);  //make it MB
                event.getBot().sendIRC().message(respondTo, "I am currently using "+usedRam+"MB ram, with "+freeRam+"MB ram free");
            }
            
            if (message.equalsIgnoreCase("!threads")){
                event.getBot().sendIRC().message(respondTo, "I am currently using "+Thread.activeCount()+" threads");
            }
            
            if (message.equalsIgnoreCase("!sysinfo")){
                int usedRam = (int) (Runtime.getRuntime().totalMemory()/1024/1024); //make it MB
                int freeRam = (int) (Runtime.getRuntime().freeMemory()/1024/1024);  //make it MB
                event.getBot().sendIRC().message(respondTo, Colors.BOLD+"Ram used: "+Colors.NORMAL+usedRam+"MB"+Colors.BOLD+" Ram free: "+Colors.NORMAL+freeRam+"MB"+Colors.BOLD+" Threads: "+Colors.NORMAL+Thread.activeCount());
            }
        }
    }
}
