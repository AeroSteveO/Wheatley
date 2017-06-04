/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.commands;

import rapternet.irc.bots.wheatley.objects.Command;
import rapternet.irc.bots.wheatley.objects.CommandMetaData;
import rapternet.irc.bots.wheatley.listeners.Global;
import java.util.ArrayList;
import java.util.List;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;

/**
 *
 * @author Stephen
 * original bot = Matrapter
 * MATLAB based IRC bot written by Steve-O
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
 *      !Laser [it]
 *          Puts lasers on the input object in a randomly generated way, if nothing is input,
 *          the object becomes "it"
 *
 */
public class LaserCMD implements Command {
    @Override
    public String toString(){
        return("LASER");
    }
    
    @Override
    public boolean isCommand(String toCheck){
        return false;
    }
    
    @Override
    public ArrayList<String> commandTerms(){
        ArrayList<String> a = new ArrayList<>();
        a.add("laser");
        return a;
    }
    
    @Override
    public ArrayList<String> help(String command) {
        ArrayList<String> a = new ArrayList<>();
        a.add(Colors.BOLD + Global.commandPrefix + "Laser [item]" + Colors.NORMAL + ": Responds with a randomly generated way to add lasers onto the input item, or if no item is input, it adds lasers onto \"it\"");
        return a;
    }

    
    @Override
    public void processCommand(Event event){
        
        
        CommandMetaData commandData = new CommandMetaData(event, false);
        String respondTo = commandData.respondToIgnoreMessage();
        
        
        String it;
        String[] check = commandData.getCommandSplit();
        String a="";
        String[] parts;
        
        if (check.length!=2){
            it = "it";
        }
        else {
            it = check[1];
        }
        switch((int) (Math.random()*4+1)) {
            case 1:
                parts = setup1();
                a = parts[0] + " " + it + " " + parts[1] + " lasers";
                break;
            case 2: case 3:
                parts = setup2();
                a = parts[0] + " lasers " + parts[1] + " " + it;
                break;
            case 4:
                a = "laser cut " + it + " into a " + shape();
                break;
        }
        event.getBot().sendIRC().message(respondTo, a.toUpperCase());
    }
    public static String[] setup1() {
        List<String> a1 = new ArrayList<>();
        List<String> a2 = new ArrayList<>();
        String[] parts = new String[2];
        a1.add("make");
        a1.add("create");
        a1.add("build");
        a1.add("genetically alter");
        a1.add("burn");
        a2.add("with");
        a2.add("with");
        a2.add("out of");
        a2.add("using");
        a2.add("through the use of");
        parts[0]= a1.get((int) (Math.random()*a1.size()-1));
        parts[1]= a2.get((int) (Math.random()*a2.size()-1));
        return (parts);
    }
    public static String[] setup2() {
        List<String> a1 = new ArrayList<>();
        List<String> a2 = new ArrayList<>();
        String[] parts = new String[2];
        a1.add("add");
        a1.add("strap");
        a1.add("put");
        a1.add("stuff");
        a1.add("engineer");
        a2.add("to");
        a2.add("on");
        a2.add("in to");
        int n = (int) (Math.random()*a1.size()+3);
        if (n<=a1.size())
            parts[0] = a1.get((int) (Math.random()*a1.size()-1));
        else if (n == a1.size()+1)
            parts[0]=tape();
        else
            parts[0]=glue();
        parts[1]= a2.get((int) (Math.random()*a2.size()-1));
        return (parts);
    }
    public static String tape() {
        List<String> tape = new ArrayList<>();
        tape.add("duct tape");
        tape.add("masking tape");
        tape.add("blue tape");
        tape.add("gorilla tape");
        tape.add("scotch tape");
        tape.add("double sided tape");
        return (tape.get((int) (Math.random()*tape.size()-1)));
    }
    public static String glue() {
        List<String> glue = new ArrayList<>();
        glue.add("epoxy");
        glue.add("wood glue");
        glue.add("gorilla glue");
        glue.add("cyanoacrelate");
        glue.add("super glue");
        glue.add("JB Weld");
        glue.add("elmers glue");
        return (glue.get((int) (Math.random()*glue.size()-1)));
    }
    public static String shape() {
        List<String> shapes = new ArrayList<>();
        shapes.add("square");
        shapes.add("circle");
        shapes.add("triangle");
        shapes.add("hexagon");
        shapes.add("heptagon");
        shapes.add("octagon");
        shapes.add("pyramid");
        shapes.add("dodecahedron");
        shapes.add("booby");
        shapes.add("penis");
        shapes.add("pentagon");
        return (shapes.get((int) (Math.random()*shapes.size()-1)));
    }
}
