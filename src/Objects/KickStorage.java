/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objects;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class stores kick data in a .json file and gives accessor methods into
 * the data as well as other helpful methods for maintaining the kick data
 * @author Stephen
 */
public class KickStorage extends SettingsBase {
    private static String MESSAGE_KEY = "message";
    private static String USERS_KEY = "users";
    private static String CHANNELS_KEY = "channels";
    private static String IS_WHITELIST_KEY = "iswhitelist";
    private static String FAILURE_KEY = "failure";
    private static String IS_ENABLED_KEY = "isenabled";

    public KickStorage(String filename) {
        this(new File(filename));
    }
    
    public KickStorage(File file) {
        super(file);
        if (isEmpty()) {
            addKick("hack", "Wheatley has killed you in his attempt to plug your brain into a computer", 
                    "Wheatley has killed you in an attempt to counter hack your brain");
            addKick("smash", "Aristotle vs MASHY-SPIKE-PLATE", "MASHY-SPIKE-PLATE smashed you into goop");
            save();
        }
    }
    
    public void addKick(String command, String message) {
        addKick(command, message, "null", "null", "null", "false");
    }
    
    public void addKick(String command, String message, String failureMessage) {
        addKick(command, message, failureMessage, "null", "null", "false");
    }
    public void addKick(String command, String message, String failureMessage, String allowedUsers, String blockedChans, String isWhitelist) {
        command = command.toLowerCase();
        if (isWhitelist == null) {
          isWhitelist = "false";
        }
        if (!isWhitelist.equals("true") && !isWhitelist.equals("false")) {
          return;
        }
        if (message == null) {
          message = "null";
        }
        if (allowedUsers == null) {
          allowedUsers = "null";
        }        
        if (blockedChans == null) {
          blockedChans = "null";
        }
        if (failureMessage == null) {
          failureMessage = "null";
        }        


        TreeMap<String,String> newKick = new TreeMap<>();
        newKick.put(MESSAGE_KEY, message);
        newKick.put(USERS_KEY, allowedUsers);
        newKick.put(CHANNELS_KEY, blockedChans);
        newKick.put(IS_WHITELIST_KEY, isWhitelist);
        newKick.put(IS_ENABLED_KEY, "true");
        newKick.put(FAILURE_KEY, failureMessage);
        
        
        System.out.println(command + " Should be added");
        System.out.println(message);
        System.out.println(failureMessage);
        settings.put(command, newKick);
        this.save();
    }
    
    public boolean copyInFromStorage(KickStorage s, String key) {
      return copyInFromStorage(s, key, key);
    }
    
    public boolean copyInFromStorage(KickStorage s, String key, String newName) {
        if (this.contains(newName)) {
            System.err.println("KEY ALREADY IN USE");
            return false;
        }
        if (!s.contains(key)) {
          System.err.println("KEY NOT AVAILABLE");
          return false;
        }
        TreeMap<String,String> newKick = new TreeMap<>();
        newKick.put(MESSAGE_KEY, s.getMessage(key));
        newKick.put(USERS_KEY, (String)((Map)s.settings.get(key)).get(USERS_KEY));
        newKick.put(CHANNELS_KEY, (String)((Map)s.settings.get(key)).get(CHANNELS_KEY));
        newKick.put(IS_WHITELIST_KEY, (String)((Map)s.settings.get(key)).get(IS_WHITELIST_KEY));
        newKick.put(IS_ENABLED_KEY, "true");
        newKick.put(FAILURE_KEY, s.getFailureMessage(key));
        
        settings.put(newName, newKick);
        save();
        return true;
    }
    public void disableKick(String key) {
        ((Map) settings.get(key)).put(IS_ENABLED_KEY, "false");
        this.save();
    }
    public void enableKick(String key) {
        ((Map) settings.get(key)).put(IS_ENABLED_KEY, "true");
        this.save();
    }
    
    public ArrayList<String> getKickCommands() {
        return getKeyList();
    }
    
    public String getMessage(String key) {
        key = key.toLowerCase();
        return (String) ((Map) settings.get(key)).get(MESSAGE_KEY);
    }
    
    public ArrayList<String> getAllowedUsers(String key) {
        key = key.toLowerCase();
        if ((((Map) settings.get(key)).get(USERS_KEY))==null) {
            return null;
        }
        if (((String) ((Map) settings.get(key)).get(USERS_KEY)).equals("null")) {
            return null;
        }
        
        
        ArrayList<String> users = new ArrayList();
        
        String[] userString = ((String) ((Map) settings.get(key)).get(USERS_KEY)).split(" ");
        
        for (int i = 0; i < userString.length; i++) {
            users.add(userString[i]);
        }
        
        return users;
    }
    
    public ArrayList<String> getChannelList(String key) {
        key = key.toLowerCase();
        if ((((Map) settings.get(key)).get(CHANNELS_KEY))==null) {
            return null;
        }
        if (((String) ((Map) settings.get(key)).get(CHANNELS_KEY)).equals("null")) {
            return null;
        }

        
        ArrayList<String> channels = new ArrayList();
        
        String[] channelNames = ((String) ((Map) settings.get(key)).get(CHANNELS_KEY)).split(" ");
        
        for (int i = 0; i < channelNames.length; i++) {
            channels.add(channelNames[i]);
        }
        
        return channels;
    }
    
    public String getFailureMessage(String key) {
        key = key.toLowerCase();
        return (String) ((Map) settings.get(key)).get(FAILURE_KEY);
    }

    public boolean isChannelListWhitelist(String key) {
        key = key.toLowerCase();
        if (( ((Map) settings.get(key)).get(IS_WHITELIST_KEY))==null) {
            return false;
        }
        if (((String) ((Map) settings.get(key)).get(IS_WHITELIST_KEY)).equals("null")) {
            return false;
        }

        return  Boolean.parseBoolean((String) ((Map) settings.get(key)).get(IS_WHITELIST_KEY));
    }
    
    public boolean isKickEnabled(String key) {
        key = key.toLowerCase();
        Boolean bool = Boolean.parseBoolean((String) ((Map) settings.get(key)).get(IS_ENABLED_KEY));
        return  bool;
    }
}
