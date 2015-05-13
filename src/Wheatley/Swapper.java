/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ActionEvent;
import org.pircbotx.hooks.events.KickEvent;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 */
public class Swapper extends ListenerAdapter {
    Map<String,ArrayList<ArrayList<String>>> log = Collections.synchronizedMap(new TreeMap<String,ArrayList<ArrayList<String>>>());
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String channel = event.getChannel().getName();
        
        addToLog(channel, new ArrayList(Arrays.asList("<"+event.getUser().getNick()+">",event.getMessage())));
        
        if (message.toLowerCase().startsWith("sw/")||((message.toLowerCase().startsWith("s/")||message.toLowerCase().startsWith("sed/"))&&!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("BlarghleBot")).contains(event.getChannel()))){
            
            if (message.endsWith("/"))
                message+="poop";
            
            
            String[] findNreplace = Colors.removeFormattingAndColors(message).split("/");
            
            int i=log.get(channel).size()-2;
            
            synchronized(log){
                ArrayList<String> reply = findReplace(i, findNreplace, channel);
                
                if (reply.size()==2&&!reply.get(1).equalsIgnoreCase("")){
                    event.getBot().sendIRC().message(event.getChannel().getName(),reply.get(0)+" "+reply.get(1).substring(0,Math.min(reply.get(1).length(),400)));
                    addToLog(channel, reply);
                }
            }
        }
        
        if (message.startsWith(Global.commandPrefix)&&!message.matches("([ ]{0,}"+Global.commandPrefix+"{1,}[ ]{0,}){1,}")){
            
            String command = message.split(Global.commandPrefix)[1];
            String[] cmdSplit = command.split(" ");
            
            
            if (cmdSplit[0].equalsIgnoreCase("bf")){
                if (cmdSplit.length==2){
                    String nick = cmdSplit[1];
                    
                    int i=log.get(channel).size()-2;
                    boolean found = false;
                    String line = new String();
                    
                    synchronized(log){
                        while (!found&&i>=0){
                            if(log.get(event.getChannel().getName()).get(i).get(0).replaceAll("(<|>)", "").equalsIgnoreCase(nick)){
                                found = true;
                                nick = log.get(event.getChannel().getName()).get(i).get(0);
                                line = log.get(event.getChannel().getName()).get(i).get(1);
//                            System.out.println(nick + " " + line);
                            }
                            i--;
                        }
                    }
                    if (!found){
                        event.getBot().sendIRC().notice(event.getUser().getNick(), "!BF nick not found in log");
                    }
                    else{
                        String[] words = line.split(" ");
                        String newLine = "";
                        for (int c=words.length-1;c>=0;c--){
                            newLine+=words[c]+" ";
                        }
                        
                        addToLog(channel, new ArrayList(Arrays.asList(nick,newLine)));
                        event.getBot().sendIRC().message(event.getChannel().getName(), nick+" "+newLine);
                    }
                }
                else if (cmdSplit.length>2){
                    event.getBot().sendIRC().notice(event.getUser().getNick(), "!BF may take a nickname as input, or no input at all");
                }
                else{
                    String nick = log.get(event.getChannel().getName()).get(log.get(event.getChannel().getName()).size()-2).get(0);
                    String line = log.get(event.getChannel().getName()).get(log.get(event.getChannel().getName()).size()-2).get(1);
                    String[] words = line.split(" ");
                    String newLine = "";
                    for (int i=words.length-1;i>=0;i--){
                        newLine+=words[i]+" ";
                    }
                    event.getBot().sendIRC().message(event.getChannel().getName(), nick+" "+newLine);
                }
            }
        }
    }
    
    
    private ArrayList<String> findReplace(int i, String[] findNreplace, String channel){
        ArrayList<String> reply = new ArrayList<>();
        Boolean found = false;
        Pattern findThis = Pattern.compile(findNreplace[1]);
        
        while (i>=0&&!found){
            try{
//                System.out.println(log.get(channel).get(i).get(1));
                if (findThis.matcher(log.get(channel).get(i).get(1)).find()){
                    
                    reply = new ArrayList(Arrays.asList(log.get(channel).get(i).get(0),log.get(channel).get(i).get(1).replaceAll(findNreplace[1],findNreplace[2])));
                    
                    if(reply.get(1).startsWith("sw/")||reply.get(1).startsWith("s/")||reply.get(1).startsWith("sed/")){
                        i--;
                        if (reply.get(1).endsWith("/"))
                            reply.set(1, reply.get(1)+"poop");
                        findNreplace = reply.get(1).split("/");
                        reply = findReplace(i, findNreplace, channel);
                    }
                    
                    found = true;
                }
                i--;
            }catch (Exception ex){
                
            }
        }
        return(reply);
    }
    
    @Override
    public void onAction(ActionEvent event) {
        addToLog(event.getChannel().getName(), new ArrayList(Arrays.asList("* "+event.getUser().getNick(),event.getAction())));
    }
    
    @Override
    public void onKick(KickEvent event) {
        addToLog(event.getChannel().getName(), new ArrayList(Arrays.asList("* "+event.getUser().getNick(),"has kicked "+event.getRecipient().getNick()+" from "+event.getChannel().getName()+" ("+event.getReason()+")")));
    }
    
    
//    @Override
//    public void onNotice(NoticeEvent event) {
//        addToLog(event.getChannel().getName(), new ArrayList(Arrays.asList("-"+event.getUser().getNick()+"-",event.getNotice())));
//    }
    
    
    private void addToLog(String channel, ArrayList<String> message) {
        if (!log.containsKey(channel)){
//            ArrayList<String> message = new ArrayList<>();
            ArrayList<ArrayList<String>> channelLog = new ArrayList<>();
            channelLog.add(message);
            log.put(channel, channelLog);
        }
        else{
            log.get(channel).add(message);
            
            if (log.get(channel).size()>100){
                log.get(channel).remove(0);
            }
        }
    }
}
