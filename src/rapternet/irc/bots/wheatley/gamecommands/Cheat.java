/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.gamecommands;

import rapternet.irc.bots.wheatley.objects.CommandGame;
import rapternet.irc.bots.common.objects.CommandMetaData;
import static rapternet.irc.bots.wheatley.listeners.GameListener.scores;
import rapternet.irc.bots.wheatley.listeners.Global;
import java.util.ArrayList;
import org.pircbotx.Colors;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 */
public class Cheat implements CommandGame {
    
    @Override
    public boolean isGame() {
        return false;
    }
    @Override
    public boolean isShortGame() {
        return false;
    }
    @Override
    public void processCommand(MessageEvent event){
        
        CommandMetaData data = new CommandMetaData(event, true);
        
        String sender = data.getCaller();
        String[] cmdSplit = data.getCommandSplit();
        String channel = data.getCommandChannel();
        boolean isVerified = data.isVerified();
        
        if ((event.getUser().getNick().equalsIgnoreCase(Global.botOwner)||event.getUser().getNick().equalsIgnoreCase("burg")) && isVerified){
            if (event.getBot().getUserChannelDao().containsUser(cmdSplit[1])){
                if (cmdSplit.length < 3) {
                    
                    event.getBot().sendRaw().rawLineNow("tban " + channel + " 10m " + cmdSplit[1] + "!*@*");
                    event.getChannel().send().kick(event.getBot().getUserChannelDao().getUser(cmdSplit[1]), "You have been temporarily banned from the #Casino for cheating");
                }
                else if (cmdSplit.length>3){
                    String time;
                    String[] kill;
                    String reason;
                    if (cmdSplit[2].matches("[0-9]+[s|d|m|h|y]")){
                        kill = Colors.removeFormattingAndColors(event.getMessage()).split(" ",4);
                        time = cmdSplit[2];
                        reason = kill[3];
                    }
                    else {
                        kill = Colors.removeFormattingAndColors(event.getMessage()).split(" ",3);
                        time = "10m";
                        reason = kill[2];
                    }
                    
                    String user = event.getBot().getUserChannelDao().getUser(kill[1]).getNick();
                    event.getBot().sendRaw().rawLineNow("tban " + channel + " " + time + " " + user + "!*@*");
                    event.getChannel().send().kick(event.getBot().getUserChannelDao().getUser(user), "You have been temporarily banned from the #Casino for cheating | <"+sender+"> "+reason);
                }
                else {
                    event.getBot().sendRaw().rawLineNow("tban " + channel + " " + cmdSplit[2] + " " + cmdSplit[1] + "!*@*");
                    event.getChannel().send().kick(event.getBot().getUserChannelDao().getUser(cmdSplit[1]), "You have been temporarily banned from the #Casino for cheating");
                }
                scores.subtractScore(cmdSplit[1], 500);
            }
            else{
                event.getBot().sendIRC().notice(event.getUser().getNick(),"User not found");
            }
        }
        else{
            event.getBot().sendIRC().notice(event.getUser().getNick(),"You do not have access to this function");
        }
    }
    @Override
    public ArrayList<String> commandTerms(){
        ArrayList<String> a = new ArrayList<>();
        a.add("cheat");
        return a;
        
    }
    @Override
    public boolean isCommand(String toCheck){
        return false;
    }
}
