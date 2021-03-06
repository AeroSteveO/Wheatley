/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.objects;

import rapternet.irc.bots.common.utils.TextUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.joda.time.DateTime;
import org.joda.time.Period;
import rapternet.irc.bots.wheatley.objects.games.WordGame;

/**
 *
 * @author Steve-O
 * Game Object built to simplify building IRC based games
 * ->stores game specific settings and ensures only one instance of a game can be in each channel
 * ->reduces redundant code and allows for re-use of code across games
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    N/A
 * - Linked Classes
 *    N/A
 * 
 * ->Current Modifiers
 * ---->Shuffle
 *          Shuffles the characters in the chosen word
 * ---->Blank
 *          Changes the characters in the chosen word to all underscores
 * ---->Reverse
 *          Reverses the characters in the chosen word
 * ---->None
 *          Does nothing
 * ---->Int Array
 *          Creates an array of integers using the number of characters and number of unique characters
 * ---->Int
 *          Creates an int using the upper bounds input
 */
public class Game {
    private List<String> wordList = TextUtils.loadTextAsList(Env.CONFIG_LOCATION + "wordlist.txt");//getWordList();
    private String chosenWord;
    private String solution;
    private int chosenNum;
    private ArrayList<Integer> chosenNumArray;
    DateTime startTime;// = new DateTime();
    
    
    public Game(WordGame mod) throws FileNotFoundException{
        this.chosenWord = wordList.get((int) (Math.random()*wordList.size()-1));
        this.solution=mod.modify(this.chosenWord);
        this.startTime = new DateTime();
    }
    
    public Game(GameMod mod, int length, int charSize) throws FileNotFoundException{
        this.startTime = new DateTime();
        
        if (mod == GameMod.INT_ARRAY){
            this.chosenNumArray = createIntArray(length,charSize);
        }
        
        else if (mod == GameMod.INT){
            this.chosenNum = createInt(charSize,length); //when creating a general integer, charSize and length are used as lower and upper bounds
        }
    }
    
    public int getInt(){
        return this.chosenNum;
    }
    
    public static char getExtendedNumerics(int val) {
        String extendedNumerics = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        if (val > extendedNumerics.length()) {
            throw (new IndexOutOfBoundsException("Index limit: " + (extendedNumerics.length()-1) + " input value out of bounds"));
        }
        
        return (extendedNumerics.charAt(val));
    }
    
    public static String convertDecimalTo(int convertToBase, long value) {
        String conversion = "";
        
        int remainder = (int) (value % convertToBase);
        while (value > 0) {
            conversion = getExtendedNumerics(remainder) + conversion;
            value = value / convertToBase;
            remainder = (int) (value % convertToBase);
        }
        
        return conversion;
    }
    
    public ArrayList<Integer> getIntArray(){
        return this.chosenNumArray;
    }
    
    private static ArrayList<Integer> createIntArray(int length, int charSize){
        ArrayList<Integer> numbers = new ArrayList<>();
        
        for(int c=0;c<length;c++){
            numbers.add(createInt(charSize));
        }
        return numbers;
    }
    
    private static int createInt(int charSize){
        return (int) (Math.random()*charSize);
    }
    
    private static int createInt(int lowerBound,int upperBound){
        return (int) (Math.random()*upperBound)+lowerBound;
    }
    
    private int convertIntegers(){
        String converted = "";
        for (int i=0;i<this.chosenNumArray.size();i++){
            converted = converted + Integer.toString(this.chosenNumArray.get(i));
        }
        int finalInt = Integer.parseInt(converted);
        return finalInt;
    }
    
    public String convertIntToString(){
        String converted = "";
        for (int i=0;i<this.chosenNumArray.size();i++){
            converted = converted + Integer.toString(this.chosenNumArray.get(i));
        }
        return converted;
    }
    
    public int getTimeSpent(){
        Period timeElapsed = new Period(this.startTime, new DateTime());
        return timeElapsed.getSeconds();
    }
    
    public String getChosenWord(){
        return(this.chosenWord);
    }
    
    public String getSolution(){
        return(this.solution);
    }
      
    public static ArrayList<String> getColorList() throws FileNotFoundException{
        try{
            Scanner wordfile = new Scanner(new File(Env.CONFIG_LOCATION + "colorlist.txt"));
            ArrayList<String> colorls = new ArrayList<>();
            while (wordfile.hasNextLine()){
                colorls.add(wordfile.nextLine());
            }
            wordfile.close();
            return (colorls);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
