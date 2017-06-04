/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.listeners;

import java.util.Random;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 * Part code from RoyalBot -- http://www.royalcraft.org/royaldev/royalbot
 * Rest of the code is Wheatley Original
 *
 * Requirements:
 * - APIs
 *    JSON-Simple-1.1.1
 * - Custom Objects
 *    ChannelStore
 * - Linked Classes
 *    Global
 *
 * Activate Commands with:
 *      Wheatley, join #[channel]
 *          Makes the bot join the given channel
 *      Wheatley, part #[channel]
 *          Makes the bot part the given channel
 *      Wheatley, leave
 *          Makes the bot part the channel its currently in
 *      Wheatley, fix yourself
 *          Ghosts/Recovers nick, rejoins channels it was disconnected from
 *      Wheatley, whats your IP
 *          Gives the current external IP address of the bot
 *      Wheatley, shutdown
 *          Shuts down the bot
 *      !ram
 *          Responds with Wheatley's current ram usage
 *      !threads
 *          Responds with the number of threads Wheatley is using
 *      !say [sentence]
 *          Forces Wheatley to say the input sentence
 *      !say [channel] [sentence]
 *          Forces Wheatley to say the sentence in the input channel
 *
 */
public class BotControl extends ListenerAdapter{
    
    @Override
    public void onMessage(MessageEvent event) throws InterruptedException, Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String[] msgSplit = message.split(" ");
        if (message.equalsIgnoreCase("!flush")&&(event.getUser().getNick().equals(Global.botOwner)&&event.getUser().isVerified())){
            
            Blarghlebot.poop = "null";
            BadWords.badwords = null;
        }
        
        if (message.equalsIgnoreCase(Global.mainNick+", please shutdown")
                ||message.equalsIgnoreCase("!shutdown")
                ||message.equalsIgnoreCase(Global.mainNick+", shutdown")) {
            
            if (event.getUser().getNick().equals(Global.botOwner)&&event.getUser().isVerified()){
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
                event.getChannel().send().kick(event.getUser(), "PART 5! BOOBY TRAP THE STALEMATE BUTTON!"); // kick people for trying to kill the bot
        }
    }
}