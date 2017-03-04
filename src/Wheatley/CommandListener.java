/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Commands.*;
import Commands.Why;
import Objects.Command;
import java.util.ArrayList;
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
    
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
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
//            String command = message.split(Global.commandPrefix)[1];
//            String[] cmdSplit = command.split(" ");
            
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
        return(listOfCommands);
    }
}