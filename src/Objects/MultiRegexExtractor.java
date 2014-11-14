package Objects;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MultiRegexExtractor {
    
    public static ArrayList<String> extractRegex(String content, Pattern[] regExArray) {
        ArrayList results = new ArrayList();
        
        int searchIndex = 0;
        if (regExArray.length == 1) {
            Pattern detailPattern = regExArray[0];
            while (searchIndex > -1) {
                Matcher detailMatcher = detailPattern.matcher(content.substring(searchIndex));
                if (detailMatcher.find()) {
                    results.add(detailMatcher.group(1));
                    int howFarIn = detailMatcher.end(1);
                    searchIndex += howFarIn;
                } else {
                    searchIndex = -1;
                }
            }
        } else {
            for (int i = 0; i < regExArray.length; i++) {
                Pattern detailPattern = regExArray[i];
                Matcher detailMatcher = detailPattern.matcher(content.substring(searchIndex));
                if (detailMatcher.find()) {
                    results.add(detailMatcher.group(1));
                    searchIndex += detailMatcher.end();
                } else {
                    results.add("");
                }
            }
        }
        return results;
    }
}