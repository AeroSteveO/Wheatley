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
import java.util.Stack;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;

/**
 *
 * @author Stephen
 */
public class ChangeBaseCMD implements Command {
    public ChangeBaseCMD() {
        
    }
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
        a.add("rpn");
        return a;
    }
    
    @Override
    public ArrayList<String> help(String command) {
        ArrayList<String> a = new ArrayList<>();
        if (command.equalsIgnoreCase("base") || command.equalsIgnoreCase(this.getClass().getName()))
            a.add(Colors.BOLD + Global.commandPrefix + "Base [int base] [int value]" + Colors.NORMAL + ": Changes the decimal input value to the input base");
        if (command.equalsIgnoreCase("rpn") || command.equalsIgnoreCase(this.getClass().getName()))
            a.add(Colors.BOLD + Global.commandPrefix + "RPN [Postfix Notation Equation]" + Colors.NORMAL + ": Evaluates the input Postfix Notation equation");
        return a;
    }

    
    @Override
    public void processCommand(Event event){
        
        CommandMetaData data = new CommandMetaData(event, false);
        
        String caller = data.getCaller(); // Nick of the user who called the command
        String[] cmdSplit = data.getCommandSplit();
        String respondTo = data.respondToCallerOrMessageChan();
        String command = data.getCommand();
        //System.out.println(command);
        
        if (command.equalsIgnoreCase("rpn")) {
            String message = data.getMessage().split(" ", 2)[1];
            //System.out.println(message);
         event.getBot().sendIRC().message(respondTo, "RPN Solution: " + evaluateRPN(message));   
        }
        else {
        if (cmdSplit.length == 3) {
            if (cmdSplit[1].matches("[0-9]+") && cmdSplit[2].matches("[0-9]+")) {
                int base;
                long value;
                try {
                    base = Integer.valueOf(cmdSplit[1]);
                    value = Long.valueOf(cmdSplit[2]);
                }
                catch (NumberFormatException e) {
                    event.getBot().sendIRC().notice(caller, "Base: Invalid Input");
                    return;
                }
                
                if (base > 36 || base < 2) {
                    event.getBot().sendIRC().notice(caller, "Base: Invalid Input, base must be an integer between 2 & 36");
                }
                
                event.getBot().sendIRC().message(respondTo, Colors.BOLD + "Decimal Value: " + Colors.NORMAL + cmdSplit[2] + Colors.BOLD + " Base " + cmdSplit[1] + ": " + Colors.NORMAL + Game.convertDecimalTo(base, value));
            }
            else {
                event.getBot().sendIRC().notice(caller, "Base: This command requires 2 integer inputs in the form of \""+Global.commandPrefix+"base [int base] [int value]\"");
            }
        }
        }
    }
    
    public static int evaluateRPN(String s) {
        Stack<Integer> stack = new Stack<>();
        String[] tokens = s.split(" ");
        for (String token : tokens) {
            if (token.matches("[0-9]"))
                stack.push(Integer.parseInt(token));
            else { // note non-standard compact formatting to fit slide...
                int op2 = stack.pop(); int op1 = stack.pop();
                switch (token) {
                    case "+":
                        stack.push(op1 + op2);
                        break;
                    case "-":
                        stack.push(op1 - op2);
                        break;
                    case "*":
                        stack.push(op1 * op2);
                        break;
                    case "/":
                        stack.push(op1 / op2);
                        break;
                    default:
                        throw new RuntimeException("unknown operator");
                }
            }
        }
        return stack.pop();
    }
}