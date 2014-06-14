/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import Wheatley.ChannelStore.ChannelArray;
import java.util.Vector;
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
    //public static List<String> Channels = new ArrayList<>();
    public static ChannelArray Channels = new ChannelArray();
}