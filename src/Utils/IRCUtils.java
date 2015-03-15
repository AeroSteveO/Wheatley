/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.pircbotx.hooks.Event;

/**
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    N/A
 * - Linked Classes
 *    N/A
 * 
 * @author Steve-O
 * 
 *
 */

public class IRCUtils{

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

}