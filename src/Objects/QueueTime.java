/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects;

import Wheatley.Global;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 */
public  class QueueTime implements Runnable {
    int time;
    User user;
    Channel chan;
    int key;
    PircBotX bot;
    Thread t;
    QueueTime(PircBotX bot, int time, Channel chan, User user, int key) {
        this.time = time;
        this.chan=chan;
        this.user=user;
        this.key=key;
        this.bot=bot;
    }
    
    public void giveT(Thread t) {
        this.t = t;
    }
//        public void onMessage(final MessageEvent event) throws Exception {
//            // in case something should be done here
//            String message = Colors.removeFormattingAndColors(event.getMessage());
//            if (message.equalsIgnoreCase("!flush")&&(event.getUser().getNick().equalsIgnoreCase(Global.botOwner)))
//                bot.getConfiguration().getListenerManager().dispatchEvent(new MessageEvent(Global.bot,chan,user,Integer.toString(key)));
//        }
    @Override
    public void run() {
        try { // No need to loop for this thread
            Thread.sleep(time*1000);
            bot.getConfiguration().getListenerManager().dispatchEvent(new MessageEvent(Global.bot,chan,user,Integer.toString(key)));
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}