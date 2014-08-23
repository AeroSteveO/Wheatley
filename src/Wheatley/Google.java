/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package Wheatley;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author tangd
 */
public class Google  {
    private boolean useMetricFirst = false;
    public Google(boolean useMetric) {
        useMetricFirst = useMetric;
    }
    public Document getXML(String url) {
        try {
            Document xmlFeed = Jsoup.connect(url).get();
            return xmlFeed;
        }
        catch(IOException ioEx) {
            System.err.println("Error fetching XML document: " + url);
        }
        return null;
    }
    public String getWeather(String searchString) {
        Document xmlPage = getXML("http://www.google.com/ig/api?weather="+searchString);
        Elements checkError = xmlPage.select("problem_cause");
        if(checkError.size()>0) return "No result found for: "+searchString;
        else {
            WeatherData weather = new WeatherData(xmlPage);
            String outputString = ""+weather.getCity()+";";
            outputString = outputString + String.format(" Conditions: %s; Temperature: %s (%s); Humidity: %s; High/Low: %s (%s); Wind: %s",
                    weather.getCondition(), weather.getTempString(useMetricFirst), weather.getTempString(!useMetricFirst),
                    weather.getHumidity(), weather.getHighLowString(useMetricFirst), weather.getHighLowString(!useMetricFirst),
                    weather.getWind());
            return outputString;
        }
    }
    public String getForecast(String searchString) {
        Document xmlPage = getXML("http://www.google.com/ig/api?weather="+searchString);
        Elements checkError = xmlPage.select("problem_cause");
        if(checkError.size()>0) return "No result found for: "+searchString;
        else {
            ForecastData forecast = new ForecastData(xmlPage);
            String outputString = ""+forecast.getCity()+" forecast (high/low); ";
            for(int i = 0; i < forecast.forecastLength(); i++) {
                ForecastDay day = forecast.getDay(i);
                outputString = outputString + String.format("%s: %s, %s (%s);",
                        day.getDay(), day.getCondition(), day.getHighLowString(useMetricFirst),
                        day.getHighLowString(!useMetricFirst));
            }
            return outputString;
        }
    }
    private class ForecastData {
        private ArrayList<ForecastDay> dayRecords = new ArrayList<ForecastDay>();
        private String city;
        public ForecastData(Document xmlpage) {
            Elements forecastDays = xmlpage.select("forecast_conditions");
            for(Element i : forecastDays) {
                dayRecords.add(new ForecastDay(i));
            }
            city = xmlpage.select("city").attr("data");
        }
        public ForecastDay getDay(int i) {
            if(i<dayRecords.size())
                return dayRecords.get(i);
            else
                return null;
        }
        public int forecastLength() {
            return dayRecords.size();
        }
        public String getCity() { return city; }
    }
    private class ForecastDay {
        String day, condition;
        int highF, lowF;
        public ForecastDay(Element xmlDay) {
            day = xmlDay.select("day_of_week").attr("data");
            condition = xmlDay.select("condition").attr("data");
            highF = Integer.parseInt(xmlDay.select("high").attr("data"));
            lowF = Integer.parseInt(xmlDay.select("low").attr("data"));
        }
        public String getDay() { return day; }
        public String getCondition() { return condition; }
        public int getHigh(boolean metric) {
            return metric ? Math.round((highF-32)/1.8f) : highF;
        }
        public int getLow(boolean metric) {
            return metric ? Math.round((lowF-32)/1.8f) : lowF;
        }
        public String getHighLowString(boolean metric) {
            return getHigh(metric)+"/"+getLow(metric)+"°"+(metric ? "C" :"F");
        }
    }
    private class WeatherData {
        private String city, humidity, wind, currentCondition;
        private int tempC, tempF, highF, lowF;
        public WeatherData(Document weatherXML) {
            city = weatherXML.select("city").attr("data");
            tempC = Integer.parseInt(weatherXML.select("current_conditions temp_c").attr("data"));
            tempF = Integer.parseInt(weatherXML.select("current_conditions temp_f").attr("data"));
            Element currentDay = weatherXML.select("forecast_conditions").first();
            highF = Integer.parseInt(currentDay.select("high").attr("data"));
            lowF = Integer.parseInt(currentDay.select("low").attr("data"));
            humidity = weatherXML.select("current_conditions humidity").attr("data").split("\\s+")[1];
            wind = weatherXML.select("current_conditions wind_condition").attr("data").substring(6);
            currentCondition = weatherXML.select("current_conditions condition").attr("data");
        }
        public int getTemp(boolean metric) {
            return metric ? tempC : tempF;
        }
        public String getTempString(boolean metric) {
            return metric ? tempC + "°C" : tempF + "°F";
        }
        public int getHigh(boolean metric) {
            return metric ? Math.round((highF-32)/1.8f) : highF;
        }
        public int getLow(boolean metric) {
            return metric ? Math.round((lowF-32)/1.8f) : lowF;
        }
        public String getHighLowString(boolean metric) {
            return getHigh(metric)+"/"+getLow(metric)+"°"+(metric ? "C" :"F");
        }
        public String getHumidity() {
            return humidity;
        }
        public String getWind() {
            return wind;
        }
        public String getCondition() {
            return currentCondition;
        }
        public String getCity() {
            return city;
        }
    }
}
