/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.objects;

import rapternet.irc.bots.wheatley.listeners.Global;
import org.pircbotx.PircBotX;

/**
 *
 * @author carson
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    N/A
 * - Linked Classes
 *    N/A
 *
 */
public class ServerReconnector implements Runnable {
    
    PircBotX bot;
    Thread t;
    
    public ServerReconnector(PircBotX bot) {
        this.bot = bot;
    }
    
    public void giveT(Thread t) {
        this.t = t;
    }
    
    @Override
    public void run() {
        try {
            bot.startBot();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        while (Global.reconnect) {
            try {
                Thread.sleep(1500);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (!bot.isConnected()) {
                try {
                    System.out.println("Trying to restart...");
                    bot.startBot();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                try {
                    Thread.sleep(15000);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}

