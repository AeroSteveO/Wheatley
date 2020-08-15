/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templat
 * and open the template in the editor.
 */
package rapternet.irc.bots.wheatley.objects;

/**
 *
 * @author thest
 */
public class Env {
    public static final String CONFIG_LOCATION = getVarOrDefault("BOT_CONFIG_FOLDER", "/config/");
    public static final String NICK = getVarOrDefault("BOT_NICK", "Wheatley");
    public static final String PASSWORD = getVarOrDefault("BOT_PASSWORD", "password");
    public static final String OWNER_NICK = getVarOrDefault("BOT_OWNER_NICK", "Steve-O");
    public static final String IRC_PORT = getVarOrDefault("BOT_IRC_PORT", "6667");
    public static final String LOGIN = getVarOrDefault("BOT_LOGIN", "Derpy");
    public static final String IRC_ADDRESS = getVarOrDefault("BOT_IRC_ADDRESS", "irc.rapternet.us");
    public static final String[] CHANNEL_LIST = getVarOrDefault("BOT_CHANNEL_LIST", "#testing,#rapterverse").split(",");
    
    public static final String IRPG_BOT_NAME = getVarOrDefault("BOT_IRPG_HOST_BOT_NAME", "idlebot");
    public static final String IRPG_CHANNEL = getVarOrDefault("BOT_IRPG_CHANNEL", "#idlerpg");
    public static final String IRPG_USERNAME = getVarOrDefault("BOT_IRPG_USERNAME", NICK);
    public static final String IRPG_PASSWORD = getVarOrDefault("BOT_IRPG_PASSWORD", "AddHere");
    public static final String IRPG_USER_CLASS = getVarOrDefault("BOT_IRPG_USER_CLASS", "Intelligence Dampening Sphere");
    
    static String getVarOrDefault(String key, String def) {
        String javaProp = System.getProperty(key, def);
        return System.getenv().getOrDefault(key, javaProp);
    }
}
