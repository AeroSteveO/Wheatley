package Objects;

import java.io.IOException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

public class HTTPGET {
    
    public String getPage(String url) {
        System.out.println("START OF GETPAGE");
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(url);
        String result = "";
        System.out.println("BEFORE TRYING");
        try {
            client.executeMethod(method);
            System.out.println("TRYING");
            result = method.getResponseBodyAsString();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("FINALLY");
            method.releaseConnection();
        }
        return result;
    }
}