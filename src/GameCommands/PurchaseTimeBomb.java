/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package GameCommands;

import Objects.CommandGame;
import Objects.CommandMetaData;
import Utils.ColorUtils;
import com.google.common.collect.ImmutableSortedSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 */
public class PurchaseTimeBomb implements CommandGame {
    private static final int minWires = 2;
    private static final int maxWires = 4;
    private static final int minTime = 45; // seconds
    private static final int maxTime = 70; // seconds
    
    private static final int minRandombombTime = 60; // seconds
    private static final int maxRandombombTime = 120; // seconds
    
    private static boolean showArt = true;
    
    private static ArrayList<String> exclusions; // excluded nicks
    
    private static boolean bombActiveUsers = false; // determine whether only active users should be random bombed
    private static boolean joinIsActivity = false; // determine whether joining the channel is counted as activity
    private static boolean allowSelfBombs = true; // allow the bot to bomb itself?
    private static int idleTime = 60; // minutes before someone is counted as idle for random bombs if idle checking is enabled
    private static boolean showCorrectWire = true; // show the correct wire after detonation?
    
    @Override
    public void processCommand(MessageEvent event){
        boolean isRandom = false;
        String victim = new String();
        
        ArrayList<String> bomb = getTimeBomb();
        
        CommandMetaData data = new CommandMetaData(event,false);
        
        String channel = data.getCommandChannel();
        String[] cmdSplit = data.getCommandSplit();
        String caller = data.getCaller();
        String respondTo = data.respondToCallerOrMessageChan();
        
        if (cmdSplit[0].equalsIgnoreCase("randombomb")) {
            isRandom = true;
        }
        
        if (cmdSplit.length == 2 || isRandom) {
            if (!isRandom && event.getBot().getUserChannelDao().containsUser(cmdSplit[1]) && event.getBot().getUserChannelDao().getChannels(event.getBot().getUserChannelDao().getUser(cmdSplit[1])).contains(event.getChannel())) {
                victim = cmdSplit[1];
            }
            else if (!isRandom) {
                event.getBot().sendIRC().notice(caller, "Timebomb: User does not exist in command channel");
                return;
            }
            else if (isRandom) {
                ImmutableSortedSet users = event.getBot().getUserChannelDao().getChannel(channel).getUsersNicks();
                
                Random random = new Random();
                int chosen = random.nextInt(users.size());
                int c = 0;
                System.out.println(chosen);
                Iterator<String> userIterator = users.iterator();
                
                while (userIterator.hasNext()) {
                    String user = userIterator.next();
                    if (c == chosen) {
                        victim = user;
                        System.out.println("VICTIM CHOSEN");
                    }
                        c++;
                    System.out.println(user);
                }
            }
            event.getBot().sendIRC().action(respondTo, "stuffs a bomb down " + victim + "'s pants.  The timer is set for %s seconds!  "
                    + "There are %s wires.  They are:");
            //event.getBot().getUserChannelDao().getUser(kill[1]).getNick()
        }
        else {
            event.getBot().sendIRC().notice(caller, "Timebomb: This command exactly one input of the form of a user nick, \"!timebomb [user]\" \"!randombomb [user]\"");
        }
        
        for (int i=0; i<bomb.size(); i++){
            event.getBot().sendIRC().message(channel, bomb.get(i));
        }
    }
    
    @Override
    public boolean isShortGame() {
        return true;
    }
    
    @Override
    public boolean isGame() {
        return false;
    }
    
    @Override
    public ArrayList<String> commandTerms(){
        ArrayList<String> a = new ArrayList<>();
        a.add("timebomb");
        a.add("randombomb");
        return a;
        
    }
    @Override
    public boolean isCommand(String toCheck){
        return false;
    }
    
    
    
    
    
    private ArrayList<String> getTimeBomb(){
        ArrayList<String> bomb = new ArrayList<>();
        bomb.add(ColorUtils.getColors(ColorUtils.BLACK,ColorUtils.BLACK) + "....." + ColorUtils.getColors(ColorUtils.LIGHT_GRAY,ColorUtils.BLACK) + "_." + ColorUtils.getColors(ColorUtils.DARK_GRAY,ColorUtils.BLACK) + "-^^---....," + ColorUtils.getColors(ColorUtils.LIGHT_GRAY,ColorUtils.BLACK) + ",-_" + ColorUtils.getColors(ColorUtils.BLACK,ColorUtils.BLACK) + ".......");
        bomb.add(ColorUtils.getColors(ColorUtils.BLACK,ColorUtils.BLACK) + "." + ColorUtils.getColors(ColorUtils.LIGHT_GRAY,ColorUtils.BLACK) + "_--" + ColorUtils.getColors(ColorUtils.DARK_GRAY,ColorUtils.BLACK) + ",.';,`.,';,.;;`;,." + ColorUtils.getColors(ColorUtils.LIGHT_GRAY,ColorUtils.BLACK) + "--_" + ColorUtils.getColors(ColorUtils.BLACK,ColorUtils.BLACK) + "...");
        bomb.add(ColorUtils.getColors(ColorUtils.LIGHT_GRAY,ColorUtils.BLACK) + "<,."+ColorUtils.getColors(ColorUtils.DARK_GRAY,ColorUtils.BLACK)+";'`\".,;`..,;`*.,';`."+ColorUtils.getColors(ColorUtils.LIGHT_GRAY,ColorUtils.BLACK)+";'>).");
        bomb.add(ColorUtils.getColors(ColorUtils.LIGHT_GRAY,ColorUtils.BLACK)+"I.:;"+ColorUtils.getColors(ColorUtils.DARK_GRAY,ColorUtils.BLACK)+".,`;~,`.;'`,.;'`,..';"+ColorUtils.getColors(ColorUtils.LIGHT_GRAY,ColorUtils.BLACK)+"`I.");
        bomb.add(ColorUtils.getColors(ColorUtils.BLACK,ColorUtils.BLACK)+"."+ColorUtils.getColors(ColorUtils.LIGHT_GRAY,ColorUtils.BLACK)+"\\_."+ColorUtils.getColors(ColorUtils.DARK_GRAY,ColorUtils.BLACK)+"`'`..`';.,`';,`';,"+ColorUtils.getColors(ColorUtils.LIGHT_GRAY,ColorUtils.BLACK)+"_../"+ColorUtils.getColors(ColorUtils.BLACK,ColorUtils.BLACK)+"..");
        bomb.add(ColorUtils.getColors(ColorUtils.BLACK,ColorUtils.BLACK)+"...."+ColorUtils.getColors(ColorUtils.LIGHT_GRAY,ColorUtils.BLACK)+"```"+ColorUtils.getColors(ColorUtils.DARK_GRAY,ColorUtils.BLACK)+"--. . , ; .--"+ColorUtils.getColors(ColorUtils.DARK_GRAY,ColorUtils.BLACK)+"'''"+ColorUtils.getColors(ColorUtils.BLACK,ColorUtils.BLACK)+".....");
        bomb.add(ColorUtils.getColors(ColorUtils.BLACK,ColorUtils.BLACK)+".........."+ColorUtils.getColors(ColorUtils.RED,ColorUtils.BLACK)+"I"+ColorUtils.getColors(ColorUtils.BLACK,ColorUtils.BLACK)+"."+ColorUtils.getColors(ColorUtils.YELLOW,ColorUtils.BLACK)+"I"+ColorUtils.getColors(ColorUtils.BROWN,ColorUtils.BLACK)+"I"+ColorUtils.getColors(ColorUtils.BLACK,ColorUtils.BLACK)+"."+ColorUtils.getColors(ColorUtils.YELLOW,ColorUtils.BLACK)+"I"+ColorUtils.getColors(ColorUtils.RED,ColorUtils.BLACK)+"I"+ColorUtils.getColors(ColorUtils.BLACK,ColorUtils.BLACK)+"...........");
        bomb.add(ColorUtils.getColors(ColorUtils.BLACK,ColorUtils.BLACK)+".........."+ColorUtils.getColors(ColorUtils.RED,ColorUtils.BLACK)+"I"+ColorUtils.getColors(ColorUtils.BLACK,ColorUtils.BLACK)+"."+ColorUtils.getColors(ColorUtils.BROWN,ColorUtils.BLACK)+"I"+ColorUtils.getColors(ColorUtils.RED,ColorUtils.BLACK)+"I"+ColorUtils.getColors(ColorUtils.BLACK,ColorUtils.BLACK)+"."+ColorUtils.getColors(ColorUtils.BROWN,ColorUtils.BLACK)+"I"+ColorUtils.getColors(ColorUtils.RED,ColorUtils.BLACK)+"I"+ColorUtils.getColors(ColorUtils.BLACK,ColorUtils.BLACK)+"...........");
        bomb.add(ColorUtils.getColors(ColorUtils.BLACK,ColorUtils.BLACK)+"......."+ColorUtils.getColors(ColorUtils.RED,ColorUtils.BLACK)+",-=II"+ColorUtils.getColors(ColorUtils.BROWN,ColorUtils.BLACK)+"..I"+ColorUtils.getColors(ColorUtils.RED,ColorUtils.BLACK)+".I=-,"+ColorUtils.getColors(ColorUtils.BLACK,ColorUtils.BLACK)+"........");
        bomb.add(ColorUtils.getColors(ColorUtils.BLACK,ColorUtils.BLACK)+"......."+ColorUtils.getColors(ColorUtils.RED,ColorUtils.BLACK)+"`-="+ColorUtils.getColors(ColorUtils.BROWN,ColorUtils.BLACK)+"#$"+ColorUtils.getColors(ColorUtils.YELLOW,ColorUtils.BLACK)+"%&"+ColorUtils.getColors(ColorUtils.BROWN,ColorUtils.BLACK)+"%$#"+ColorUtils.getColors(ColorUtils.RED,ColorUtils.BLACK)+"=-'"+ColorUtils.getColors(ColorUtils.BLACK,ColorUtils.BLACK)+"........");
        return bomb;
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String user = new String();
        
        
        
        String msg = String.format("stuffs a bomb down %s's pants.  The timer is set for %s seconds!  There are %s wires.  They are: %s.' % (self.victim, self.detonateTime, len(wires), utils.str.commaAndify(wires))", user);
        
        
    }
    
    private ArrayList<String> getShortColorList() {
        ArrayList<String> colors = new ArrayList<>();
        colors.add("red");
        colors.add("orange");
        colors.add("yellow");
        colors.add("green");
        colors.add("blue");
        colors.add("purple");
        colors.add("pink");
        colors.add("black");
        colors.add("brown");
        colors.add("gray");
        colors.add("white");
        return colors;
    }
    
    private ArrayList<String> getLongColorList() {
        ArrayList<String> colors = new ArrayList<>();
        colors.add("AliceBlue");
        colors.add("AntiqueWhite");
        colors.add("Aqua");
        colors.add("Aquamarine");
        colors.add("Azure");
        colors.add("Beige");
        colors.add("Bisque");
        colors.add("Black");
        colors.add("BlanchedAlmond");
        colors.add("Blue");
        colors.add("BlueViolet");
        colors.add("Brown");
        colors.add("BurlyWood");
        colors.add("CadetBlue");
        colors.add("Chartreuse");
        colors.add("Chocolate");
        colors.add("Coral");
        colors.add("CornflowerBlue");
        colors.add("Cornsilk");
        colors.add("Crimson");
        colors.add("Cyan");
        colors.add("DarkBlue");
        colors.add("DarkCyan");
        colors.add("DarkGoldenRod");
        colors.add("DarkGray");
        colors.add("DarkGreen");
        colors.add("DarkKhaki");
        colors.add("DarkMagenta");
        colors.add("DarkOliveGreen");
        colors.add("DarkOrange");
        colors.add("DarkOrchid");
        colors.add("DarkRed");
        colors.add("DarkSalmon");
        colors.add("DarkSeaGreen");
        colors.add("DarkSlateBlue");
        colors.add("DarkSlateGray");
        colors.add("DarkTurquoise");
        colors.add("DarkViolet");
        colors.add("DeepPink");
        colors.add("DeepSkyBlue");
        colors.add("DimGray");
        colors.add("DodgerBlue");
        colors.add("FireBrick");
        colors.add("FloralWhite");
        colors.add("ForestGreen");
        colors.add("Fuchsia");
        colors.add("Gainsboro");
        colors.add("GhostWhite");
        colors.add("Gold");
        colors.add("GoldenRod");
        colors.add("Gray");
        colors.add("Green");
        colors.add("GreenYellow");
        colors.add("HoneyDew");
        colors.add("HotPink");
        colors.add("IndianRed");
        colors.add("Indigo");
        colors.add("Ivory");
        colors.add("Khaki");
        colors.add("Lavender");
        colors.add("LavenderBlush");
        colors.add("LawnGreen");
        colors.add("LemonChiffon");
        colors.add("LightBlue");
        colors.add("LightCoral");
        colors.add("LightCyan");
        colors.add("LightGoldenRodYellow");
        colors.add("LightGrey");
        colors.add("LightGreen");
        colors.add("LightPink");
        colors.add("LightSalmon");
        colors.add("LightSeaGreen");
        colors.add("LightSkyBlue");
        colors.add("LightSlateGray");
        colors.add("LightSteelBlue");
        colors.add("LightYellow");
        colors.add("Lime");
        colors.add("LimeGreen");
        colors.add("Linen");
        colors.add("Magenta");
        colors.add("Maroon");
        colors.add("MediumAquaMarine");
        colors.add("MediumBlue");
        colors.add("MediumOrchid");
        colors.add("MediumPurple");
        colors.add("MediumSeaGreen");
        colors.add("MediumSlateBlue");
        colors.add("MediumSpringGreen");
        colors.add("MediumTurquoise");
        colors.add( "MediumVioletRed");
        colors.add("MidnightBlue");
        colors.add("MintCream");
        colors.add("MistyRose");
        colors.add("Moccasin");
        colors.add("NavajoWhite");
        colors.add("Navy");
        colors.add("OldLace");
        colors.add( "Olive");
        colors.add("OliveDrab");
        colors.add( "Orange");
        colors.add( "OrangeRed");
        colors.add("Orchid");
        colors.add("PaleGoldenRod");
        colors.add("PaleGreen");
        colors.add("PaleTurquoise");
        colors.add("PaleVioletRed");
        colors.add("PapayaWhip");
        colors.add("PeachPuff");
        colors.add("Peru");
        colors.add("Pink");
        colors.add("Plum");
        colors.add( "PowderBlue");
        colors.add( "Purple");
        colors.add( "Red");
        colors.add("RosyBrown");
        colors.add("RoyalBlue");
        colors.add("SaddleBrown");
        colors.add("Salmon");
        colors.add("SandyBrown");
        colors.add("SeaGreen");
        colors.add( "SeaShell");
        colors.add("Sienna");
        colors.add("Silver");
        colors.add("SkyBlue");
        colors.add("SlateBlue");
        colors.add("SlateGray");
        colors.add("Snow");
        colors.add("SpringGreen");
        colors.add("SteelBlue");
        colors.add("Tan");
        colors.add("Teal");
        colors.add("Thistle");
        colors.add("Tomato");
        colors.add("Turquoise");
        colors.add("Violet");
        colors.add("Wheat");
        colors.add("White");
        colors.add("WhiteSmoke");
        colors.add("Yellow");
        colors.add("YellowGreen");
        return colors;
    }
}

//* DeepThroat stuffs a bomb down Passo's pants.  The timer is set for 55 seconds!  There are 1 wires.  They are: white.
//<Passo> from the middle east Dahrain
//<Passo> that's where they import it
//<Passo> .cutwire white
//<DeepThroat> Passo has cut the white wire!  This has defused the bomb!
//<DeepThroat> He then quickly rearms the bomb and throws it back at Dahrain with just seconds on the clock!
//<Dahrain> ahhhhhh
//<DeepThroat> ....._.-^^---....,,-_.......
//<DeepThroat> ._--,.';,`.,';,.;;`;,.--_...
//<DeepThroat> <,.;'`".,;`..,;`*.,';`.;'>).
//<DeepThroat> I.:;.,`;~,`.;'`,.;'`,..';`I.
//<DeepThroat> .\_.`'`..`';.,`';,`';,_../..
//<DeepThroat> ....```--. . , ; .--'''.....
//<DeepThroat> ..........I.II.II...........
//<DeepThroat> ..........I.II.II...........
//<DeepThroat> .......,-=II..I.I=-,........
//<DeepThroat> .......`-=#$%&%$#=-'........
//* DeepThroat has kicked Dahrain from #asylum (BOOM!)
//<DeepThroat> Should've gone for the white wire!