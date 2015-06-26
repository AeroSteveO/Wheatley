/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.io.File;
import java.util.ArrayList;
import org.joda.time.DateTime;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.ConnectEvent;
import org.pircbotx.hooks.events.KickEvent;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Stephen
 */
public class WhatPre extends ListenerAdapter {
    String preChan = "#***REMOVED***-announce"; // Music pre channel
    String preBot = "Drone"; // Music pre bot name
    String musicFolderLocation = "/media/backup/Music - Stephen/"; // Music folder location
//    String musicFolderLocation = "music/";
    ArrayList<String> bands = loadBandNamesFromFolders(); // List of bands from folder names in the music folder
    ArrayList<String> announcedPres = new ArrayList<>();  // Pre's that have been announced to the channel
    
    @Override
    public void onConnect(ConnectEvent event){
        event.getBot().sendIRC().message("Drone","enter :***REMOVED***,#***REMOVED***-announce ***REMOVED*** ***REMOVED***");
    }
    
    @Override
    public void onKick(KickEvent event) throws Exception {
        if (event.getRecipient().getNick().equals(event.getBot().getNick())) {
            event.getBot().sendIRC().joinChannel(event.getChannel().getName());
        }
    }
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        
        String message = Colors.removeFormattingAndColors(event.getMessage().trim());
        String[] msgSplit = message.split(" ");
        
        if (event.getChannel().getName().equalsIgnoreCase(preChan)&&event.getUser().getNick().equalsIgnoreCase(preBot)){
            
            String artist = message.split("\\s-\\s")[0].trim();
            String album = message.split("\\s-\\s",2)[1].split("\\[")[0].trim();
            
            int year = 0;
            String type = new String();
            String info = message.split("-")[message.split("-").length-1];
            
            for (int i=0;i<msgSplit.length;i++){
                if (msgSplit[i].matches("\\[[0-9]+\\]")){
                    year = Integer.parseInt(msgSplit[i].replaceAll("\\[|\\]","").trim());
                }
                else if (msgSplit[i].matches("\\[[a-zA-Z]+\\]")){
                    type = msgSplit[i].replaceAll("\\[|\\]","");
                }
            }
            
            if (bands.contains(artist.toLowerCase()) && (year >= (DateTime.now().getYear() - 2)) && !announcedPres.contains(artist.toLowerCase() + " - " + album.toLowerCase())){
                Global.bot.sendIRC().message(Global.mainChan,Colors.BOLD+"ARTIST: "+Colors.NORMAL+artist+Colors.BOLD+" ALBUM: "+Colors.NORMAL+album+Colors.BOLD+" Year: "+Colors.NORMAL+year+Colors.BOLD+" Type: "+Colors.NORMAL+type);
                announcedPres.add(artist.toLowerCase() + " - " + album.toLowerCase());
            }
        }
        if (event.getChannel().getName().equalsIgnoreCase(Global.mainChan)){
            
            if(message.equalsIgnoreCase(Global.commandPrefix+"loadmusic")&&event.getUser().getNick().equalsIgnoreCase("***REMOVED***")){
                bands = loadBandNamesFromFolders();
                event.respond("Band folder names reloaded");
            }
            if (Global.relay)
                Global.bot.sendIRC().message(Global.mainChan,"<"+event.getUser().getNick()+"> "+event.getMessage());
            
            if (message.equalsIgnoreCase(Global.commandPrefix+"relay")){
                if (event.getUser().getNick().equals("***REMOVED***")&&event.getUser().isVerified()){
                    if (Global.relay){
                        Global.relay = false;
                        event.respond("RELAY DISABLED");
                    }
                    else{
                        Global.relay = true;
                        event.respond("RELAY ENABLED");
                    }
                }
            }
            if (msgSplit[0].equalsIgnoreCase(Global.commandPrefix+"addband")){
                
            }
        }
    }
    
    private ArrayList<String> loadBandNamesFromFolders(){
        File folder = new File(musicFolderLocation);
        
        File[] listOfFilesAndFolders = folder.listFiles();
        ArrayList<String> listOfFiles = new ArrayList<>();// = new File[];
        
        for (int i = 0; i < listOfFilesAndFolders.length; i++) {
            if (listOfFilesAndFolders[i].isFile()) {
//                listOfFiles.add(listOfFilesAndFolders[i].getName());
            } else if (listOfFilesAndFolders[i].isDirectory()) {
//                System.out.println("Directory: " + listOfFilesAndFolders[i].getName());
                listOfFiles.add(listOfFilesAndFolders[i].getName().toLowerCase());
            }
        }
        return listOfFiles;
    }
}