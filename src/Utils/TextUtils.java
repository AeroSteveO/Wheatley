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
import java.net.HttpURLConnection;
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
 *  - Utilities
 *    N/A
 * - Linked Classes
 *    N/A
 *
 * Methods:
 *     *loadText         - Loads the input file as a string
 *     *loadTextAsList   - Loads the input file as an array, where each line in the file is a new item in the array
 *     *readUrl          - Loads the url and returns a string of the contents
 *     *addToDoc         - Adds the input text as a new line at the bottom of the input text file
 *     *addToDocIfUnique - Adds a line to the end of the input text file if that line is unique in that file
 *     *readUrlUsingGet  - Reads the input URL using a GET and the class user agent and returns a String
 *                         (Sometimes this will work when readUrl does not)
 *
 * Note: Only commands marked with a * are available for use outside the object
 *
 */
public class TextUtils {
    private final String USER_AGENT = "Mozilla/5.0";
    
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
                wordls= wordls+(wordfile.nextLine());
            }
            wordfile.close();
            return (wordls);
        } catch (FileNotFoundException ex) {
            System.out.println("TEXT LOADER FAILED");
            ex.printStackTrace();
            return null;
        }
    }
    public static ArrayList<String> loadTextAsList(String filename) {
        return loadTextAsList(new File(filename));
    }
    
    public static ArrayList<String> loadTextAsList(File file) {
        try{
            
            if(!file.exists()){
                file.createNewFile();
                return null;
            }
            
            Scanner wordfile = new Scanner(file);
//new File("wordlist.txt")
            ArrayList<String> wordls = new ArrayList<String>();
            while (wordfile.hasNext()){
                wordls.add(wordfile.nextLine().trim());
            }
            wordfile.close();
            return (wordls);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    //converts URL to string, primarily used to string-ify json text
    public static String readUrl(String urlString) throws Exception {
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
            ex.printStackTrace();
            return(null);
        }finally {
            if (reader != null)
                reader.close();
        }
    }
    
    public String readUrlUsingGet(String url) throws Exception {
        
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        
        con.setRequestMethod("GET");
        
        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);
        
//        con.setRequestProperty("X-Mashape-Key", key);
        
        int responseCode = con.getResponseCode();
//        System.out.println("\nSending 'GET' request to URL : " + url);
//        System.out.println("Response Code : " + responseCode);
        
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        
//        System.out.println(response.toString());
        return response.toString();
    }
    
    public static boolean addToDocIfUnique(String filename, String addition){
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
            
            return addToDocIfUnique(file,addition);
            
        }
        catch(Exception ex){
            ex.printStackTrace();
            return false;
        }
    }
    
    public static boolean addToDocIfUnique(File file, String addition){
        try{
            
            if(!file.exists()){
                file.createNewFile();
            }
            ArrayList<String> listing = loadTextAsList(file);
            CaseInsensitiveList current = new CaseInsensitiveList();
            
            current.addAll(listing);
            
            if (current.contains(addition)){
                return false;
            }
            else{
                FileWriter fileWritter = new FileWriter(file.getName(),true);
                BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                bufferWritter.write("\n"+addition);
                bufferWritter.close();
                return true;
            }
        } catch (Exception ex){
            ex.printStackTrace();
            return false;
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
    private static class CaseInsensitiveList extends ArrayList<String> {
        @Override
        public boolean contains(Object o) {
            String paramStr = (String)o;
            for (String s : this) {
                if (paramStr.equalsIgnoreCase(s)) return true;
            }
            return false;
        }
    }
}
