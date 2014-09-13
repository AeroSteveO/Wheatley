/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 * Used to update .txt files
 * 
 * Activate commands with
 *      !update [filename] [singleWordItem]
 *          !update badwords defenetely
 */
public class UpdateFiles extends ListenerAdapter{
    
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        if (message.toLowerCase().startsWith("!update ")&&event.getUser().getNick().equals(Global.botOwner)){
            String[] properties = message.split(" ");
            if (properties.length== 3){
                String filename = properties[1];
                String addition = properties[2];
                
                try{
                    File file =new File(filename+".txt");
                    
                    //if file doesnt exists, then create it
                    if(!file.exists()){
                        file.createNewFile();
                    }
                    
                    //true = append file
                    FileWriter fileWritter = new FileWriter(file.getName(),true);
                    BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                    bufferWritter.write("\n"+addition);
                    bufferWritter.close();
                    
                    event.getBot().sendIRC().message(event.getChannel().getName(),"Success: "+addition+" was added to "+ filename);
                    
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
