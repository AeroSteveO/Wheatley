/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Objects;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import org.pircbotx.Colors;

/**
 *
 * @author Steve-O
 * Game Object built to simplify building IRC based games
 * ->stores game specific settings and ensures only one instance of a game can be in each channel
 * ->reduces redundant code and allows for re-use of code across games
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
    private String channelName;
    private String gameType;
    private List<String> wordList = getWordList();
    private int timeLimit;
    private String chosenWord;
    private String winner = "";
    private int moneyChange = 0;
    private String modifier;
    private String solution;
    private int chosenNum;
    private String key;
    private ArrayList<Integer> chosenNumArray;
    private TimedWaitForQueue gameQueue;
    
    public Game(String channel, String mod, String type) throws FileNotFoundException{
        this.channelName = channel;
        this.modifier = mod;
        this.timeLimit = 10;
        this.gameType = type;
        this.chosenWord = wordList.get((int) (Math.random()*wordList.size()-1));
        this.solution=modify(mod,this.chosenWord);
    }
    public Game(String channel, String game, String mod, int time) throws FileNotFoundException{
        this.channelName=channel;
        this.gameType = game;
        this.modifier = mod;
        this.timeLimit = time;
        this.chosenWord = wordList.get((int) (Math.random()*wordList.size()-1));
        this.solution=modify(mod,this.chosenWord);
    }
    public Game(String channel, String game, String mod, int length, int charSize, int time) throws FileNotFoundException{
        this.channelName=channel;
        this.gameType = game;
        this.modifier = mod;
        this.timeLimit = time;
        if (mod.equalsIgnoreCase("int array")){
            this.chosenNumArray = createIntArray(length,charSize);
            //this.chosenNum = convertIntegers();
        }
        else if (mod.equalsIgnoreCase("int")){
            this.chosenNum = createInt(charSize,length); //when creating a general integer, charSize and length are used as lower and upper bounds
        }
    }
    public int getInt(){
        return this.chosenNum;
    }
    public void setTime(int t){
        this.timeLimit = t;
    }
    public ArrayList<Integer> getIntArray(){
        return this.chosenNumArray;
    }
    public String describeGame(){
        return (Colors.BOLD+"Game: "+Colors.NORMAL+this.gameType+Colors.BOLD+" Channel: "+Colors.NORMAL+this.channelName);//Colors.BOLD+" Modifier: "+Colors.NORMAL+this.modifier+
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
    public String getGameType(){
        return(this.gameType);
    }
    public String getChosenWord(){
        return(this.chosenWord);
    }
    public String getSolution(){
        return(this.solution);
    }
    public String getChannelName(){
        return(this.channelName);
    }
    
    private ArrayList<String> getWordList() throws FileNotFoundException{
        try{
            Scanner wordfile = new Scanner(new File("wordlist.txt"));
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
    
    private static String modify(String mod, String word){
        String modifiedWord ="";
        if(mod.equalsIgnoreCase("blank"))   //Change the chosenword to all underscores
            modifiedWord=makeBlank(word);
        else if(mod.equalsIgnoreCase("shuffle")) //Shuffle the characters in the chosen word
            modifiedWord=shuffle(word);
        else if(mod.equalsIgnoreCase("reverse")) //Reverse the chosenword
            modifiedWord=reverse(word);
        else if(mod.equalsIgnoreCase("none"))    //User doesn't want the string modified
            modifiedWord=word;
        return(modifiedWord);
    }
    private static String shuffle(String input){
        List<Character> characters = new ArrayList<Character>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size()!=0){
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
        List<Character> characters = new ArrayList<Character>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        for(int i=characters.size();i>0;i--){
            output.append(characters.get(i-1));
        }
        return(output.toString());
    }
    
    private ArrayList<String> getColorList() throws FileNotFoundException{
        try{
            Scanner wordfile = new Scanner(new File("colorlist.txt"));
            ArrayList<String> colorls = new ArrayList<String>();
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
    public boolean isChanEqual(String inputChannel) {
        boolean isChan = false;
        if (inputChannel.equalsIgnoreCase(this.channelName))
            isChan = true;
        return(isChan);
    }
    public boolean isGameRunning(String inputChannel, String gameName) {
        boolean isChan = false;
        if (inputChannel.equalsIgnoreCase(this.channelName))
            if (gameName.equalsIgnoreCase(this.gameType))
                isChan = true;
        return(isChan);
    }
    public static class GameArray extends Vector<Game>{
        public int getGameIdx(String channel,String game){
            int idx = -1;
            for(int i = 0; i < this.size(); i++) {
                if (this.get(i).channelName.equalsIgnoreCase(channel)&&this.get(i).gameType.equalsIgnoreCase(game)) {
                    idx = i;
                    break;
                }
            }
            return (idx);
        }
        public boolean isGameActive(String inputChannel, String GameType) {
            boolean isChan = false;
            if (!this.isEmpty()){
                for (int i=0;i<this.size();i++){
                    if(this.get(i).isGameRunning(inputChannel,GameType)){
                        isChan = true;
                    }
                }
            }
            return(isChan);
        }
        public Game getGame(String channel,String game){
            return (this.get(this.getGameIdx(channel, game)));
        }
        public ArrayList<String> getCurrentGameDescriptions(){
            ArrayList<String> gameDescriptions = new ArrayList<>();
            
            if (this.size()==0){
                gameDescriptions.add("NO CURRENT GAMES");
            }
            else{
                for (int i=0;i<this.size();i++){
                    gameDescriptions.add(this.get(i).describeGame());
                }
            }
            return gameDescriptions;
        }
        public boolean isGameActive(String currentChan,String GameType, String modification, int time) throws FileNotFoundException{
            boolean isActive=false;
            if (this.isEmpty()){
                this.add(new Game(currentChan,GameType,modification,time));
            }
            else{
                for (int i=0;i<this.size();i++){
                    if(this.get(i).isGameRunning(currentChan,GameType)){
                        isActive = true;
                    }
                }
                if (!isActive){
                    this.add(new Game(currentChan,GameType,modification,time));
                }
            }
            return(isActive);
        }
        public void closeAllGames(){
            for (int i=0;i>this.size()-1;i++){
                
            }
        }
    }
}
