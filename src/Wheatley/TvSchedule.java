/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author Stephen
 * 
 * Requirements:
 * - APIs
 *    JodaTime
 * - Custom Objects
 *    N/A
 * - Linked Classes
 *    N/A
 * 
 * Activate commands with:
 *      !schedule [YYYY-MM-DD]
 *          Looks up the TV rage schedule for the given day (only shows after 8PM)
 *      !schedule first
 *          Looks up the first available date in the TV rage schedule
 *      !schedule last
 *          Looks up the last available date in the TV rage schedule
 *      !Tonight
 *          Looks up the TV rage schedule for the current night (shows after 8PM)
 *      !Tomorrow
 *          Looks up the TV rage schedule for tomorrow night (shows after 8PM)
 *      !Yesterday
 *          Looks up the TV rage schedule for last night (shows after 8PM)
 * 
 * 
 * http://jaxen.org/apidocs/index.html
 * 
 * 
 */
public class TvSchedule extends ListenerAdapter{
    ArrayList<String> nightTimes = getNightTvTimes();
    Element cache = initializeCache();
    DateTime cacheExpiration = new DateTime().plusDays(1);
    String key = "";
    ArrayList<String> whiteList = getWhiteList();
    // <Matthias> CW, ABC, CBS, NBC, and FOX for the main networks
    //<Matthias> TBS, TNT, SYFY, AMC, showtime, hbo
    
    public TvSchedule() {
        if (!Global.settings.contains("tvr-api")) {
            Global.settings.create("tvr-api","AddHere");
            System.out.println("ERROR: NO TV RAGE API KEY");
        }
        key = Global.settings.get("tvr-api");

    }

    
    private ArrayList<String> getWhiteList(){
        ArrayList<String> whiteList = new ArrayList<>();
        whiteList.add("cw");
        whiteList.add("abc");
        whiteList.add("cbs");
        whiteList.add("nbc");
        whiteList.add("fox");
        whiteList.add("tbs");
        whiteList.add("tnt");
        whiteList.add("syfy");
        whiteList.add("amc");
        whiteList.add("showtime");
        whiteList.add("hbo");

        return whiteList;
    }
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        if (message.startsWith(Global.commandPrefix)&&!message.matches("([ ]{0,}!{1,}[ ]{0,}){1,}")){
            String command = message.split(Global.commandPrefix)[1];
            String[] cmdSplit = command.split(" ");
            
            if (command.equalsIgnoreCase("t")||command.equalsIgnoreCase("tonight")){
                String today = getTodaysDate();
                ArrayList<String> schedule = getSchedule(today);
                
                
                if (schedule==null || schedule.isEmpty()){
                    event.getBot().sendIRC().message(event.getChannel().getName(), "No shows found for date "+today);
                }
                else{
                    event.getBot().sendIRC().message(event.getUser().getNick(),Colors.BOLD+"Schedule for: "+Colors.NORMAL+today);
                    for (int i=0;i<schedule.size();i++){
                        event.getBot().sendIRC().message(event.getUser().getNick(),schedule.get(i));
                    }
                }
            }
            else if (command.equalsIgnoreCase("yesterday")){
                String today = getYesterdaysDate();
                ArrayList<String> schedule = getSchedule(today);
                
                
                if (schedule==null || schedule.isEmpty()){
                    event.getBot().sendIRC().message(event.getChannel().getName(), "No shows found for date "+today);
                }
                else{
                    event.getBot().sendIRC().message(event.getUser().getNick(),Colors.BOLD+"Schedule for: "+Colors.NORMAL+today);
                    for (int i=0;i<schedule.size();i++){
                        event.getBot().sendIRC().message(event.getUser().getNick(),schedule.get(i));
                    }
                }
            }
            else if (command.equalsIgnoreCase("tomorrow")){
                String today = getTomorrowsDate();
                ArrayList<String> schedule = getSchedule(today);
                
                if (schedule==null || schedule.isEmpty()){
                    event.getBot().sendIRC().message(event.getChannel().getName(), "No shows found for date "+today);
                }
                else{
                    event.getBot().sendIRC().message(event.getUser().getNick(),Colors.BOLD+"Schedule for: "+Colors.NORMAL+today);
                    for (int i=0;i<schedule.size();i++){
                        event.getBot().sendIRC().message(event.getUser().getNick(),schedule.get(i));
                    }
                }
            }
            else if (cmdSplit[0].equalsIgnoreCase("schedule")&&cmdSplit.length==2){
//                System.out.println(date.getYear());
                if(cmdSplit[1].matches("20([0-9][0-9])\\-((0|1)*[0-9])\\-((0|1|2|3)[0-9])")){
                    ArrayList<String> schedule = getSchedule(cmdSplit[1]);
                    
                    
                    if (schedule==null || schedule.isEmpty()){
                        event.getBot().sendIRC().message(event.getChannel().getName(), "No shows found for date "+cmdSplit[1]);
                    }
                    else{
                        event.getBot().sendIRC().message(event.getUser().getNick(),Colors.BOLD+"Schedule for: "+Colors.NORMAL+cmdSplit[1]);
                        for (int i=0;i<schedule.size();i++){
                            event.getBot().sendIRC().message(event.getUser().getNick(),schedule.get(i));
                        }
                    }
                }
                else if (cmdSplit[1].equalsIgnoreCase("last")){
                    event.getBot().sendIRC().message(event.getChannel().getName(),"The last date available is "+lastDate()+" (Note: The schedule for dates far in the future may be incomplete/non-existant)");
                }
                else if (cmdSplit[1].equalsIgnoreCase("first")){
                    event.getBot().sendIRC().message(event.getChannel().getName(),"The first date available is "+earliestDate());
                }
                else if (cmdSplit[1].equalsIgnoreCase("format")){
                    event.getBot().sendIRC().notice(event.getUser().getNick(), "Schedule inputs take the format of [YYYY-MM-DD], for example: !schedule 2015-4-25");
                }
                else{
                    event.getBot().sendIRC().notice(event.getUser().getNick(), "Schedule date does not match the YYYY-MM-DD format, or date is out of valid range");
                }
            }
        }
    }
    
    // Provides the earliest date available in the TV rage api (in YYYY-MM-DD format)
    private String earliestDate(){
        Element baseElement = getTvRageData();
        ArrayList<String> schedule = new ArrayList<>();
        NodeList day = baseElement.getElementsByTagName("DAY");
        
        String minimumDate = day.item(0).getAttributes().getNamedItem("attr").getNodeValue();
        return minimumDate;
    }
    
    //Provides the last date available in the TV rage api (in YYYY-MM-DD format)
    private String lastDate(){
        Element baseElement = getTvRageData();
        ArrayList<String> schedule = new ArrayList<>();
        NodeList day = baseElement.getElementsByTagName("DAY");
        
        String minimumDate = day.item(day.getLength()-1).getAttributes().getNamedItem("attr").getNodeValue();
        return minimumDate;
    }
    
    private ArrayList<String> getSchedule(String today){
        Element baseElement = getTvRageData();
        ArrayList<String> schedule = new ArrayList<>();
        NodeList day = baseElement.getElementsByTagName("DAY");
        
        int dayLocation = 0;
        boolean found = false;
        {
            int i=0;
            while (!found && i < day.getLength()){
                
                found = day.item(i).getAttributes().getNamedItem("attr").getNodeValue().equalsIgnoreCase(today);
                if (found){
                    dayLocation=i;
                }
                else
                    i++;
            }
        }
        
        Element dayElement = (Element) baseElement.getElementsByTagName("DAY").item(dayLocation);
        
        for(int i=0;i<dayElement.getElementsByTagName("time").getLength();i++) { // LOOP THROUGH ALL THE TIMES
            for(int o=0;o<nightTimes.size();o++){ // CHECK TO SEE IF THE FOUND TIME IS ONE OF THE NIGHT TIME ALLOWED HOURS
                if (dayElement.getElementsByTagName("time").item(i).getAttributes().getNamedItem("attr").getNodeValue().matches(nightTimes.get(o))){
                    
                    Element showElement = (Element) dayElement.getElementsByTagName("time").item(i);
                    String showTime = dayElement.getElementsByTagName("time").item(i).getAttributes().getNamedItem("attr").getNodeValue();
                    
                    
                    for (int c=0;c < showElement.getElementsByTagName("show").getLength(); c++) { // LOOP THROUGH ALL THE SHOWS IN THE FOUND HOUR
                        
                        Element show = (Element) showElement.getElementsByTagName("show").item(c);
                        
                        String network = showElement.getElementsByTagName("network").item(c).getTextContent();
                        String showName = show.getAttributes().getNamedItem("name").getNodeValue();
                        
                        String showEpisode = show.getElementsByTagName("ep").item(0).getTextContent();
                        
                        if (showEpisode.matches("[0-9]+x[0-9]+"))
                            showEpisode = "S" + showEpisode.replaceAll("x", "E");
                        
                        showTime = prettifyShowTime(today,showTime);
                        
                        if (showTime==(null)){
                            return null;
                        }
                        else if (whiteList.contains(network.toLowerCase())){
                            String showTitle = showElement.getElementsByTagName("title").item(c).getTextContent();
                            schedule.add("["+showTime+"] "+Colors.RED+" ("+network+") "+Colors.NORMAL+Colors.BOLD+showName + ": "+Colors.NORMAL+showTitle + Colors.GREEN + " ("+showEpisode+")" + Colors.NORMAL);
                        }
                    }
                }
            }
        }
        return schedule;
    }
    
    private String getTodaysDate(){
        return (todayPlus(0));
    }
    private String getYesterdaysDate(){
        return (todayPlus(-1));
    }
    private String getTomorrowsDate(){
        return (todayPlus(1));
    }
    private String todayPlus(int n){
        DateTime today = new DateTime().plusDays(n);
        String day = today.dayOfMonth().getAsString();
        String month = String.valueOf(today.getMonthOfYear());
        String year = String.valueOf(today.getYear());
        return (year+"-"+month+"-"+day);
    }
    private Element getTvRageData(){
        if (new DateTime().isAfter(cacheExpiration)){
            try{
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Element baseElement = (Element) dBuilder.parse("http://services.tvrage.com/feeds/fullschedule.php" + "?key="+key).getElementsByTagName("schedule").item(0);
                return baseElement;
            } catch(Exception ex){
                return null;
            }
        }
        else{
            return cache;
        }
    }
    
    private ArrayList<String> getNightTvTimes() {
        ArrayList<String> times = new ArrayList<>();
        times.add("08:.*pm");
        times.add("09:.*pm");
        times.add("10:.*pm");
        return times;
    }
    
    private Element initializeCache() {
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Element baseElement = (Element) dBuilder.parse("http://services.tvrage.com/feeds/fullschedule.php" + "?key="+key).getElementsByTagName("schedule").item(0);
            return baseElement;
        } catch(Exception ex){
            return null;
        }
    }
    
    private String prettifyShowTime(String today, String showTime) {
        String[] dayInfo = today.split("-");
        int year = Integer.parseInt(dayInfo[0]);
        int month = Integer.parseInt(dayInfo[1]);
        int day = Integer.parseInt(dayInfo[2]);
        int hour = Integer.parseInt(showTime.split(":")[0]);
        int minute = Integer.parseInt(showTime.split(":")[1].split(" ")[0]);
        try{
            
            DateTime haveaNiceDay = new DateTime(year, month,  day, hour, minute);
            
            DateTimeFormatter fmt = DateTimeFormat.forPattern("hh:mm");
            
            return (fmt.print(roundDate(haveaNiceDay,30))+" "+showTime.split(" ")[1]);
        }
        catch (Exception ex){
            return null;
        }
    }
    
    private DateTime roundDate(final DateTime dateTime, int minutes) {
        
        if (minutes < 1 || 60 % minutes != 0) {
            throw new IllegalArgumentException("minutes must be a factor of 60");
        }
        
        final DateTime hour = dateTime.hourOfDay().roundFloorCopy();
        final long millisSinceHour = new Duration(hour, dateTime).getMillis();
        final int roundedMinutes = ((int)Math.round(
                millisSinceHour / 60000.0 / minutes)) * minutes;
        return hour.plusMinutes(roundedMinutes);
    }
}