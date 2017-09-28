/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.commands;

import rapternet.irc.bots.common.objects.Command;
import rapternet.irc.bots.wheatley.listeners.Global;
import java.util.ArrayList;
import java.util.List;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

/**
 *
 * @author Stephen
 * Ported from an easter egg in MATLAB
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    Command
 * - Utilities
 *    N/A
 * - Linked Classes
 *    Global
 *
 * Activate Command with:
 *      !Why
 *      [BotName], Why [any question]
 *          Responds with a randomly generated reason as to why
 *
 */

public class Why implements Command{
    
    @Override
    public String toString(){
        return("Why implements the easter egg from MATLAB for fun in IRC");
    }
    
    @Override
    public boolean isCommand(String toCheck){
        
        return(toCheck.toLowerCase().startsWith(Global.mainNick.toLowerCase()+", why"));
    }
    
    @Override
    public ArrayList<String> help(String command) {
        ArrayList<String> a = new ArrayList<>();
        a.add(Colors.BOLD + Global.mainNick + ", why [any sort of question]?" + Colors.NORMAL + ": Responds with a randomly generated reason as to why" );
        a.add(Colors.BOLD + Global.commandPrefix + "Why" + Colors.NORMAL + ": Responds with a randomly generated reason as to why");
        
        return a;
    }
    
    
    @Override
    public ArrayList<String> commandTerms(){
        ArrayList<String> a = new ArrayList<>();
        a.add("why");
        return a;
    }
    
    @Override
    public void processCommand(Event event){
        String a;
        
        switch((int) (Math.random()*10+1)) {
            case 1:
                a=special_case();
                break;
            case 2: case 3: case 4:
                a=phrase();
                break;
            default:
                a=sentence();
        }
        
        if (event instanceof MessageEvent){
            MessageEvent mEvent = (MessageEvent) event;
            mEvent.getBot().sendIRC().message(mEvent.getChannel().getName(), a);
        }
        else if(event instanceof PrivateMessageEvent){
            PrivateMessageEvent mEvent = (PrivateMessageEvent) event;
            mEvent.getBot().sendIRC().message(mEvent.getUser().getNick(), a);
        }
    }
    
    private static String special_case() {
        List<String> a = new ArrayList<>();
        a.add("why not?");
        a.add("don't ask!");
        a.add("it's your karma.");
        a.add("stupid question!");
        a.add("how should I know?");
        a.add("can you rephrase that?");
        a.add("it should be obvious.");
        a.add("the devil made me do it.");
        a.add("the computer did it.");
        a.add("the customer is always right.");
        a.add("in the beginning, God created the heavens and the earth...");
        a.add("don't you have something better to do?");
        return (a.get((int) (Math.random()*a.size()-1)));
    }
    
    private static String phrase() {
        String a = new String();
        switch((int) (Math.random()*3+1)) {
            case 1:
                a = "for the " + nouned_verb() + " " + prepositional_phrase() + ".";
                break;
            case 2:
                a = "to " + present_verb() + " " + oobject() + ".";
                break;
            case 3:
                a = "because " + sentence();
                break;
        }
        return (a);
    }
    
    private static String prepositional_phrase() {
        String a = new String();
        switch((int) (Math.random()*3+1)) {
            case 1:
                a = preposition() + " " + article() + " " + noun_phrase();
                break;
            case 2:
                a = preposition() + " " + proper_noun();
                break;
            case 3:
                a = preposition() + " " + accusative_pronoun();
                break;
        }
        return (a);
    }
    
    private static String sentence() {
        String a;
        a = subject() + " " + predicate() + ".";
        return (a);
    }
    
    private static String noun_phrase() {
        String a = new String();
        switch((int) (Math.random()*3+1)) {
            case 1:
                a = noun();
                break;
            case 2:
                a = adjective_phrase() + " " + noun_phrase();
                break;
            case 3:
                a = adjective_phrase() + " " + noun();
                break;
        }
        return (a);
    }
    private static String subject() {
        String a = new String();
        switch((int) (Math.random()*4+1)) {
            case 1:
                a = proper_noun();
                break;
            case 2:
                a = nominative_pronoun();
                break;
            case 3:
                a = article() + " " + noun_phrase();
                break;
            case 4:
                a = article() + " " + noun_phrase();
                break;
        }
        return (a);
    }
    private static String adjective_phrase() {
        String a = new String();
        switch((int) (Math.random()*6+1)) {
            case 1: case 2: case 3:
                a = adjective();
                break;
            case 4: case 5:
                a = adjective_phrase() + " and " + adjective_phrase();
                break;
            case 6:
                a = adverb() + " " + adjective();
                break;
        }
        return (a);
    }
    private static String predicate() {
        String a = new String();
        switch((int) (Math.random()*3+1)) {
            case 1:
                a = transitive_verb() + " " + oobject();
                break;
            case 2: case 3:
                a = intrasitive_verb();
                break;
        }
        return (a);
    }
    private static String oobject() {
        String a;
        switch((int) (Math.random()*3+1)) {
            case 1:
                a = accusative_pronoun();
                break;
            default:
                a = article() + " " + noun_phrase();
                break;
        }
        return (a);
    }
    
    
    private static String preposition() {
        List<String> a = new ArrayList<>();
        a.add("of");
        a.add("from");
        return (a.get((int) (Math.random()*a.size()-1)));
    }
    private static String noun() {
        List<String> a = new ArrayList<>();
        a.add("mathematician");
        a.add("programmer");
        a.add("system manager");
        a.add("engineer");
        a.add("hamster");
        a.add("kid");
        return (a.get((int) (Math.random()*a.size()-1)));
    }
    private static String nouned_verb() {
        List<String> a = new ArrayList<>();
        a.add("love");
        a.add("approval");
        return (a.get((int) (Math.random()*a.size()-1)));
    }
    private static String proper_noun() {
        List<String> a = new ArrayList<>();
        a.add("Cleve");
        a.add("Jack");
        a.add("Bill");
        a.add("Joe");
        a.add("Pete");
        a.add("Loren");
        a.add("Damian");
        a.add("Barney");
        a.add("Nausheen");
        a.add("Mary Ann");
        a.add("Penny");
        a.add("Mara");
        return (a.get((int) (Math.random()*a.size()-1)));
    }
    private static String nominative_pronoun() {
        List<String> a = new ArrayList<>();
        a.add("I");
        a.add("you");
        a.add("he");
        a.add("she");
        a.add("they");
        return (a.get((int) (Math.random()*a.size()-1)));
    }
    private static String accusative_pronoun() {
        List<String> a = new ArrayList<>();
        a.add("me");
        a.add("all");
        a.add("her");
        a.add("him");
        return (a.get((int) (Math.random()*a.size()-1)));
    }
    private static String adverb() {
        List<String> a = new ArrayList<>();
        a.add("very");
        a.add("not very");
        a.add("excessively");
        a.add("not excessively");
        return (a.get((int) (Math.random()*a.size()-1)));
    }
    private static String adjective() {
        List<String> a = new ArrayList<>();
        a.add("tall");
        a.add("bald");
        a.add("young");
        a.add("smart");
        a.add("terrified");
        a.add("good");
        return (a.get((int) (Math.random()*a.size()-1)));
    }
    private static String article() {
        List<String> a = new ArrayList<>();
        a.add("the");
        a.add("some");
        a.add("a");
        return (a.get((int) (Math.random()*a.size()-1)));
    }
    private static String present_verb() {
        List<String> a = new ArrayList<>();
        a.add("fool");
        a.add("please");
        a.add("satisfy");
        a.add("honor");
        return (a.get((int) (Math.random()*a.size()-1)));
    }
    private static String intrasitive_verb() {
        List<String> a = new ArrayList<>();
        a.add("insisted on it");
        a.add("suggested it");
        a.add("told me to");
        a.add("wanted it");
        a.add("knew it was a good idea");
        a.add("wanted it that way");
        return (a.get((int) (Math.random()*a.size()-1)));
    }
    private static String transitive_verb() {
        List<String> a = new ArrayList<>();
        a.add("threatened");
        a.add("told");
        a.add("asked");
        a.add("helped");
        a.add("obeyed");
        a.add("obeyed");
        a.add("obeyed");
        a.add("obeyed");
        a.add("obeyed");
        a.add("obeyed");
        return (a.get((int) (Math.random()*a.size()-1)));
    }
}
