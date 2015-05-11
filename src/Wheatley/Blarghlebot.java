/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import com.google.common.collect.ImmutableSortedSet;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.*;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.User;
import org.pircbotx.hooks.*;
import org.pircbotx.hooks.events.*;

/**
 *
 * @author Steve-O
 * original bot functions by Blarghedy
 * Who's lazy and doesn't run his bot much
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
 *          Replaces the text replaceThis with the text replaceWithThis, using the log of previous messages in Global.channels
 *      !roll xdy
 *          Rolls x number of dy dice and outputs the result
 *      !russianroulette
 *          Play a game of Russian Roulette with the bot
 *      !g [terms to google]
 *          Pulls up let me google that for you, to tell people to google things
 *      ![number]
 *          Pulls up a link for the dtella irc quote with that number
 *      !xkcdb [number]
 *          Pulls up a link for the xkcdb quote with that number
 *      !bash [number]
 *          Pulls up a link for the bash.org quote with that number
 *
 */
public class Blarghlebot extends ListenerAdapter {
    int shoot = 0;
    static String poop = "null";
    Map<String,ArrayList<ArrayList<String>>> log = new TreeMap<>();
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String channel = event.getChannel().getName();
        
        addToLog(channel, new ArrayList(Arrays.asList("<"+event.getUser().getNick()+">",message)));
        
        if (message.toLowerCase().startsWith("sw/")||((message.toLowerCase().startsWith("s/")||message.toLowerCase().startsWith("sed/"))&&!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("BlarghleBot")).contains(event.getChannel()))){
            
            if (message.endsWith("/"))
                    message+="poop";
            
            String[] findNreplace = Colors.removeFormattingAndColors(message.toLowerCase()).split("/");
            
            int i=log.get(channel).size()-2;
            ArrayList<String> reply = findReplace(i, findNreplace, channel);
            if (reply.size()==2&&!reply.get(1).equalsIgnoreCase("")){
                event.getBot().sendIRC().message(event.getChannel().getName(),reply.get(0)+" "+reply.get(1).substring(0,Math.min(reply.get(1).length(),400)));
                addToLog(channel, reply);
            }
        }
        if ((message.toLowerCase().startsWith(Global.mainNick.toLowerCase()+", ")&&!message.toLowerCase().startsWith(Global.mainNick.toLowerCase()+", why")&&!message.toLowerCase().startsWith(Global.mainNick.toLowerCase()+", what do you think of")&&message.endsWith("?"))){
            //Messages from : http://en.wikipedia.org/wiki/Magic_8-Ball
            List<String> magic = new ArrayList<>();
            magic.add( "It is certain" );
            magic.add( "It is decidedly so" );
            magic.add( "Without a doubt");
            magic.add( "Yes definitely");
            magic.add( "You may rely on it" );
            magic.add( "As I see it, yes" );
            magic.add( "Most likely");
            magic.add( "Outlook good");
            magic.add( "Yes" );
            magic.add( "Signs point to yes" );
            magic.add( "Reply hazy try again");
            magic.add( "Ask again later");
            magic.add( "Better not tell you now" );
            magic.add( "Cannot predict now" );
            magic.add( "Concentrate and ask again");
            magic.add( "Don't count on it");
            magic.add( "My reply is no" );
            magic.add( "My sources say no" );
            magic.add( "Outlook not so good");
            magic.add( "Very doubtful");
            event.respond(magic.get((int) (Math.random()*magic.size()-1)));
        }
        
        if (!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("BlarghleBot")).contains(event.getChannel())) {
                        
            if (message.equalsIgnoreCase("!users")||message.equalsIgnoreCase("!dtellausers")){
                int totalUsers = 0;
                int dtellaChanUsers = 0;
                int dtellaUsers = 0;
                ImmutableSortedSet users = event.getBot().getUserChannelDao().getAllUsers();
                
                Iterator<User> iterator = users.iterator();
                while(iterator.hasNext()) {
                    User user = iterator.next();
//                    if (user.getNick().startsWith("|")){
//                        dtellaUsers++;
//                    }
                    if (user.getServer().toLowerCase().matches(".*bridge.*")&&!user.getNick().equalsIgnoreCase("***REMOVED***")){
                        dtellaUsers++;
                    }
                    Iterator<Channel> chanIterator = user.getChannels().iterator();
                    while (chanIterator.hasNext()){
                        Channel chanElement = chanIterator.next();
                        if(chanElement.getName().equalsIgnoreCase("#dtella")){
                            dtellaChanUsers++;
                            break;
                        }
                    }
                    if (user.getChannels().size()>0)
                        totalUsers++;
                }
                event.getBot().sendIRC().message(event.getChannel().getName(),Colors.BOLD+"Dtella Shares: "+Colors.NORMAL+(dtellaUsers)+Colors.BOLD+" #Dtella Users: "+Colors.NORMAL+dtellaChanUsers+Colors.BOLD+" Total Visible Users "+Colors.NORMAL+totalUsers);
            }
            
            //KICKS ON KICKS ON KICKS
            if ((message.equalsIgnoreCase("Blarghlebot, transform and rollout"))||(message.equalsIgnoreCase(Global.mainNick+", transform and rollout"))) {
                switch((int) (Math.random()*3+1)) {
                    case 1:
                        event.getBot().sendIRC().action(event.getChannel().getName(),"transforms into CherryBot");
                        event.getBot().sendIRC().action(event.getChannel().getName(),"charges his cherry bomb...");
                        event.getChannel().send().kick(event.getUser(), "CHERRY ASPLOSION");
                        break;
                    case 2:
                        event.getBot().sendIRC().action(event.getChannel().getName(),"transforms into PeachBot");
                        event.getBot().sendIRC().action(event.getChannel().getName(),"charges his peachfuzz...");
                        event.getChannel().send().kick(event.getUser(), "EAT FUZZ");
                        break;
                    case 3:
                        event.getBot().sendIRC().action(event.getChannel().getName(),"transforms into AppleBot");
                        event.getBot().sendIRC().action(event.getChannel().getName(),"charges his apple cannon...");
                        event.getChannel().send().kick(event.getUser(), "EAT PIE");
                        break;
                }
            }
            //OTHER Functions
            
            if (message.equalsIgnoreCase("i put on my robe and wizard hat"))
                event.getChannel().send().kick(event.getUser(), "LIGHTNING BOLT");
            else if (message.equalsIgnoreCase("ba dum")||message.equalsIgnoreCase("badum"))
                event.getBot().sendIRC().message(event.getChannel().getName(), "psh");
            
            if (message.startsWith(Global.commandPrefix)&&!message.matches("([ ]{0,}"+Global.commandPrefix+"{1,}[ ]{0,}){1,}")){
                
                String command = message.split(Global.commandPrefix)[1];
                String[] cmdSplit = command.split(" ");
                
                if (command.equalsIgnoreCase("banme")){
                    event.getBot().sendRaw().rawLineNow("tban " + event.getChannel().getName() + " 1m " + event.getUser().getNick() + "!*@*");
                    event.getChannel().send().kick(event.getUser(), "You're Welcome");
                }
                
                else if (command.equalsIgnoreCase("suicide"))
                    event.getChannel().send().kick(event.getUser(), "SOMETHING WITTY ABOUT DYING");
                
                else if (command.equalsIgnoreCase("kickme"))
                    event.getChannel().send().kick(event.getUser(), "you += dead");
                if (cmdSplit[0].toLowerCase().startsWith("troll")){
                    if((event.getUser().getNick().equalsIgnoreCase(Global.botOwner)||event.getChannel().isOwner(event.getUser()))&&event.getUser().isVerified()){
//                        String[] kill = message.split(" ");
                        event.getChannel().send().kick(event.getBot().getUserChannelDao().getUser(cmdSplit[1]),"YOO GAWT TROLLED " + cmdSplit[1]);
                    }
                    else{
                        event.getChannel().send().kick(event.getUser(),"YOO GAWT TROLLED " + event.getUser().getNick());
                    }
                }
                
                //Mildly Useful functions
                else if (cmdSplit[0].equalsIgnoreCase("g")) {
                    String searchquery;
                    searchquery = message.substring(message.indexOf(" ") + 1);
                    String url = "http://lmgtfy.com/?q=" + URLEncoder.encode(searchquery, "UTF-8");
                    event.respond(url);
                }
                
                else if (command.equalsIgnoreCase("russianroulette")) {
                    if (shoot==0){
                        shoot = (int) (Math.random()*6+1);
                        if (shoot==1){
                            event.getChannel().send().kick(event.getUser(), "BANG");
                            event.getBot().sendIRC().message(event.getChannel().getName(), "-Reloaded-");
                            shoot = 0;
                        }
                        else{
                            event.getBot().sendIRC().message(event.getChannel().getName(), "-click-");
                        }
                    }
                    else{
                        shoot = shoot - 1;
                        if (shoot==1){
                            event.getChannel().send().kick(event.getUser(), "BANG");
                            event.getBot().sendIRC().message(event.getChannel().getName(), "-Reloaded-");
                            shoot = 0;
                        }
                        else{
                            event.getBot().sendIRC().message(event.getChannel().getName(), "-click-");
                        }
                    }
                }
                
                else if (command.equalsIgnoreCase("headon")){
                    event.getBot().sendIRC().message(event.getChannel().getName(), "APPLY DIRECTLY TO FOREHEAD");
                    event.getBot().sendIRC().message(event.getChannel().getName(), "APPLY DIRECTLY TO FOREHEAD");
                    event.getBot().sendIRC().message(event.getChannel().getName(), "APPLY DIRECTLY TO FOREHEAD");
                }
                
                //Functions Using REGEX
                else if (Pattern.matches("!hm[m]+", message.toLowerCase()))
                    //<BlarghleBot> Vanilla, the old what for quite so cheerio good chap good sir why certainly old bean
                    //<BlarghleBot> Vanilla, I say good chap indeed verily why certainly old bean good sir
                    //<BlarghleBot> Steve-O, hm yes good sir quite so cheerio the old what for why certainly good chap
                    event.getBot().sendIRC().message(event.getChannel().getName(), event.getUser().getNick()+", good sir old bean good chap verily mm why certainly the old what for");//indeed good chap the old what for ah yes hm yes good sir old bean
                
                else if (Pattern.matches("!trol[ol]+", message.toLowerCase())||Pattern.matches("!trolo[lo]+", message.toLowerCase()))
                    event.getBot().sendIRC().message(event.getChannel().getName(), "http://youtu.be/v1PBptSDIh8");
                // REGEX for creating IRC quote links
                else if (Pattern.matches("![0-9]+", message))
                    event.getBot().sendIRC().message(event.getChannel().getName(), "http://quotes.dtella.org/?quote="+message.split("!")[1]);
                
                else if (command.equalsIgnoreCase("passthepoop")) {
                    if ("null".equals(poop)){
                        poop = event.getUser().getNick();
                    }
                    else if (event.getUser().getNick().equals(poop)) {
                        event.getBot().sendIRC().message(event.getChannel().getName(), "You can't pass the poop with yourself!");
                        poop = "null";
                    }
                    else {
                        event.getBot().sendIRC().message(event.getChannel().getName(), "Pass the poop!  " + poop + " will poop into " + event.getUser().getNick() + "'s butthole, and then " + event.getUser().getNick() + " will poop it back... into " + poop + "'s butthole.  And then they'll just keep doing it back and forth... forever.");
                        poop = "null";
                    }
                }
                //<Blarghedy> !roll 5d5+4d4+3d3+2d2+8*9d3
                //<BlarghleBot> Blarghedy rolled 16, 8, 5, 3, 22, 23, 17, 19, 21, 15, 21, 18 for a total of 188
                //<Blarghedy> !roll 5*3d3
                //<BlarghleBot> Blarghedy rolled 2 1 3 : 6, 3 2 3 : 8, 3 1 3 : 7, 1 2 2 : 5, 1 1 2 : 4, for a total of 30
                //<Blarghedy> !roll 1000d1000
                //<BlarghleBot> Blarghedy rolled 506063 for a total of 506063
                else if (cmdSplit[0].equalsIgnoreCase("roll")&&Pattern.matches("[0-9]{1,2}?d[0-9]{1,3}?", cmdSplit[1])){
                    String[] rolls = message.split(" ")[1].split("d");
                    String dice = "You rolled: ";
                    int total=0;
                    int temp=(int) (Math.random()*Integer.parseInt(rolls[1]) + 1);
                    total = temp+total;
                    dice = dice + Integer.toString(temp);
                    for (int i=1; i<=Integer.parseInt(rolls[0])-1;i++){
                        temp = (int)(Math.random() * Integer.parseInt(rolls[1]) + 1);
                        total = temp + total;
                        dice = dice + ", " + Integer.toString(temp);
                    }
                    event.getBot().sendIRC().message(event.getChannel().getName(),dice + " for a total of " + Integer.toString(total));
                }
                else if (cmdSplit[0].equalsIgnoreCase("roll") && Pattern.matches("[0-9]+d[0-9]+", cmdSplit[1])){
                    event.getBot().sendIRC().message(event.getChannel().getName(),"NO");
                }
                
                else if (cmdSplit[0].equalsIgnoreCase("xzibit")&&(cmdSplit.length==3)){
                    event.getBot().sendIRC().message(event.getChannel().getName(),"Yo dawg I heard you like " + cmdSplit[1] + " so I put an " + cmdSplit[1] + " in your " + cmdSplit[2] + " so you can " + cmdSplit[1] + " while you " + cmdSplit[2] + ".");
                }
                //<Evidlo> [15:02:31] !yodawg b a
                // <BlarghleBot> [15:02:31] Yo dawg I heard you like bs so I put an b in your a so you can b while you a.
                //DUMB CHAT
                else if (command.equalsIgnoreCase("burn"))
                    event.getBot().sendIRC().message(event.getChannel().getName(), "http://quotes.dtella.org/?quote=1076");
                
                else if (command.equalsIgnoreCase("udon"))
                    event.getBot().sendIRC().message(event.getChannel().getName(), "http://quotes.dtella.org/?quote=1019");
                
                else if (command.equalsIgnoreCase("rimshot"))
                    event.getBot().sendIRC().message(event.getChannel().getName(), "http://www.instantrimshot.com/");
                
                else if (command.equalsIgnoreCase("clitoris"))
                    event.getBot().sendIRC().message(event.getChannel().getName(), "http://quotes.dtella.org/?quote=1050");
                
                else if (command.equalsIgnoreCase("vuvuzela"))
                    event.getBot().sendIRC().message(event.getChannel().getName(), "BZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
                
                else if (cmdSplit[0].equalsIgnoreCase("bash")&&Pattern.matches("[0-9]+", cmdSplit[1]))
                    event.getBot().sendIRC().message(event.getChannel().getName(), "http://bash.org/?"+message.split(" ")[1]);
                
                else if (cmdSplit[0].equalsIgnoreCase("xkcdb")&&Pattern.matches("[0-9]+", cmdSplit[1]))
                    event.getBot().sendIRC().message(event.getChannel().getName(), "http://www.xkcdb.com/"+message.split(" ")[1]);
                
                else if (command.equalsIgnoreCase("dropthebass"))
                    event.getBot().sendIRC().message(event.getChannel().getName(),"WUB WUB WUBWUBWUBWUB WUB WUB");
            }
        }
    }
    private ArrayList<String> findReplace(int i, String[] findNreplace, String channel){
        ArrayList<String> reply = new ArrayList<>();
        Boolean found = false;
        Pattern findThis = Pattern.compile(findNreplace[1]);
        
        while (i>=0&&!found){
            try{
                System.out.println(log.get(channel).get(i).get(1));
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
        System.out.println(event.getChannel().getName() + ", "+ event.getUser().getNick()+", "+event.getReason());
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