/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Commands;

import Objects.Command;
import Objects.CommandMetaData;
import java.util.ArrayList;
import java.util.List;
import org.pircbotx.hooks.Event;

/**
 *
 * @author Stephen
 * original bot = Matrapter
 * matlab based IRC bot written by Steve-O
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    Command
 *    CommandMetaData
 * - Utilities
 *    N/A
 * - Linked Classes
 *    N/A
 * 
 * Activate Commands With
 *      !ignite [it]
 *          lights input object on fire in a randomly generated way, if nothing is input,
 *          the object becomes "it"
 *
 */
public class IgniteCMD implements Command {
    @Override
    public String toString(){
        return("IGNITE");
    }
    
    @Override
    public boolean isCommand(String toCheck){
        return false;
    }
    
    @Override
    public ArrayList<String> commandTerms(){
        ArrayList<String> a = new ArrayList<>();
        a.add("ignite");
        return a;
    }
    
    @Override
    public void processCommand(Event event){
        CommandMetaData commandData = new CommandMetaData(event, false);
        
        
        String respondTo = commandData.respondToIgnoreMessage();
        String it;
        String[] check = commandData.getCommandSplit();
        if (check.length!=2){
            it = "it";
        }
        else {
            it = check[1];
        }
        String chat = simpleFront()+ " " + it + " " + simpleEnd();
        event.getBot().sendIRC().message(respondTo, chat.toUpperCase());
        
    }
    
    public static String simpleFront() {
        List<String> a = new ArrayList<>();
        a.add("burn");
        a.add("bake");
        a.add("broil");
        a.add("roast");
        a.add("vaporize");
        a.add("sublimate");
        a.add("toast");
        a.add("melt");
        a.add("brand");
        a.add("incinerate");
        a.add("carbonize");
        a.add("flay");
        a.add("attack");
        a.add("constipate");
        a.add("gasify");
        a.add("atomize");
        a.add("bombard");
        return (a.get((int) (Math.random()*a.size()-1)));
    }
    public static String simpleEnd() {
        List<String> a = new ArrayList<>();
        a.add("with nukes");
        a.add("with gasoline");
        a.add("with dicks");
        a.add("with diesel fuel");
        a.add("with babies");
        a.add("with the broken dreams of engineers");
        a.add("with plasma");
        a.add("with the Incinerate! plasmid from Ryan Industries");
        a.add("with the Devil's kiss vigor by Fink Co.");
        a.add("with thermite");
        a.add("in a toaster");
        a.add("in an oven like momma's cookies");
        a.add("with fire");
        a.add("in a bonfire");
        a.add("in the nether");
        a.add("in a blaze of failure");
        a.add("with the unused art degrees of baristas");
        a.add("with napalm");
        a.add("with exploding barrels");
        a.add("using a chemical thrower");
        a.add("like you mean it");
        a.add("like a pyromaniac");
        a.add("in the center of a star");
        a.add("in the corona of a star");
        a.add("in the heart of a volcano");
        a.add("in the exhaust of the space shuttle");
        a.add("with lasers");
        a.add("with ejected plasma from the corona of the sun");
        a.add("with a carebearstare");
        return (a.get((int) (Math.random()*a.size()-1)));
    }
    
}
