/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package rapternet.irc.bots.wheatley.objects;

import rapternet.irc.bots.wheatley.utils.TextUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.joda.time.DateTime;
import org.joda.time.Period;

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
    private List<String> wordList = TextUtils.loadTextAsList("wordlist.txt");//getWordList();
    private String chosenWord;
    private GameMod modifier;
    private String solution;
    private int chosenNum;
    private ArrayList<Integer> chosenNumArray;
    DateTime startTime;// = new DateTime();
    
    
    public Game(GameMod mod) throws FileNotFoundException{
        this.modifier = mod;
        this.chosenWord = wordList.get((int) (Math.random()*wordList.size()-1));
        this.solution=modify(mod,this.chosenWord);
        this.startTime = new DateTime();
    }
    
    public Game(GameMod mod, int length, int charSize) throws FileNotFoundException{
        this.modifier = mod;
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
    
    private static String modify(GameMod mod, String word){
        
        String modifiedWord ="";
        
        if(mod == GameMod.BLANK)   //Change the chosenword to all underscores
            modifiedWord=makeBlank(word);
        else if(mod == GameMod.SHUFFLE) //Shuffle the characters in the chosen word
            modifiedWord=shuffle(word);
        else if(mod == GameMod.REVERSE) //Reverse the chosenword
            modifiedWord=reverse(word);
        else if(mod == GameMod.NONE)    //User doesn't want the string modified
            modifiedWord=word;
        else
            throw new UnsupportedOperationException("Modifier not supported, use 'none' to leave the word unmodified");
        
        return(modifiedWord);
    }
    
    private static String shuffle(String input){
        List<Character> characters = new ArrayList<>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(!characters.isEmpty()){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
        return(output.toString());
    }
    
    private static String makeBlank(String input){
        String blanks = new String();
        for (int i = 0; i<input.length(); i++){
            blanks = blanks + "_";
        }
        return(blanks);
    }
    
    private static String reverse(String input){
        List<Character> characters = new ArrayList<>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        for(int i=characters.size();i>0;i--){
            output.append(characters.get(i-1));
        }
        return(output.toString());
    }
    
    public static ArrayList<String> getColorList() throws FileNotFoundException{
        try{
            Scanner wordfile = new Scanner(new File("colorlist.txt"));
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
