/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Objects.MapArray;
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
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    N/A
 * - Linked Classes
 *    Global
 *
 * Activate Commands with:
 *      s/replaceThis/replaceWithThis
 *          Replaces the text replaceThis with the text replaceWithThis, using
 *          a log of previous messages
 *      !bf
 *      !bf [nick]
 *          Reverses the word order in the previous line said, or the previous line
 *          said by the input nickname
 *      !bff
 *      !bff [nick]
 *          Reverses all the letters in the previous line said, or the previous
 *          line said by the input nickname
 *      !bfff
 *      !bfff [nick]
 *          Reverses all the words, and all the letters in all the words, of the
 *          previous line said, or the previous line said by the input nickname
 */
public class Swapper extends ListenerAdapter {
//    Map<String,ArrayList<ArrayList<String>>> log = Collections.synchronizedMap(new TreeMap<String,ArrayList<ArrayList<String>>>());
    private static MapArray logger = new MapArray(100);
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String channel = event.getChannel().getName();
        
        addToLog(channel, new ArrayList(Arrays.asList("<"+event.getUser().getNick()+">",event.getMessage())));
        
        if (message.toLowerCase().startsWith("sw/")||((message.toLowerCase().startsWith("s/")||message.toLowerCase().startsWith("sed/"))&&!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("BlarghleBot")).contains(event.getChannel()))){
            
            if (message.endsWith("/"))
                message+="poop";
            
            ArrayList<ArrayList<String>> logCopy = logger.getArray(channel);
            String[] findNreplace = Colors.removeFormattingAndColors(message).split("/");
            
            int i = logCopy.size()-2;
            
            ArrayList<String> reply = findReplace(i, findNreplace, logCopy);
            
            if (reply.size()==2&&!reply.get(1).equalsIgnoreCase("")){
                event.getBot().sendIRC().message(event.getChannel().getName(),reply.get(0)+" "+reply.get(1).substring(0,Math.min(reply.get(1).length(),400)));
                addToLog(channel, reply);
            }
        }
        
        if (message.startsWith(Global.commandPrefix)&&!message.matches("([ ]{0,}"+Global.commandPrefix+"{1,}[ ]{0,}){1,}")){
            
            String command = message.split(Global.commandPrefix)[1];
            String[] cmdSplit = command.split(" ");
            
            
            if (cmdSplit[0].toLowerCase().matches("b[f]{1,}")){
                if (cmdSplit.length==2){
                    String nick = cmdSplit[1];
                    
                    ArrayList<ArrayList<String>> logCopy = logger.getArray(channel);
                    
                    int i=logCopy.size()-2;
                    boolean found = false;
                    String line = new String();
                    
                    while (!found && i >= 0){
                        if(logCopy.get(i).get(0).replaceAll("(<|>)", "").equalsIgnoreCase(nick)){
                            found = true;
                            nick = logCopy.get(i).get(0);
                            line = logCopy.get(i).get(1);
                        }
                        i--;
                    }
                    if (!found){
                        event.getBot().sendIRC().notice(event.getUser().getNick(), "!BF nick not found in log");
                    }
                    else{
                        if (cmdSplit[0].equalsIgnoreCase("bf")){
                            addToLog(channel, new ArrayList(Arrays.asList(nick,reverseWords(line))));
                            event.getBot().sendIRC().message(event.getChannel().getName(), nick+" "+reverseWords(line));
                        }
                        else if (cmdSplit[0].equalsIgnoreCase("bff")){
                            addToLog(channel, new ArrayList(Arrays.asList(nick,reverseAllLetters(line))));
                            event.getBot().sendIRC().message(event.getChannel().getName(), nick+" "+reverseAllLetters(line));
                        }
                        else {
                            addToLog(channel, new ArrayList(Arrays.asList(nick,reverseLettersAndWords(line))));
                            event.getBot().sendIRC().message(event.getChannel().getName(), nick+" "+reverseLettersAndWords(line));
                        }
                    }
                }
                else if (cmdSplit.length > 2){
                    event.getBot().sendIRC().notice(event.getUser().getNick(), "!BF may take a nickname as input, or no input at all");
                }
                else{
                    ArrayList<ArrayList<String>> logCopy = logger.getArray(channel);
                    String nick = logCopy.get(logCopy.size()-2).get(0);
                    String line = logCopy.get(logCopy.size()-2).get(1);
                    
                    if (cmdSplit[0].equalsIgnoreCase("bf")){
                        addToLog(channel, new ArrayList(Arrays.asList(nick,reverseWords(line))));
                        event.getBot().sendIRC().message(event.getChannel().getName(), nick+" "+reverseWords(line));
                    }
                    else if (cmdSplit[0].equalsIgnoreCase("bff")){
                        addToLog(channel, new ArrayList(Arrays.asList(nick,reverseAllLetters(line))));
                        event.getBot().sendIRC().message(event.getChannel().getName(), nick+" "+reverseAllLetters(line));
                    }
                    else {
                        addToLog(channel, new ArrayList(Arrays.asList(nick,reverseLettersAndWords(line))));
                        event.getBot().sendIRC().message(event.getChannel().getName(), nick+" "+reverseLettersAndWords(line));
                    }
                }
            }
        }
    }
    
    private String reverseWords(String line){
        String[] words = line.split(" ");
        String newLine = "";
        for (int c=words.length-1;c>=0;c--){
            newLine+=words[c]+" ";
        }
        return newLine;
    }
    
    private String reverseAllLetters(String line){
        String reverse = "";
        int length = line.length();
        
        for ( int i = length - 1 ; i >= 0 ; i-- )
            reverse = reverse + line.charAt(i);
        
        return reverse;
    }
    
    private String reverseLettersAndWords(String line){
        String reversedLine = reverseWords(line);
        String[] words = reversedLine.split(" ");
        String newLine = "";
        
        for (int i = 0; i<words.length; i++){
            words[i]=reverseAllLetters(words[i]);
        }
        
        for (int c=words.length-1;c>=0;c--){
            newLine+=words[c]+" ";
        }
        return newLine;
    }
    
    private ArrayList<String> findReplace(int i, String[] findNreplace, ArrayList<ArrayList<String>> logCopy){
        ArrayList<String> reply = new ArrayList<>();
        Boolean found = false;
        Pattern findThis = Pattern.compile(findNreplace[1]);
        
        while (i >= 0 && !found){
            try{
//                System.out.println(logCopy.get(i).get(1));
                if (findThis.matcher(logCopy.get(i).get(1)).find()){
                    
                    reply = new ArrayList(Arrays.asList(logCopy.get(i).get(0),logCopy.get(i).get(1).replaceAll(findNreplace[1],findNreplace[2])));
                    
                    if(reply.get(1).startsWith("sw/")||reply.get(1).startsWith("s/")||reply.get(1).startsWith("sed/")){
                        i--;
                        if (reply.get(1).endsWith("/"))
                            reply.set(1, reply.get(1)+"poop");
                        findNreplace = reply.get(1).split("/");
                        reply = findReplace(i, findNreplace, logCopy);
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
        logger.addToLog(channel, message);
    }
}
