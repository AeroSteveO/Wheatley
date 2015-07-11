/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import GameCommands.GameBombCMD;
import GameCommands.MoneyCMD;
import GameCommands.PurchaseTimeBomb;
import Objects.Command;
import Objects.CommandGame;
import Utils.GameUtils;
import java.util.ArrayList;
import java.util.List;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

/**
 *
 * @author Stephen
 */
public class GameListener extends ListenerAdapter{
    List<CommandGame> commandList = getCommandList();
    
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
        if (!GameUtils.areGamesBlocked(event.getChannel().getName())) {
            if (message.startsWith(Global.commandPrefix)&&!message.matches("([ ]{0,}"+Global.commandPrefix+"{1,}[ ]{0,}){1,}")) {
                String command = message.toLowerCase().split(Global.commandPrefix)[1];
                String[] cmdSplit = command.split(" ");
                for (int i=0;i<commandList.size();i++){
                    if (commandList.get(i).commandTerms().contains(cmdSplit[0])) {
                        commandList.get(i).processCommand(event);
                    }
                }
            }
//            else if (message.toLowerCase().startsWith(Global.mainNick.toLowerCase()+", ")) {
//                for (int i=0;i<commandList.size();i++){
//                    if (commandList.get(i).isCommand(message)){
//                        commandList.get(i).processCommand(event);
//                    }
//                }
//            }
        }
    }
    
//    @Override
//    public void onPrivateMessage(PrivateMessageEvent event) throws Exception {
//        String message = Colors.removeFormattingAndColors(event.getMessage());
//        
//        if (message.startsWith(Global.commandPrefix)&&!message.matches("([ ]{0,}"+Global.commandPrefix+"{1,}[ ]{0,}){1,}")) {
//            String command = message.split(Global.commandPrefix)[1];
//            String[] cmdSplit = command.split(" ");
//            for (int i=0;i<commandList.size();i++){
//                if (commandList.get(i).commandTerms().contains(cmdSplit[0])) {
//                    commandList.get(i).processCommand(event);
//                }
//            }
//        }
//        else if (message.toLowerCase().startsWith(Global.mainNick.toLowerCase())) {
//            for (int i=0;i<commandList.size();i++){
//                if (commandList.get(i).isCommand(message)){
//                    commandList.get(i).processCommand(event);
//                }
//            }
//        }
//    }
    
    private List<CommandGame> getCommandList() {
        List<CommandGame> listOfCommands = new ArrayList<>();
//        listOfCommands.add(new MoneyCMD());
        listOfCommands.add(new PurchaseTimeBomb());
        listOfCommands.add(new GameBombCMD());
        return(listOfCommands);
    }
    
}
