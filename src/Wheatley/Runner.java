/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Wheatley;

import org.pircbotx.PircBotX;

/**
 *
 * @author carson
 */
public class Runner implements Runnable {

    PircBotX bot;
    Thread t;

    Runner(PircBotX bot) {
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

