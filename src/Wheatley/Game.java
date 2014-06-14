/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.WaitForQueue;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 */
public class Game {
    String channelName;
    String gameType;
    List<String> wordList = getWordList();
    int timeLimit;
    String chosenWord;
    String winner;
    int moneyChange = 0;
    boolean active = true;
    String modifier;
    String solution;
    
    
    Timer timer;
    
    Game(String channel, String mod, String type) throws FileNotFoundException{
        this.channelName = channel;
        this.modifier = mod;
        this.timeLimit = 10;
        this.gameType = type;
        this.chosenWord = wordList.get((int) (Math.random()*wordList.size()-1));
        this.solution=modify(mod,this.chosenWord);
    }
    Game(String channel, String game, String mod, int time) throws FileNotFoundException{
        this.channelName=channel;
        this.gameType = game;
        this.modifier = mod;
        this.timeLimit = time;
        this.chosenWord = wordList.get((int) (Math.random()*wordList.size()-1));
        this.solution=modify(mod,this.chosenWord);
    }
    public class TimedWaitForQueue extends WaitForQueue{
        int time;
        private QueueTime runnable = null;
        Thread t;
        public TimedWaitForQueue(PircBotX bot,int time, Channel chan,User user, int key) throws InterruptedException {
            super(bot);
            this.time=time;
            QueueTime runnable = new QueueTime(Global.bot,time,chan,user,key);
            this.t = new Thread(runnable);
            runnable.giveT(t);
            t.start();
//            Thread.sleep(this.time*1000);
//            bot.getConfiguration().getListenerManager().dispatchEvent(new MessageEvent(Global.bot,chan,user,Integer.toString(key)));
        }
        public void end() throws InterruptedException{
            this.close();
            t.join(1000);
        }
    }
    public class QueueTime implements Runnable {
        int time;
        User user;
        Channel chan;
        int key;
        PircBotX bot;
        Thread t;
        QueueTime(PircBotX bot, int time, Channel chan, User user, int key) {
            this.time = time;
            this.chan=chan;
            this.user=user;
            this.key=key;
            this.bot=bot;
        }
        
        public void giveT(Thread t) {
            this.t = t;
        }
        
        @Override
        public void run() {
            try {
                Thread.sleep(time*1000);
                bot.getConfiguration().getListenerManager().dispatchEvent(new MessageEvent(Global.bot,chan,user,Integer.toString(key)));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
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
        if(mod.equalsIgnoreCase("blank"))
            modifiedWord=makeBlank(word);
        if(mod.equalsIgnoreCase("shuffle"))
            modifiedWord=shuffle(word);
        if(mod.equalsIgnoreCase("reverse"))
            modifiedWord=reverse(word);
        
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
            //int randPicker = (int)(Math.random()*characters.size());
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
    
    public static class ChannelArray extends Vector<Game>{
        public int getGameIdx(String toCheck){
            int idx = -1;
            for(int i = 0; i < this.size(); i++) {
                if (this.get(i).channelName.equalsIgnoreCase(toCheck)) {
                    idx = i;
                    break;
                }
            }
            return (idx);
        }
    }
}
