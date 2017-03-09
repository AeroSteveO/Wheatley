//new simple settings class
//one level, no nesting, accepts strings for all
//only other accepted object is list
//use list for channels
//strings for
//-server
//-port
//-nick
//-password
//-login



package Objects;

import java.io.File;
import java.util.ArrayList;


/**
 *
 * @author Stephen
 *
 * Requirements:
 * - APIs
 *    JSON (AOSP JSON parser)
 * - Custom Objects
 *    N/A
 * - Linked Classes
 *    N/A
 *
 * Object:
 *      Settings
 * - Object that extends the generic settings object and adds the necessary functions
 *   and ability to handle throttling
 *
 * Methods:
 *     *contains  - Returns true if the input key or channel/key pair exists in the map
 *     *create    - Creates the input key/value or key/channel/value pair
 *     *getString - Gets the value for the input key or key/channel
 *     *getArray  - Gets the ArrayList from the input key
 *     *getChannelList - Returns an ArrayList of channels that exist in the settings file
 *     *loadFile       - Parses the file string into a map
 *      loadText       - Loads an input file to string
 *     *save           - Saves the settings to file
 *     *setFileName    - Sets the file name to be used for saving the settings
 *      toList         - Converts JSON list to list
 *      toMap          - Converts JSONObject to map
 *      jsonToMap      - Recursively uses toList and toMap to convert JSON objects
 *                       to maps/lists
 *
 * Note: Only commands marked with a * are available for use outside the object
 *
 * Useful Resources
 * - http://developer.android.com/reference/org/json/package-summary.html
 * - http://developer.android.com/reference/java/util/Map.html
 *
 * Version: 0.6.0
 *
 */
public class SimpleSettings extends SettingsBase {
//    Map<String, String> stuff = new TreeMap<String, String>();
//    File file = new File("doNotSave");
//    Map <String, Object> settings = Collections.synchronizedMap(new TreeMap<String, Object>( ));
    
    public SimpleSettings(File file){
        super(file);
    }
    public SimpleSettings(String filename){
        super(filename);
    }
    
    public void create(String key, String value){
        key=key.toLowerCase();
        if (!settings.containsKey(key))
            settings.put(key,value);
    }
    
    public void create(String key, ArrayList<String> value){
        key=key.toLowerCase();
        if (!key.endsWith("list"))
            key += "list";
        
        if (!settings.containsKey(key))
            settings.put(key,value);
    }
    
    public String getString(String key){
        key=key.toLowerCase();
        
        if(settings.containsKey(key))
            return settings.get(key).toString();
        else
            throw new UnsupportedOperationException("KEY MISSING");
    }
    
    public ArrayList<String> getArray(String key){
        key=key.toLowerCase();
        if (!key.endsWith("list"))
            key += "list";
        if(settings.containsKey(key))
            return (ArrayList) settings.get(key);
        else
            throw new UnsupportedOperationException("KEY MISSING");
    }
    
    public void setString(String key, String value){
        key=key.toLowerCase();
        
        if(settings.containsKey(key))
            settings.put(key,value);
        else
            throw new UnsupportedOperationException("KEY MISSING");
    }
    
    public void setArray(String key, ArrayList<String> value){
        key=key.toLowerCase();
        if (!key.endsWith("list"))
            key += "list";
        
        if(settings.containsKey(key))
            settings.put(key,value);
        else
            throw new UnsupportedOperationException("KEY MISSING");
    }
    
    @Override
    public boolean contains(String key){
        key=key.toLowerCase();
        return(settings.containsKey(key));
    }
    
    @Override
    public void removeKey(String key) {
        key=key.toLowerCase();
        settings.remove(key);
    }
}