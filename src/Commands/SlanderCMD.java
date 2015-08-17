/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Commands;

import Objects.Command;
import Objects.CommandMetaData;
import Wheatley.Global;
import java.util.ArrayList;
import org.pircbotx.Colors;
import org.pircbotx.hooks.Event;

/**
 *
 * @author Stephen
 * Original Bot = Matrapter
 * A Matlab based IRC bot written by Steve-O
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
 * Sources: http://www.tastefullyoffensive.com/2011/10/shakespeare-insult-kit.html
 *          http://imgur.com/gallery/q4UXODX
 *          http://www.pangloss.com/seidel/shake_rule.html
 *          http://m.imgur.com/gallery/gUnGqDI
 *          http://i.imgur.com//reyuFY3.jpg
 *
 * Activate Commands With
 *      !Shakespeare [it]
 *          Insults the provided object, if no object is give, it insults 'thou'
 *      !Insult [it]
 *      !Slander [it]
 *          Insults the given object or 'you' if no object is given, using a
 *          random insult generated from one of the built in methods
 */

public class SlanderCMD implements Command {
    @Override
    public String toString(){
        return("SLANDER");
    }
    
    @Override
    public boolean isCommand(String toCheck){
        return false;
    }
    
    @Override
    public ArrayList<String> commandTerms(){
        ArrayList<String> a = new ArrayList<>();
        a.add("insult");
        a.add("slander");
        a.add("shakespeare");
        return a;
    }
    
    @Override
    public ArrayList<String> help(String command) {
        ArrayList<String> a = new ArrayList<>();
        a.add(Colors.BOLD + Global.commandPrefix + "Slander [nick]" + Colors.NORMAL + ": Responds with a randomly generated insult of the input nick, or if no nick is input, it insults \"you\"");
        return a;
    }

    
    @Override
    public void processCommand(Event event){
        
        CommandMetaData commandData = new CommandMetaData(event, false);
        String respondTo = commandData.respondToIgnoreMessage();
        String it;
        String[] check = commandData.getCommandSplit();
        
        if (check.length!=2){
            it = "You ";
        }
        else {
            it = check[1] + ", you ";
        }
        
        if (check[0].equalsIgnoreCase("!shakespeare"))    {
            if (check.length!=2){
                it = "Thou ";
            }
            else {
                it = check[1] + ", thou ";
            }
            event.getBot().sendIRC().message(respondTo, shakespeareInsult(it));
        }
        else{
            switch((int) (Math.random()*5+1)) {
                case 1:
                    event.getBot().sendIRC().message(respondTo, basicInsult(it));
                    break;
                case 2:
                    event.getBot().sendIRC().message(respondTo, gavinInsult(it));
                    break;
                case 3:
                    event.getBot().sendIRC().message(respondTo, basicInsult(it));
                    break;
                case 4:
                    event.getBot().sendIRC().message(respondTo, gavinInsult(it));
                    break;
                case 5:
                    event.getBot().sendIRC().message(respondTo, shakespeareInsult(it));
                    break;
            }
        }
    }
    
    private String shakespeareInsult(String insult){
        ArrayList<String> a = shakespeareFront();  //Begenning part of insult
        ArrayList<String> b = shakespeareMid();    //Middle of insult
        ArrayList<String> c = shakespeareEnd();    //End of insult
        insult = insult +a.get((int) (Math.random()*a.size()-1))+" "+ b.get((int) (Math.random()*b.size()-1)) + " " +c.get((int) (Math.random()*c.size()-1));
        return(insult);
    }
    
    private String basicInsult(String insult){
        ArrayList<String> a = new ArrayList<String>(); //Begenning part of insult
        ArrayList<String> b = new ArrayList<String>(); //Middle of insult
        ArrayList<String> c = new ArrayList<String>(); //End of insult
        a.add("idiotic");
        a.add("insecure");
        a.add("stupid");
        a.add("slimy");
        a.add("slutty");
        a.add("smelly");
        a.add("pompous");
        a.add("lazy");
        a.add("communist");
        a.add("dicknose");
        a.add("pie-eating");
        a.add("racist");
        a.add("elitist");
        a.add("white trash");
        a.add("butterface");
        a.add("drug-loving");
        a.add("tone deaf");
        a.add("ugly");
        a.add("creepy");
        
        b.add("douche");
        b.add("ass");
        b.add("turd");
        b.add("rectum");
        b.add("butt");
        b.add("cock");
        b.add("shit");
        b.add("crotch");
        b.add("bitch");
        b.add("prick");
        b.add("slut");
        b.add("taint");
        b.add("fuck");
        b.add("dick");
        b.add("shart");
        b.add("boner");
        b.add("nut");
        b.add("sphincter");
        
        c.add("pilot");
        c.add("canoe");
        c.add("captain");
        c.add("pirate");
        c.add("hammer");
        c.add("knob");
        c.add("box");
        c.add("jockey");
        c.add("Nazi");
        c.add("waffle");
        c.add("goblin");
        c.add("blossom");
        c.add("biscuit");
        c.add("clown");
        c.add("socket");
        c.add("monster");
        c.add("hound");
        c.add("dragon");
        c.add("balloon");
        
        //String insult = "You ";
        int scale = (int) (Math.random()*3);
        if (scale == 0)
            insult = insult + a.get((int) (Math.random()*a.size()-1))+" ";
        else {
            int index;
            for (int i=0; i<=scale;i++){
                index = (int) (Math.random()*a.size()-1);
                insult = insult + a.get(index)+", ";
                a.remove(index);
            }
        }
        insult = insult + b.get((int) (Math.random()*b.size()-1)) + " " +c.get((int) (Math.random()*c.size()-1));
        return(insult);
    }
    
    private String gavinInsult(String insult){
        ArrayList<String> a = new ArrayList<String>(); //Begenning part of insult
        ArrayList<String> b = new ArrayList<String>(); //Middle of insult
        a.add("gobby");
        a.add("gammy");
        a.add("gumpy");
        a.add("mungy");
        a.add("buggy");
        a.add("pricky");
        a.add("gebby");
        a.add("muggy");
        a.add("pissy");
        a.add("dopy");
        a.add("spinning");
        a.add("spunky");
        a.add("toppy");
        a.add("goffy");
        a.add("drippy");
        a.add("biffy");
        a.add("absolute");
        a.add("douchey");
        a.add("jammy");
        a.add("wallar");
        a.add("chuffy");
        
        b.add("donut");
        b.add("munge");
        b.add("geck");
        b.add("bitch");
        b.add("minge");
        b.add("gob");
        b.add("anus");
        b.add("gub");
        b.add("guff");
        b.add("knob");
        b.add("git");
        b.add("bugger");
        b.add("spap");
        b.add("bip");
        b.add("prick");
        b.add("mug");
        b.add("gump");
        b.add("sausage");
        b.add("mugget");
        b.add("peem");
        b.add("pleb");
        
        insult =insult + a.get((int) (Math.random()*a.size()-1)) +" lit'le "+ b.get((int) (Math.random()*b.size()-1));
        return(insult);
    }
    private ArrayList<String> shakespeareFront(){
        ArrayList<String> first = new ArrayList<String>();
        first.add("artless");
        first.add("bawdy");
        first.add("beslubbering");
        first.add("bootless");
        first.add("churlish");
        first.add("cockered");
        first.add("dankish");
        first.add("dissembling");
        first.add("droning");
        first.add("errant");
        first.add("fawning");
        first.add("fobbing");
        first.add("froward");
        first.add("frothy");
        first.add("gleeking");
        first.add("goatish");
        first.add("gorbellied");
        first.add("impertinent");
        first.add("infectious");
        first.add("jarring");
        first.add("loggerheaded");
        first.add("lumpish");
        first.add("mammering");
        first.add("mangled");
        first.add("mewling");
        first.add("paunchy");
        first.add("pribbling");
        first.add("puking");
        first.add("puny");
        first.add("qualling");
        first.add("rank");
        first.add("reeky");
        first.add("roguish");
        first.add("ruttish");
        first.add("saucy");
        first.add("spleeny");
        first.add("spongy");
        first.add("surly");
        first.add("tottering");
        first.add("unmuzzled");
        first.add("vain");
        first.add("venomed");
        first.add("villainous");
        first.add("warped");
        first.add("wayward");
        first.add("weedy");
        first.add("yeasty");
        first.add("clouted");
        first.add("cockered");
        first.add("currish");
        //Booster kit 1
        first.add("cullionly");
        first.add("fusty");
        first.add("caluminous");
        first.add("wimpled");
        first.add("burly-boned");
        first.add("misbegotten");
        first.add("odiferous");
        first.add("cullionly");
        first.add("poisonous");
        first.add("fishified");
        first.add("Wart-necked");
        
        return first;
    }
    private ArrayList<String> shakespeareEnd(){
        ArrayList<String> end = new ArrayList<String>();
        end.add("apple-john");
        end.add("boar-pig");
        end.add("bugbear");
        end.add("bum-bailey");
        end.add("clack-dish");
        end.add("canker-dish");
        end.add("clotpole");
        end.add("coxcomb");
        end.add("cod-piece");
        end.add("death-token");
        end.add("dewberry");
        end.add("flap-dragon");
        end.add("flax-wench");
        end.add("flirt-gill");
        end.add("foot-licker");
        end.add("fustilarian");
        end.add("giglet");
        end.add("gudgeon");
        end.add("haggard");
        end.add("harpy");
        end.add("hedge-pig");
        end.add("horn-beast");
        end.add("hugger-mugger");
        end.add("joithead");
        end.add("lewdster");
        end.add("lout");
        end.add("maggot-pie");
        end.add("malt-worm");
        end.add("mammet");
        end.add("measle");
        end.add("minnow");
        end.add("miscreant");
        end.add("moldwarp");
        end.add("mumble-news");
        end.add("nut-hook");
        end.add("pigeon-egg");
        end.add("pignut");
        end.add("puttock");
        end.add("pumpion");
        end.add("ratsbane");
        end.add("scut");
        end.add("skainsmate");
        end.add("strumpet");
        end.add("varlot");
        end.add("vassal");
        end.add("whey-face");
        end.add("wagtail");
        end.add("baggage");
        end.add("barnacle");
        end.add("bladdar");
        //Booster kit 1
        end.add("knave");
        end.add("blind-worm");
        end.add("popinjay");
        end.add("scullian");
        end.add("jolt-head");
        end.add("malcontent");
        end.add("devil-monk");
        end.add("toad");
        end.add("rascal");
        end.add("Basket-Cockle");
        return end;
    }
    private ArrayList<String> shakespeareMid(){
        ArrayList<String> mid = new ArrayList<String>();
        mid.add("base-court");
        mid.add("bat-fowling");
        mid.add("beef-witted");
        mid.add("beetle-headed");
        mid.add("boil-brained");
        mid.add("clapper-clawed");
        mid.add("clay-brained");
        mid.add("common-kissing");
        mid.add("crook-pated");
        mid.add("dismal-dreaming");
        mid.add("dizzy-eyed");
        mid.add("doghearted");
        mid.add("dread-bolted");
        mid.add("earth-vexing");
        mid.add("elf-skinned");
        mid.add("fat-kidneyed");
        mid.add("fen-sucked");
        mid.add("flap-mouthed");
        mid.add("fly-bitten");
        mid.add("folly-fallen");
        mid.add("fool-born");
        mid.add("full-gorged");
        mid.add("guts-griping");
        mid.add("half-faced");
        mid.add("hasty-witted");
        mid.add("hedge-born");
        mid.add("hell-hated");
        mid.add("idle-headed");
        mid.add("ill-breeding");
        mid.add("ill-nurtured");
        mid.add("knotty-pated");
        mid.add("milk-livered");
        mid.add("motley-minded");
        mid.add("onion-eyed");
        mid.add("plume-plucked");
        mid.add("pottle-deep");
        mid.add("pox-marked");
        mid.add("reeling-ripe");
        mid.add("rough-hewn");
        mid.add("rude-growing");
        mid.add("rump-fed");
        mid.add("shard-borne");
        mid.add("sheep-biting");
        mid.add("spur-galled");
        mid.add("swag-bellied");
        mid.add("tardy-gaited");
        mid.add("tickle-brained");
        mid.add("toad-spotted");
        mid.add("unchin-snouted");
        mid.add("weather-bitten");
        //Booster kit 1
        mid.add("whoreson");
        mid.add("malmsey-nosed");
        mid.add("rampallian");
        mid.add("lily-livered");
        mid.add("scurvy-valiant");
        mid.add("brazen-faced");
        mid.add("unwash'd");
        mid.add("bunch-back'd");
        mid.add("leaden-footed");
        mid.add("muddy-mettled");
        mid.add("pigeon-liver'd");
        mid.add("scale-sided");
        return mid;
    }
}