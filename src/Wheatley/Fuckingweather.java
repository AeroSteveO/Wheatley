package Wheatley;

import Objects.HTTPGET;
import Objects.MultiRegexExtractor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class Fuckingweather
extends ListenerAdapter {
    
    private static final Pattern makeSure = Pattern.compile("<p class=\"large\">I CAN&#39;T FIND THAT SHIT</p>", 2);
    private static final Pattern[] details = {
        Pattern.compile("id=\"locationdisplayspan\" class=\"small\">(.*?)</span>", 2),
        Pattern.compile("tempf=\"(.*?)\"", 2),
        Pattern.compile("class=\"remark\">(.*?)<", 2),
        Pattern.compile("class=\"flavor\">(.*?)<", 34)};
    
    @Override
    public void onMessage(MessageEvent event) throws IOException, Exception {
        String message = Colors.removeFormattingAndColors(event.getMessage());
        if (message.equalsIgnoreCase("!fuckingweather")) {
            System.out.println("stuff");
            String zip = "";
            event.getBot().sendIRC().message(event.getChannel().getName(),process(zip));
        }
        else if (message.matches("!fuckingweather\\s[0-9]{5}")){
            System.out.println("stuffffffffffffff");
            String zip = message.split(" ")[1];
            event.getBot().sendIRC().message(event.getChannel().getName(),process(zip));
        }
    }
    
    public String process(String zip) throws IOException, Exception {
        try {
            zip = URLEncoder.encode(zip, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (zip.equals("")) {
            zip = "47906";
        }
        String searchURL = "http://www.thefuckingweather.com/?zipcode=" + zip;
        System.out.println(searchURL);
        
        HTTPGET h = new HTTPGET();
        String page = h.getPage(searchURL);
        Matcher m = makeSure.matcher(page);
        
        if (!m.find()) {
            ArrayList fields = MultiRegexExtractor.extractRegex(page, details);
            String place = ((String) fields.get(0)).trim();
            String weather = ((String) fields.get(1)).trim() + "F";
            String desc = ((String) fields.get(2)).replaceAll("\\<.*?\\>", " ").trim();
            String remark = ((String) fields.get(3)).trim();
            String result = "!fuckingweather " + place + " - " + weather + " " + desc + " (" + remark + ") - " + searchURL;
            return result;
        }
        return "!fuckingweather WRONG FUCKING ZIP";
    }
    public String getHTML(String url) throws IOException{
        URL oracle = new URL(url);
        BufferedReader in = new BufferedReader(
        new InputStreamReader(oracle.openStream()));

        String inputLine = null;
        while (in.readLine()!= null)
            inputLine = in.readLine();
//            System.out.println(inputLine);
        in.close();
        
        return(inputLine);//.replace("[\t\r\n]", ""));

    }
    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            return buffer.toString();
        }catch (Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getMessage());
        } finally {
            if (reader != null)
                reader.close();
        }
        return "SOMETHING FUCKING BROKE";
    }
    
    public String getPage(String url) {
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(url);
        String result = "";
        try {
            client.executeMethod(method);
            result = method.getResponseBodyAsString();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            method.releaseConnection();
        }
        return result;
    }
}