/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Objects.ChannelStore.ChannelArray;
import Objects.Settings;
import Objects.Throttle;
import java.util.ArrayList;
import org.pircbotx.PircBotX;

/**
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    Settings
 *    Throttle
 *    ChannelStore
 * - Linked Classes
 *    N/A
 * 
 * @author Steve-O
 * botOwner      - User with all powers over the bot, and ability to shut the bot down
 * mainNick      - The intended nickname for the bot, not necessarily the current nickname
 * nickPass      - The nickServ password for the bot
 * botAdmins     - Administrators of the bot, ability to use some commands
 * commandPrefix - The character that signals the bot that a command is being sent
 * reconnect     - Boolean to activate the aggressive server reconnect loop
 * bot           - Current PircBotX bot object
 * channels      - Array of settings for each channel the bot is in
 *
 */
public class Global {
    
    public static String botOwner = new String(); //Updated in the Main .java file from Setings.XML
    public static String mainNick = new String(); //Updated in the Main .java file from Setings.XML
    public static String nickPass = new String(); //Updated in the Main .java file from Setings.XML
    public static boolean reconnect = true;
    
    public static String mainServer = new String();
    public static String serverPort = new String();
    
    public static PircBotX bot;
    public static PircBotX whatPreBot;
    
    public static boolean relay = false;
    
    public static String mainChan = "#rapterverse";
    
    public static ChannelArray channels = new ChannelArray();
    public static String commandPrefix = "!";              // Not implemented in all functions
    public static String phrasePrefix = new String();      //mainNick+", ";
    public static ArrayList<String> botAdmin = getBotAdmins();
//    public static final Reflections wheatleyReflection = new Reflections("org.pircbotx");
    //public static List<Command> commandList = new ArrayList<>();
    
    public static Settings settings = new Settings("settings.json");
    public static Throttle throttle = new Throttle("throttle.json");
    
    public static String[] getBotOps(){
        String[] admins = {"Steve-O","burg","theDoctor"};
        return admins;
    }
            
    private static ArrayList<String> getBotAdmins() {
        ArrayList<String> admins = new ArrayList<>();
        admins.add(botOwner);
        admins.add("theDoctor");
        admins.add("burg");
        return(admins);
    }
//    public static void addCommands(List<Command> list, Class<? extends Annotation> cl) {
//        Set<Class<?>> classes = wheatleyReflection.getTypesAnnotatedWith(cl);
//        for (Class<?> clss : classes) {
//            try {
//                System.out.println(((Command) clss.newInstance()).toString());
//                list.add(((Command) clss.newInstance()));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    public static void registerCommandList() {
//        for (Command command : commandList) {
//            commandList.add(command.getCommand());
//        }
//    }
}