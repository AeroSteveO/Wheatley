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
 * YES GASP I KNOW GLOBAL VARIABLES ARE REALLY NICE SOMETIMES
 *
 */
public class Global {
    public static String BotOwner = new String(); //Updated in the Main .java file from Setings.XML
    public static String MainNick = new String(); //Updated in the Main .java file from Setings.XML
    public static String NickPass = new String(); //Updated in the Main .java file from Setings.XML
    public static boolean reconnect = true;
    public static PircBotX bot;
    public static ChannelArray Channels = new ChannelArray();
    public static String commandPrefix = "!";       // Not implemented yet in other functions
    static GameArray activeGame = new GameArray();  // To be implemented in games
}