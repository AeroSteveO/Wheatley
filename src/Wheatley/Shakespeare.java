/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.util.ArrayList;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 * Shakespeare    Provides shakespearean era insults.
 * @author Steve-O
 * original bot = Matrapter
 * matlab based IRC bot written by Steve-O
 * Source:  http://www.tastefullyoffensive.com/2011/10/shakespeare-insult-kit.html
 * shakespeare(it) insults the provided object.
 * shakespeare, by itself, insults 'thou'.
 */
public class Shakespeare extends ListenerAdapter {
    ArrayList<String> first = null;
    ArrayList<String> mid = null;
    ArrayList<String> ending = null;
    @Override
    public void onMessage(final MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        if (message.startsWith("!shakespeare"))    {
            String it;
            String[] check = message.split(" ");
            if (check.length!=2){
                it = "Thou";
            }
            else {
                it = check[1];
            }
            if (first == null)
                first = Front();
            
            if (mid == null)
                mid = Middle();
            
            if (ending == null)
                ending = Ending();
            if (it.equals("Thou"))
                event.getBot().sendIRC().message(event.getChannel().getName(), event.getBot().getUserChannelDao().getUser(it).getNick() + " " + first.get((int) (Math.random()*first.size()-1)) + " " + mid.get((int) (Math.random()*mid.size()-1)) + " " + ending.get((int) (Math.random()*ending.size()-1)));
            else
                event.getBot().sendIRC().message(event.getChannel().getName(), event.getBot().getUserChannelDao().getUser(it).getNick() + ", thou " + first.get((int) (Math.random()*first.size()-1)) + " " + mid.get((int) (Math.random()*mid.size()-1)) + " " + ending.get((int) (Math.random()*ending.size()-1)));
        }
    }
    public ArrayList<String> Front(){
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
        //    first.add("");
        return first;
    }
    public ArrayList<String> Ending(){
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
        return end;
    }
    public ArrayList<String> Middle(){
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
        return mid;
    }
}

