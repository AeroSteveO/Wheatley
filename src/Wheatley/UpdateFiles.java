/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Wheatley;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

/**
 *
 * @author Steve-O
 * Used to update .txt files
 *
 * Requirements:
 * - APIs
 *    N/A
 * - Custom Objects
 *    N/A
 * - Linked Classes
 *    Global
 * 
 * Activate commands with:
 *      !update [filename] [singleWordItem]
 *          !update badwords defenetely
 */
public class UpdateFiles extends ListenerAdapter{
    
    
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        
        String message = Colors.removeFormattingAndColors(event.getMessage());
        
        if (message.startsWith(Global.commandPrefix)){
            
            String command = message.split(Global.commandPrefix)[1];
            String[] cmdSplit = command.split(" ");
            
            if (cmdSplit[0].equalsIgnoreCase("update")){
                if(event.getUser().getNick().equals(Global.botOwner)&&event.getUser().isVerified()){
                    
//                    String[] properties = message.split(" ");
                    if (cmdSplit.length== 3){
                        String filename = cmdSplit[1];
                        String addition = cmdSplit[2];
                        
                        try{
                            
                            File file;
                            
                            if (filename.split("\\.").length==1){
                                file =new File(filename+".txt");
                                filename = filename+".txt";
                            }
                            
                            else{
                                file =new File(filename);
                            }
                            
//                            if file doesnt exists, then create it
                            if(!file.exists()){
                                file.createNewFile();
                            }
                            
                            //true = append file
                            FileWriter fileWritter = new FileWriter(file.getName(),true);
                            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
                            bufferWritter.write("\n"+addition);
                            bufferWritter.close();
                            
                            event.getBot().sendIRC().message(event.getChannel().getName(),"Success: "+addition+" was added to "+ filename);
                            
                        }catch(Exception e){
                            e.printStackTrace();
                            event.getBot().sendIRC().notice(event.getUser().getNick(),"FAILURE: "+addition+" was NOT added to "+ filename);
                        }
                    }
                    else{
                        event.getBot().sendIRC().notice(event.getUser().getNick(), "Update reqires 2 inputs: the filename and the single word addition");
                    }
                }
                else{
                    event.getBot().sendIRC().notice(event.getUser().getNick(), "You do not have access to this command");
                }
            }
        }
    }
}
