/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.util.Random;
import org.pircbotx.Channel;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 * Part code from RoyalBot -- http://www.royalcraft.org/royaldev/royalbot
 * 
 */
public class BotControl extends ListenerAdapter{
    
    @Override
    public void onMessage(MessageEvent event) throws InterruptedException {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
        if (message.equalsIgnoreCase("!flush")&&event.getUser().getNick().equals(Global.BotOwner)){
            omgword.activechan.clear();
            omgword.wordls.clear();
            Hangman.activechan.clear();
            Blarghlebot.poop = "null";
            FactSphereFacts.quotels = null;
        }
        
        if (message.equalsIgnoreCase(Global.MainNick+", fix yourself")&&event.getUser().getNick().equals(Global.BotOwner)){

            event.getBot().sendIRC().message("NickServ", "ghost " + Global.MainNick + " " + Global.NickPass);
            event.getBot().sendIRC().message("NickServ", "recover " + Global.MainNick + " " + Global.NickPass);
            
            Thread.sleep(5000);
            event.getBot().sendIRC().changeNick(Global.MainNick);
            event.getBot().sendIRC().message("NickServ", "identify " + Global.NickPass);
            for (int i=0;i<Global.Channels.size();i++){
                event.getBot().sendIRC().joinChannel(Global.Channels.get(i));
            }
        }
        
        if (message.equalsIgnoreCase(Global.MainNick+", please power down")||message.equalsIgnoreCase("!powerdown")||message.equalsIgnoreCase(Global.MainNick+", shutdown")) {
            if (event.getUser().getNick().equals("Steve-O")){
                Global.reconnect = false;
                event.getBot().sendIRC().message(event.getChannel().getName(), "Let go! Let go! I'm still connected. I can pull myself in. I can still fix this!");
                Random generator = new Random();
                int i = generator.nextInt(2);
                if (i == 0) {
                    event.getBot().sendIRC().quitServer("Accidentally went to space");
                } else {
                    event.getBot().sendIRC().quitServer("Squished by GLaDOS");
                }
                System.exit(0);
            }
            else
                event.getChannel().send().kick(event.getUser(), "PART 5! BOOBY TRAP THE STALEMATE BUTTON!");
        }
        
        
        if ((message.toLowerCase().startsWith("!join ")||message.toLowerCase().startsWith(Global.MainNick.toLowerCase()+", join ")||message.toLowerCase().startsWith(Global.MainNick.toLowerCase()+", please join "))&&event.getUser().getNick().equals(Global.BotOwner)){
            String[] chan = message.split("#");
            if (message.toLowerCase().contains("#")){
                event.getBot().sendIRC().message(event.getChannel().getName(),"Joining #" + chan[1]);
                event.getBot().sendIRC().joinChannel("#" + chan[1]);
            }
            else
                event.getBot().sendIRC().message(event.getChannel().getName(),chan[chan.length-1] + " is not a channel");
        }
        
        
        if ((message.toLowerCase().startsWith("!part")||message.toLowerCase().startsWith(Global.MainNick.toLowerCase()+", leave")||message.toLowerCase().startsWith(Global.MainNick.toLowerCase()+", please leave"))){
            if (message.toLowerCase().contains("#")&&event.getUser().getNick().equals(Global.BotOwner)) {
                String[] chan = message.split("#");
                Channel c = event.getBot().getUserChannelDao().getChannel("#"+chan[1]);
                if (!event.getBot().getUserBot().getChannels().contains(c)) {
                    event.respond("Not in that channel!");
                }
                else {
                    c.send().part();
                    event.respond("Parted from " + chan[1] + ".");
                }
            }
            else if ((event.getChannel().isOwner(event.getUser())||event.getUser().getNick().equals(Global.BotOwner))&&(message.endsWith("leave")||message.equalsIgnoreCase("!part")))
                event.getChannel().send().part("Goodbye");
        }
    }
}
