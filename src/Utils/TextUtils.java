/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Stephen
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    N/A
 * - Linked Classes
 *    N/A
 * 
 * 
 *
 */
public class TextUtils {
    public static String loadText(String filename) throws FileNotFoundException, IOException{
        File file =new File(filename);
        
        return (loadText(file));
    }
    public static String loadText(File file) throws FileNotFoundException, IOException{
//        File file =new File(filename);
        //if file doesnt exists, then create it
        if(!file.exists()){
            file.createNewFile();
            return null;
        }
        
        try{
            Scanner wordfile = new Scanner(file);
            String wordls = "";
            while (wordfile.hasNext()){
                wordls= wordls+(wordfile.next());
            }
            wordfile.close();
            return (wordls);
        } catch (FileNotFoundException ex) {
            System.out.println("TEXT LOADER FAILED");
            ex.printStackTrace();
            return null;
        }
    }
    public static ArrayList<String> loadTextAsList(String filename) throws FileNotFoundException{
        return loadTextAsList(new File(filename));
    }
    
    public static ArrayList<String> loadTextAsList(File file) throws FileNotFoundException{
        try{
            Scanner wordfile = new Scanner(file);
//new File("wordlist.txt")
            ArrayList<String> wordls = new ArrayList<String>();
            while (wordfile.hasNext()){
                wordls.add(wordfile.next());
            }
            wordfile.close();
            return (wordls);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    //converts URL to string, primarily used to string-ify json text
    private static String readUrl(String urlString) throws Exception {
//        System.out.println(urlString);
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder buffer = new StringBuilder();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            return buffer.toString();
        }catch(IOException ex){
            return(null);
        }finally {
            if (reader != null)
                reader.close();
        }
    }
}
