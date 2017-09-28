/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.listeners;

import rapternet.irc.bots.wheatley.commands.ChannelPartCMD;
import rapternet.irc.bots.wheatley.commands.IgniteCMD;
import rapternet.irc.bots.wheatley.commands.ShortCMD;
import rapternet.irc.bots.wheatley.commands.FactSphereCMD;
import rapternet.irc.bots.wheatley.commands.SpaceCMD;
import rapternet.irc.bots.wheatley.commands.CaveJohnsonCMD;
import rapternet.irc.bots.wheatley.commands.UpdateFile;
import rapternet.irc.bots.wheatley.commands.PingCMD;
import rapternet.irc.bots.wheatley.commands.DynamicKickManager;
import rapternet.irc.bots.wheatley.commands.LaserCMD;
import rapternet.irc.bots.wheatley.commands.Hashtagify;
import rapternet.irc.bots.common.commands.Help;
import rapternet.irc.bots.wheatley.commands.SlanderCMD;
import rapternet.irc.bots.wheatley.commands.ChannelJoinCMD;
import rapternet.irc.bots.common.commands.ManualBotControl;
import rapternet.irc.bots.wheatley.commands.ThrottleCMD;
import rapternet.irc.bots.wheatley.commands.IpInfoCMD;
import rapternet.irc.bots.common.commands.FixCMD;
import rapternet.irc.bots.wheatley.commands.EnglishSayingsCMD;
import rapternet.irc.bots.wheatley.commands.ChangeBaseCMD;
import rapternet.irc.bots.wheatley.commands.ShortenCMD;
import rapternet.irc.bots.common.commands.SysInfoCMD;
import rapternet.irc.bots.common.commands.ListChannels;
import rapternet.irc.bots.wheatley.commands.Why;
import rapternet.irc.bots.common.objects.Command;
import rapternet.irc.bots.wheatley.objects.MapArray;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

/**
 *
 * @author Stephen
 * create commandlist in this class and do a hard coded array for testing
 */
public class CommandListener extends ListenerAdapter{
    private static List<Command> commandList = getCommandList();
    public static MapArray logger = new MapArray(100);
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        String nick = event.getUser().getNick();
        logger.addToLog(event.getChannel().getName(), new ArrayList(Arrays.asList("<" + nick + ">", message)));
        
        if (message.startsWith(Global.commandPrefix)&&!message.matches("([ ]{0,}"+Global.commandPrefix+"{1,}[ ]{0,}){1,}")){
            String command = message.toLowerCase().split(Global.commandPrefix)[1];
            String[] cmdSplit = command.split(" ");
            for (int i=0;i<commandList.size();i++){
                if (commandList.get(i).commandTerms().contains(cmdSplit[0])){
                    commandList.get(i).processCommand(event);
                }
            }
        }
        else if (message.toLowerCase().startsWith(Global.mainNick.toLowerCase()+", ")){
            for (int i=0;i<commandList.size();i++){
                if (commandList.get(i).isCommand(message)){
                    commandList.get(i).processCommand(event);
                }
            }
        }
    }
    
    @Override
    public void onPrivateMessage(PrivateMessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
        if (message.startsWith(Global.commandPrefix)&&!message.matches("([ ]{0,}"+Global.commandPrefix+"{1,}[ ]{0,}){1,}")){
            String command = message.split(Global.commandPrefix)[1];
            String[] cmdSplit = command.split(" ");
            for (int i=0;i<commandList.size();i++){
                if (commandList.get(i).commandTerms().contains(cmdSplit[0])){
                    commandList.get(i).processCommand(event);
                }
            }
        }
        else if (message.toLowerCase().startsWith(Global.mainNick.toLowerCase() + ", ")){
            for (int i=0;i<commandList.size();i++){
                if (commandList.get(i).isCommand(message)){
                    commandList.get(i).processCommand(event);
                }
            }
        }
    }
    
    public static List<Command> getCommandsAvailable() {
        return commandList;
    }
    
    private static List<Command> getCommandList() {
        List<Command> listOfCommands = new ArrayList<>();
//        listOfCommands.add(new PickAPort());
        listOfCommands.add(new Why());
        listOfCommands.add(new ThrottleCMD());
        listOfCommands.add(new ManualBotControl());
        listOfCommands.add(new ListChannels());
        listOfCommands.add(new UpdateFile());
        listOfCommands.add(new IpInfoCMD());
        listOfCommands.add(new FixCMD());
        listOfCommands.add(new IgniteCMD());
        listOfCommands.add(new LaserCMD());
        listOfCommands.add(new SlanderCMD());
        listOfCommands.add(new PingCMD());
        listOfCommands.add(new ShortenCMD());
        listOfCommands.add(new SysInfoCMD());
        listOfCommands.add(new ChangeBaseCMD());
        listOfCommands.add(new Help());
        listOfCommands.add(new CaveJohnsonCMD());
        listOfCommands.add(new FactSphereCMD());
        listOfCommands.add(new EnglishSayingsCMD());
        listOfCommands.add(new ChannelJoinCMD());
        listOfCommands.add(new ChannelPartCMD());
        listOfCommands.add(new DynamicKickManager());
        listOfCommands.add(new ShortCMD());
        listOfCommands.add(new Hashtagify());
        listOfCommands.add(new SpaceCMD());
        return(listOfCommands);
    }
}