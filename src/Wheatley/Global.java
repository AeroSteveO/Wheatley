/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Objects.ChannelStore.ChannelArray;
import Objects.Game.GameArray;
import org.pircbotx.PircBotX;

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
 * activeGame    - Array of games currently active in channels the bot is in
 * 
 */
public class Global {
    public static String botOwner = new String(); //Updated in the Main .java file from Setings.XML
    public static String mainNick = new String(); //Updated in the Main .java file from Setings.XML
    public static String nickPass = new String(); //Updated in the Main .java file from Setings.XML
    public static boolean reconnect = true;
    public static PircBotX bot;
    public static ChannelArray channels = new ChannelArray();
    public static String commandPrefix = "!";              // Not implemented yet in other functions
    public static GameArray activeGame = new GameArray();  // To be implemented in games
}