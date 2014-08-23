/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Wheatley;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author tangd
 */
public abstract class ForecastLog {
    private long expiration;    //Denotes when the log should be discarded
    private int highC, highF;
    private int lowC, lowF;
    private String day;
    private String condition;
    private int rainChance;
    private String formattedString = null;
    public ForecastLog(long ttl, int highC, int highF, int lowC, int lowF, String day, String condition, int rainChance) {
        this.expiration = expiration + (new Date()).getTime()+ttl;
        this.highC = highC;
        this.highF = highF;
        this.lowC = lowC;
        this.lowF = lowF;
        this.day = day;
        this.condition = condition;
        this.rainChance = rainChance;
    }

    public String getCondition() {
        return condition;
    }

    public String getDay() {
        return day;
    }

    public long getExpiration() {
        return expiration;
    }

    public int getHighC() {
        return highC;
    }

    public int getHighF() {
        return highF;
    }

    public int getLowC() {
        return lowC;
    }

    public int getLowF() {
        return lowF;
    }

    public int getRainChance() {
        return rainChance;
    }
    public String getFormattedForecast() {
        return formattedString;
    }
    public abstract void setFormattedString();
}
