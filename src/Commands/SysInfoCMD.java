/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Commands;

import Objects.Command;
import Objects.CommandMetaData;
import Utils.IRCUtils;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
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
    public ArrayList<String> commandTerms() {
        ArrayList<String> a = new ArrayList<>();
        a.add("sysinfo");
        a.add("ram");
        a.add("status");
        a.add("threads");
        a.add("uptime");
        return a;
    }
    @Override
    public boolean isCommand(String toCheck) {
        return false;
    }
    
    @Override
    public void processCommand(Event event) {
        
        CommandMetaData data = new CommandMetaData(event, false);
        String caller = data.getCaller();
        String channel = data.getEventChannel();
        String respondTo = new String();
        String message = data.getMessage();
        
        if (channel==null)
            respondTo = caller;
        else
            respondTo = channel;
        
//        boolean isVerified = data.isVerifiedBotOwner();
//        
//        if(!isVerified){
//            event.getBot().sendIRC().notice(caller, Colors.BOLD+"SysInfo: "+Colors.NORMAL+"You don't have access to this command");
//        }
//        else{
        
        if (message.equalsIgnoreCase("!ram")) {
            long heapUsed = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed() / 1024 / 1024; // Make it MB
            long heapMax = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax() / 1024 / 1024; // Make it MB
            int usedRam = (int) (Runtime.getRuntime().totalMemory() / 1024 / 1024); //make it MB
            int freeRam = (int) (Runtime.getRuntime().freeMemory() / 1024 / 1024);  //make it MB
            
            event.getBot().sendIRC().message(respondTo, Colors.BOLD + "RAM Used: " + Colors.NORMAL + usedRam+"MB" +
                    Colors.BOLD + " RAM Free: " + Colors.NORMAL +freeRam+"MB" +
                    Colors.BOLD + " Heap Used: " + Colors.NORMAL + heapUsed + "MB" +
                    Colors.BOLD + " Max Heap: " + Colors.NORMAL + heapMax + "MB");
        }
        
        if (message.equalsIgnoreCase("!threads")) {
            int peakThreads = ManagementFactory.getThreadMXBean().getPeakThreadCount();
            
            long[] threadIDs = ManagementFactory.getThreadMXBean().getAllThreadIds();
            long cpuTime = 0;
            long userTime = 0;
            for (int i = 0; i < threadIDs.length; i++) { // Add up all the currently active threads cpu/user times
                cpuTime += ManagementFactory.getThreadMXBean().getThreadCpuTime(threadIDs[i]);
                userTime += ManagementFactory.getThreadMXBean().getThreadUserTime(threadIDs[i]);
            }
            
            cpuTime = cpuTime / (long) 1E6; // Convert nano seconds to seconds
            userTime = userTime / (long) 1E6; // Convert nano seconds to seconds
            
            long totalStartedThreadCount = ManagementFactory.getThreadMXBean().getTotalStartedThreadCount();
            
            event.getBot().sendIRC().message(respondTo, Colors.BOLD + "Active Threads: " + Colors.NORMAL +Thread.activeCount()+
                    Colors.BOLD + " Peak Thread Count: " + Colors.NORMAL + peakThreads +
                    Colors.BOLD + " Total Threads Started: " + Colors.NORMAL + totalStartedThreadCount +
                    Colors.BOLD + " Active Thread CPU time: " + Colors.NORMAL + IRCUtils.millisToPrettyPrintTime(cpuTime) +
                    Colors.BOLD + " Active Thread User time: " + Colors.NORMAL + IRCUtils.millisToPrettyPrintTime(userTime));
        }
        
        if (message.equalsIgnoreCase("!sysinfo")) {
            int usedRam = (int) (Runtime.getRuntime().totalMemory()/1024/1024); //make it MB
            int freeRam = (int) (Runtime.getRuntime().freeMemory()/1024/1024);  //make it MB
            int cpuCores = Runtime.getRuntime().availableProcessors();
            event.getBot().sendIRC().message(respondTo, Colors.BOLD+"RAM used: "+Colors.NORMAL+usedRam+"MB"+
                    Colors.BOLD+" RAM free: "+Colors.NORMAL+freeRam+"MB"+
                    Colors.BOLD+" Threads: "+Colors.NORMAL+Thread.activeCount() +
                    Colors.BOLD + " Cores Available: " + Colors.NORMAL + cpuCores);
        }
        
        if (message.equalsIgnoreCase("!uptime")) {
            long jvmUpTime = ManagementFactory.getRuntimeMXBean().getUptime();
            event.getBot().sendIRC().message(respondTo, Colors.BOLD+"Uptime: "+Colors.NORMAL+IRCUtils.millisToPrettyPrintTime(jvmUpTime)+"");
        }
//        }
    }
}
