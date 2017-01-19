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
    
//    public boolean isEmpty(){
//        return settings.isEmpty();
//    }
    
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
    public boolean contains(String key){
        key=key.toLowerCase();
        return(settings.containsKey(key));
    }
    public void removeKey(String key) {
        key=key.toLowerCase();
        settings.remove(key);
    }

//    public void save() {
//        try{
//            if (!file.getName().equalsIgnoreCase("doNotSave")){
//                JSONObject genJSON = new JSONObject(settings);
//                
//                
//                
//                String json = genJSON.toString(2);
//                try{
//                    
//                    file.createNewFile(); // We're just replacing the old file, not modifying it
//                    
//                    FileWriter fileWritter = new FileWriter(file.getName());
//                    BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
//                    bufferWritter.write(json);
//                    bufferWritter.close();
//                    System.out.println("FILE SAVED");
//                }catch(IOException e){
//                    System.out.println(file.getName()+" HAS NOT BEEN SAVED");
//                    e.printStackTrace();
//                }
//            }
//            else
//                System.out.println("FILE HAS NOT BEEN SAVED, NO FILENAME INPUT");
//        }
//        catch (Exception ex){
//            System.out.println("FILE SAVE HAS FAILED");
//            ex.printStackTrace();
//        }
//    }
//    public boolean loadFile() throws IOException, JSONException{
//        
//        
//        try{
//            String json = loadText(); // Should only have one line of text
//            
//            if (json!=null&&!json.equals("")){
//                JSONObject object = (JSONObject) new JSONTokener(json).nextValue();
//                
//                settings = jsonToMap(object);
//                
//            }
//            else{
//                System.out.println(file.getName()+" IS EMPTY");
//                return false;
//            }
//        }catch(Exception e){
//            System.out.println(file.getName()+" HAS NOT BEEN LOADED");
//            System.out.println(e.getMessage());
//            e.printStackTrace();
//            return false;
//        }
//        return true;
//    }
//    
//    
//    public void setFileName(String filename){
//        this.file = new File(filename);
//    }
//    public void setFile(File file){
//        this.file = file;
//    }
//    
//    private String loadText() throws FileNotFoundException, IOException{
//        //if file doesnt exists, then create it
//        if(!file.exists()){
//            file.createNewFile();
//            return null;
//        }
//        
//        try{
//            Scanner wordfile = new Scanner(file);
//            String wordls = "";
//            while (wordfile.hasNext()){
//                wordls= wordls+(wordfile.nextLine());
//            }
//            wordfile.close();
//            return (wordls);
//        } catch (FileNotFoundException ex) {
//            System.out.println("TEXT LOADER FAILED");
//            ex.printStackTrace();
//            return null;
//        }
//    }
//    
//    private static Map jsonToMap(JSONObject json) throws JSONException {
//        Map<String, Object> retMap = new HashMap<String, Object>();
//        
//        if(json != JSONObject.NULL) {
//            retMap = toMap(json);
//        }
//        else
//            System.out.println("JSON EMPTY");
//        return retMap;
//    }
//    
//    private static Map toMap(JSONObject object) throws JSONException {
//        Map<String, Object> map = new HashMap<String, Object>();
//        
//        Iterator<String> keysItr = object.keys();
//        while(keysItr.hasNext()) {
//            String key = keysItr.next();
//            Object value = object.get(key);
//            
//            if(value instanceof JSONArray) {
//                value = toList((JSONArray) value);
//            }
//            
//            else if(value instanceof JSONObject) {
//                value = toMap((JSONObject) value);
//            }
//            map.put(key, value);
//        }
//        return map;
//    }
//    
//    private static List toList(JSONArray array) throws JSONException {
//        List<Object> list = new ArrayList<Object>();
//        for(int i = 0; i < array.length(); i++) {
//            Object value = array.get(i);
//            if(value instanceof JSONArray) {
//                value = toList((JSONArray) value);
//            }
//            
//            else if(value instanceof JSONObject) {
//                value = toMap((JSONObject) value);
//            }
//            list.add(value);
//        }
//        return list;
//    }
}