/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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
 * Methods:
 *     *loadText       - loads the input file as a string
 *     *loadTextAsList - loads the input file as an array, where each line in the file is a new item in the array
 *     *readUrl        - loads the url and returns a string of the contents
 *     *addToDoc       - adds the input text as a new line at the bottom of the input text file
 *
 * Note: Only commands marked with a * are available for use outside the object
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
    public static void addToDoc(String filename, String addition){
        try{
            File file;
            
            if (filename.split("\\.").length==1){
                file =new File(filename+".txt");
                filename = filename+".txt";
            }
            else{
                file =new File(filename);
            }
            
            if(!file.exists()){
                file.createNewFile();
            }
            
            addToDoc(file,addition);
        }
        catch(Exception ex){
            ex.printStackTrace();
            return;
        }
    }
    
    public static void addToDoc(File file, String addition){
        try{
            
            if(!file.exists()){
                file.createNewFile();
            }
            
            FileWriter fileWritter = new FileWriter(file.getName(),true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write("\n"+addition);
            bufferWritter.close();
            
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}

