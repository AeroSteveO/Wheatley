/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.listeners;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.NoticeEvent;
import rapternet.irc.bots.wheatley.objects.Env;

/**
 *
 * @author Stephen
 */
public class IdleRPG extends ListenerAdapter {
    
    private String idleRPGChan = Env.IRPG_CHANNEL; // TODO ADD TO SETTINGS
    private String idleBot = Env.IRPG_BOT_NAME; // TODO ADD TO SETTINGS
    private String username = Env.IRPG_USERNAME;
    private String password = ""; // TODO ADD TO SETTINGS
    private String userClass = Env.IRPG_USER_CLASS; // TODO ADD TO SETTINGS
    private int time = 120; // seconds
    
    public IdleRPG() {
        if (!Global.settings.contains("idlerpg-pass")) {
            Global.settings.create("idlerpg-pass",Env.IRPG_PASSWORD);
            System.out.println("ERROR: NO IDLE RPG PASSWORD");
        }
        password = Global.settings.get("idlerpg-pass");
    }
    
    @Override
    public void onConnect(ConnectEvent event) {
        event.getBot().sendIRC().joinChannel(idleRPGChan);
        
        try {
            Thread.sleep(6000);
        }
        catch (InterruptedException e) {
        }
        
        event.getBot().sendIRC().message(idleBot, "login " + username + " " + password);
    }
    
    @Override
    public void onNotice(NoticeEvent event) {
        if (event.getUser() == null || event.getUser().getNick() == null) {
            return;
        }
        if (!event.getUser().getNick().isEmpty()) {
            if (event.getUser().getNick().equalsIgnoreCase(idleBot)) {
                if (event.getMessage().equalsIgnoreCase("Sorry, no such account name. Note that account names are case sensitive.")) {
                    event.getBot().sendIRC().message(idleBot, "register " + username + " " + password + " " + userClass);
                }
            }
        }
    }
    
    @Override
    public void onMessage(MessageEvent event) {
        if(event.getMessage().equalsIgnoreCase(Global.commandPrefix + "irpg")) {
            if (event.getUser().getNick().equalsIgnoreCase(Global.botOwner) && event.getUser().isVerified()) {
                event.getBot().sendIRC().message(idleBot, "login " + username + " " + password);
            }
        }
        else if (event.getMessage().equalsIgnoreCase(Global.commandPrefix + "regirpg")) {
            if (event.getUser().getNick().equalsIgnoreCase(Global.botOwner) && event.getUser().isVerified()) {
                event.getBot().sendIRC().message(idleBot, "register " + username + " " + password + " " + userClass);
            }
        }
    }
}
