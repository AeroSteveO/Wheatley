/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Commands;

import Objects.Command;
import Objects.CommandMetaData;
import Objects.Game;
import Wheatley.Global;
import java.util.ArrayList;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;

/**
 *
 * @author Stephen
 */
public class ChangeBaseCMD implements Command {
    @Override
    public String toString(){
        return("Base: Changes an input decimal value to the input base, EX: \"!base 2 1234\"");
    }
    
    @Override
    public boolean isCommand(String toCheck){
        return false; // Phrase that when spoken will activate the command
    }
    
    @Override
    public ArrayList<String> commandTerms(){
        ArrayList<String> a = new ArrayList<>();
        a.add("base"); 
        return a;
    }
    
    @Override
    public void processCommand(Event event){
        
        CommandMetaData data = new CommandMetaData(event, false);
        
        String caller = data.getCaller(); // Nick of the user who called the command
        String[] cmdSplit = data.getCommandSplit();
        String respondTo = data.respondToCallerOrMessageChan();
        
        if (cmdSplit.length == 3) {
            if (cmdSplit[1].matches("[0-9]+") && cmdSplit[2].matches("[0-9]+")) {
                event.getBot().sendIRC().message(respondTo, Colors.BOLD + "Decimal Value: " + Colors.NORMAL + cmdSplit[2] + Colors.BOLD + " Base " + cmdSplit[1] + ": " + Colors.NORMAL + Game.convertDecimalTo(Integer.valueOf(cmdSplit[1]), Integer.valueOf(cmdSplit[2])));
            }
            else {
                event.getBot().sendIRC().notice(caller, "Base: This command requires 2 integer inputs in the form of \""+Global.commandPrefix+"base [int base] [int value]\"");
            }
        }
    }
}