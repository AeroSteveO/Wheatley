/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Objects.ChannelStore.ChannelArray;
import Objects.Command;
import java.lang.annotation.Annotation;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.Event;
import org.reflections.Reflections;

/**
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
    public static PircBotX bot;
    public static ChannelArray channels = new ChannelArray();
    public static String commandPrefix = "!";              // Not implemented in all functions
    public static String phrasePrefix = new String();      //mainNick+", ";
    public static ArrayList<String> botAdmin = getBotAdmins();
//    public static final Reflections wheatleyReflection = new Reflections("org.pircbotx");
    public static List<Command> commandList = new ArrayList<>();
    
    public static String getTimestamp(Event event){
        TimeZone tz = TimeZone.getDefault();
        Date timestamp = new Date(Long.parseLong(String.valueOf(event.getTimestamp())));
        DateFormat timeFormat = new SimpleDateFormat("M/d/yy h:mm aa");
        String tzShortString = tz.getDisplayName(tz.inDaylightTime(new Date()), TimeZone.SHORT);
        String outputTime = timeFormat.format(timestamp) + " " + tzShortString;
        return outputTime;
    }
    
    public static String getTimestamp(String date) {
        TimeZone tz = TimeZone.getDefault();
        Date timestamp = new Date(Long.parseLong(date));
        DateFormat timeFormat = new SimpleDateFormat("M/d/yy h:mm aa");
        String tzShortString = tz.getDisplayName(tz.inDaylightTime(new Date()), TimeZone.SHORT);
        String outputTime = timeFormat.format(timestamp) + " " + tzShortString;
        return outputTime;
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