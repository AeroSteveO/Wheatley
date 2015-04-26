/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package GameCommands;

import Objects.Command;
import Wheatley.Global;
import java.util.ArrayList;
import org.pircbotx.hooks.Event;

/**
 *
 * @author Stephen
 */
public class Cheat implements Command{
    @Override
    public void processCommand(Event event){
        
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
