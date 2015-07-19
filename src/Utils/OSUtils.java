/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

package Utils;

import java.util.Locale;

/**
 *
 * @author Stephen
 */
public class OSUtils {
    
    private static String OS = null;
    
    public static String getOSName() {
        if(OS == null) { OS = System.getProperty("os.name"); }
        return OS;
    }
    
    public static boolean isWindows() {
        return getOSName().startsWith("Windows");
    }
    
    public static String getOSArchitecture() {
        return System.getProperty("os.arch");
    }
    
    public static String getOSVersion() {
        return System.getProperty("os.version");
    }
    
    public enum OSType {
        Windows, MacOS, Linux, Other
    };
    
    // cached result of OS detection
    protected static OSType detectedOS;
    
    /**
     * detect the operating system from the os.name System property and cache
     * the result
     *
     * @returns - the operating system detected
     */
    public static OSType getOperatingSystemType() {
        if (detectedOS == null) {
            String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
            if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
                detectedOS = OSType.MacOS;
            } else if (OS.indexOf("win") >= 0) {
                detectedOS = OSType.Windows;
            } else if (OS.indexOf("nux") >= 0) {
                detectedOS = OSType.Linux;
            } else {
                detectedOS = OSType.Other;
            }
        }
        return detectedOS;
    }
}