/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.pircbotx.Colors;
import org.pircbotx.hooks.*;
import org.pircbotx.hooks.events.*;

/**
 *
 * @author Steve-O
 * original bot functions by Blarghedy
 * Who's lazy and doesn't run his bot much
 */
public class Blarghlebot extends ListenerAdapter {
    int shoot = 0;
    static String poop = "null";
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String[] messageArray = Colors.removeFormattingAndColors(event.getMessage()).split(" ");
        if ((message.equalsIgnoreCase("Blarghlebot, transform and rollout"))||(message.equalsIgnoreCase(Global.MainNick+", transform and rollout"))) {
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
        if (messageArray[0].equalsIgnoreCase("!g")) {
            String searchquery;
            searchquery = message.substring(message.indexOf(" ") + 1);
            String url = "http://lmgtfy.com/?q=" + URLEncoder.encode(searchquery, "UTF-8");
            event.respond(url);
        }
        if (message.equalsIgnoreCase("!headon")){
            event.getBot().sendIRC().message(event.getChannel().getName(), "APPLY DIRECTLY TO FOREHEAD");
            event.getBot().sendIRC().message(event.getChannel().getName(), "APPLY DIRECTLY TO FOREHEAD");
            event.getBot().sendIRC().message(event.getChannel().getName(), "APPLY DIRECTLY TO FOREHEAD");
        }
        
        if (message.equalsIgnoreCase("i put on my robe and wizard hat"))
            event.getChannel().send().kick(event.getUser(), "LIGHTNING BOLT");
        
        if (message.equalsIgnoreCase("!suicide"))
            event.getChannel().send().kick(event.getUser(), "SOMETHING WITTY ABOUT DYING");
        
        if (message.equalsIgnoreCase("!burn"))
            event.getBot().sendIRC().message(event.getChannel().getName(), "http://quotes.dtella.org/?quote=1076");
        
        if (message.equalsIgnoreCase("!udon"))
            event.getBot().sendIRC().message(event.getChannel().getName(), "http://quotes.dtella.org/?quote=1019");
        
        if (message.equalsIgnoreCase("!rimshot"))
            event.getBot().sendIRC().message(event.getChannel().getName(), "http://www.instantrimshot.com/");
        
        if (message.equalsIgnoreCase("!kickme"))
            event.getChannel().send().kick(event.getUser(), "you += dead");
        
        if (message.equalsIgnoreCase("!vuvuzela"))
            event.getBot().sendIRC().message(event.getChannel().getName(), "BZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ");
        
        if (Pattern.matches("!hm[m]+", message.toLowerCase()))
            event.getBot().sendIRC().message(event.getChannel().getName(), "indeed good chap the old what for ah yes hm yes good sir old bean");
        
        if (Pattern.matches("!trol[ol]+", message.toLowerCase())||Pattern.matches("trolo[lo]+", message.toLowerCase()))
            event.getBot().sendIRC().message(event.getChannel().getName(), "http://youtu.be/v1PBptSDIh8");
        
        if (Pattern.matches("![0-9]+", message))
            event.getBot().sendIRC().message(event.getChannel().getName(), "http://quotes.dtella.org/?quote="+message.split("!")[1]);
        
        if (message.equalsIgnoreCase("ba dum")||message.equalsIgnoreCase("badum"))
            event.getBot().sendIRC().message(event.getChannel().getName(), "psh");
        
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
        if (message.toLowerCase().startsWith("!troll")){
            if(event.getUser().getNick().equals(Global.BotOwner)||event.getChannel().isOwner(event.getUser())){
                String[] kill = message.split(" ");
                event.getChannel().send().kick(event.getBot().getUserChannelDao().getUser(kill[1]),"YOO GAWT TROLLED " + kill[1]);
            }
            else{
                event.getChannel().send().kick(event.getUser(),"YOO GAWT TROLLED " + event.getUser());
            }
        }
        //(int) (Math.random()*5+1)
        
        if ((message.toLowerCase().startsWith("blarghlebot, ")&&message.endsWith("?"))||(message.toLowerCase().startsWith(Global.MainNick.toLowerCase()+", ")&&message.endsWith("?"))){
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
        if (message.equalsIgnoreCase("!banme")){
            event.getBot().sendRaw().rawLineNow("tban " + event.getChannel().getName() + " 1m " + event.getUser().getNick() + "!*@*");
            event.getChannel().send().kick(event.getUser(), "You're Welcome");
        }
        
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
            //String[] xzibit = message.split(" ");
            event.getBot().sendIRC().message(event.getChannel().getName(),"Yo dawg I heard you like " + messageArray[1] + " so I put an " + messageArray[1] + " in your " + messageArray[2] + " so you can " + messageArray[1] + " while you " + messageArray[2] + ".");
        }
    }
}