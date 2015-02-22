/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects;

import Wheatley.Global;
import java.io.IOException;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

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
public class Runner implements Runnable {
    
    PircBotX bot;
    Thread t;
    
    public Runner(PircBotX bot) {
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

