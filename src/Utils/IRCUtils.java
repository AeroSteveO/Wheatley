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
 * @author Steve-O
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    N/A
 * - Utilities
 *    N/A
 * - Linked Classes
 *    N/A
 *
 * Methods:
 *     *getTimeStamp - returns a nicely formatted time stamp
 *
 * Note: Only commands marked with a * are available for use outside the object
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
    public static String millisToPrettyPrintTime(long milliseconds) {
        String prettyPrintTime = new String();
        long x = milliseconds / 1000;
        long seconds = x % 60;
        x /= 60;
        long minutes = x % 60;
        x /= 60;
        long hours = x % 24;
        x /= 24;
        long days = x;
        
        if (days > 0) {
            prettyPrintTime += days + " days, ";
        }
        if (hours > 0) {
            prettyPrintTime += hours + " hours, ";
        }
        if (minutes > 0) {
            prettyPrintTime += minutes + " minutes, ";
        }
        prettyPrintTime += seconds + " seconds.";
        
        return prettyPrintTime;
    }
}