/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;
import org.pircbotx.Colors;
import org.pircbotx.hooks.*;
import org.pircbotx.hooks.events.*;

/**
 *
 * @author Steve-O
 * original bot functions by Blarghedy
 * Who's lazy and doesn't run his bot much
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
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        int idx = Global.channels.getChanIdx(event.getChannel().getName());
        Global.channels.get(idx).addMessageToLog("<"+event.getUser().getNick()+"> "+message.toLowerCase());
        
        if (!event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser("BlarghleBot")).contains(event.getChannel())) {
            String[] messageArray = message.split(" ");
            //<Evidlo> re.split('(?<!\\\\)/','hello\/world/hello')
            //<Evidlo> Whenever python sees \/, it changes it to \\/
            //<Evidlo> So thats why theres \\\\
            if (message.toLowerCase().startsWith("s/")||message.toLowerCase().startsWith("sed/")){
                String[] findNreplace = Colors.removeFormattingAndColors(message.toLowerCase()).split("/");
                Pattern findThis = Pattern.compile(findNreplace[1]);
                String reply = "";
                int i=Global.channels.get(idx).getMessageLogSize()-2;
                reply = findReplace(i, findNreplace, findThis, idx);
                if (!reply.equalsIgnoreCase("")){
                    event.getBot().sendIRC().message(event.getChannel().getName(),reply.substring(0,Math.min(reply.length(),400)));
                    Global.channels.get(idx).addMessageToLog(reply);
                }
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
            if (message.equalsIgnoreCase("!banme")){
                event.getBot().sendRaw().rawLineNow("tban " + event.getChannel().getName() + " 1m " + event.getUser().getNick() + "!*@*");
                event.getChannel().send().kick(event.getUser(), "You're Welcome");
            }
            
            if (message.equalsIgnoreCase("i put on my robe and wizard hat"))
                event.getChannel().send().kick(event.getUser(), "LIGHTNING BOLT");
            
            if (message.equalsIgnoreCase("!suicide"))
                event.getChannel().send().kick(event.getUser(), "SOMETHING WITTY ABOUT DYING");
            
            if (message.equalsIgnoreCase("!kickme"))
                event.getChannel().send().kick(event.getUser(), "you += dead");
            
            if (message.toLowerCase().startsWith("!troll")){
                if((event.getUser().getNick().equalsIgnoreCase(Global.botOwner)||event.getChannel().isOwner(event.getUser()))&&event.getUser().isVerified()){
                    String[] kill = message.split(" ");
                    event.getChannel().send().kick(event.getBot().getUserChannelDao().getUser(kill[1]),"YOO GAWT TROLLED " + kill[1]);
                }
                else{
                    event.getChannel().send().kick(event.getUser(),"YOO GAWT TROLLED " + event.getUser());
                }
            }
            
            //Mildly Useful functions
            if (messageArray[0].equalsIgnoreCase("!g")) {
                String searchquery;
                searchquery = message.substring(message.indexOf(" ") + 1);
                String url = "http://lmgtfy.com/?q=" + URLEncoder.encode(searchquery, "UTF-8");
                event.respond(url);
            }
            
            if (message.equalsIgnoreCase("!russianroulette")) {
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
            
            if (message.equalsIgnoreCase("!headon")){
                event.getBot().sendIRC().message(event.getChannel().getName(), "APPLY DIRECTLY TO FOREHEAD");
                event.getBot().sendIRC().message(event.getChannel().getName(), "APPLY DIRECTLY TO FOREHEAD");
                event.getBot().sendIRC().message(event.getChannel().getName(), "APPLY DIRECTLY TO FOREHEAD");
            }
            
            //Functions Using REGEX
            if (Pattern.matches("!hm[m]+", message.toLowerCase()))
                //<BlarghleBot> Vanilla, the old what for quite so cheerio good chap good sir why certainly old bean
                //<BlarghleBot> Vanilla, I say good chap indeed verily why certainly old bean good sir
                //<BlarghleBot> Steve-O, hm yes good sir quite so cheerio the old what for why certainly good chap
                event.getBot().sendIRC().message(event.getChannel().getName(), event.getUser().getNick()+", good sir old bean good chap verily mm why certainly the old what for");//indeed good chap the old what for ah yes hm yes good sir old bean
            
            if (Pattern.matches("!trol[ol]+", message.toLowerCase())||Pattern.matches("!trolo[lo]+", message.toLowerCase()))
                event.getBot().sendIRC().message(event.getChannel().getName(), "http://youtu.be/v1PBptSDIh8");
            // REGEX for creating IRC quote links
            if (Pattern.matches("![0-9]+", message))
                event.getBot().sendIRC().message(event.getChannel().getName(), "http://quotes.dtella.org/?quote="+message.split("!")[1]);
            
            //OTHER Functions
            if ((message.toLowerCase().startsWith("blarghlebot, ")&&message.endsWith("?"))||(message.toLowerCase().startsWith(Global.mainNick.toLowerCase()+", ")&&!message.toLowerCase().startsWith(Global.mainNick.toLowerCase()+", why")&&!message.toLowerCase().startsWith(Global.mainNick.toLowerCase()+", what do you think of")&&message.endsWith("?"))){
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
            
            if (message.equalsIgnoreCase("!passthepoop")) {
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
            if (message.startsWith("!roll")&&(Pattern.matches("!roll [0-9]{1,2}?d[0-9]{1,3}?", message.toLowerCase()))){
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
            else if (message.toLowerCase().startsWith("!roll")&&(Pattern.matches("!roll [0-9]+d[0-9]+", message.toLowerCase()))){
                event.getBot().sendIRC().message(event.getChannel().getName(),"NO");
            }
            
            if (message.toLowerCase().startsWith("!xzibit")&&(Pattern.matches("!xzibit [a-zA-Z]+ [a-zA-Z]+", message.toLowerCase()))){
                event.getBot().sendIRC().message(event.getChannel().getName(),"Yo dawg I heard you like " + messageArray[1] + " so I put an " + messageArray[1] + " in your " + messageArray[2] + " so you can " + messageArray[1] + " while you " + messageArray[2] + ".");
            }
            //<Evidlo> [15:02:31] !yodawg b a
            // <BlarghleBot> [15:02:31] Yo dawg I heard you like bs so I put an b in your a so you can b while you a.
            //DUMB CHAT
            if (message.equalsIgnoreCase("!burn"))
                event.getBot().sendIRC().message(event.getChannel().getName(), "http://quotes.dtella.org/?quote=1076");
            
            if (message.equalsIgnoreCase("!udon"))
                event.getBot().sendIRC().message(event.getChannel().getName(), "http://quotes.dtella.org/?quote=1019");
            
            if (message.equalsIgnoreCase("!rimshot"))
                event.getBot().sendIRC().message(event.getChannel().getName(), "http://www.instantrimshot.com/");
            
            if (message.equalsIgnoreCase("!clitoris"))
                event.getBot().sendIRC().message(event.getChannel().getName(), "http://quotes.dtella.org/?quote=1050");
            
            if (message.equalsIgnoreCase("!vuvuzela"))
                event.getBot().sendIRC().message(event.getChannel().getName(), "BZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
            
            if (message.equalsIgnoreCase("ba dum")||message.equalsIgnoreCase("badum"))
                event.getBot().sendIRC().message(event.getChannel().getName(), "psh");
        }
        if (Pattern.matches("!bash [0-9]+", message))
            event.getBot().sendIRC().message(event.getChannel().getName(), "http://bash.org/?"+message.split(" ")[1]);
        
        if (Pattern.matches("!xkcdb [0-9]+", message))
            event.getBot().sendIRC().message(event.getChannel().getName(), "http://www.xkcdb.com/"+message.split(" ")[1]);
        
        if (message.equalsIgnoreCase("!dropthebass"))
            event.getBot().sendIRC().message(event.getChannel().getName(),"WUB WUB WUBWUBWUBWUB WUB WUB");
        
    }
    private String findReplace(int i, String[] findNreplace, Pattern findThis, int idx){
        String reply="";
        String[] backReply;
        Boolean found = false;
        
        while (i>=0&&!found){
            if (findThis.matcher(Global.channels.get(idx).getMessage(i).split(" ",2)[1]).find()){
//                String[] temp = reply.split(" ",2);
                reply = Global.channels.get(idx).getMessage(i).split(" ",2)[0]+" "+Global.channels.get(idx).getMessage(i).split(" ",2)[1].replaceAll(findNreplace[1],findNreplace[2]);
                backReply = reply.split(" ");
                if (backReply.length>1){
                    if(backReply[1].startsWith("s/")||backReply[1].startsWith("sed/")){
                        i--;
                        reply=backReply[1];
                        for(int c = 2;c<backReply.length;c++){
                            reply = reply +" "+ backReply[c];
                        }
                        findNreplace = reply.split("/");
                        findThis = Pattern.compile(findNreplace[1]);
                        reply = findReplace(i, findNreplace, findThis, idx);
                    }
                }
                found = true;
            }
            i--;
        }
        return(reply);
    }
}