package org.anton.nea.helpers;

import javafx.scene.Scene;
import org.anton.nea.ui.ErrorWindow;

import java.util.Objects;
import java.io.*;

public class OnStartup {
    public static final int WINDOWS = 0;
    public static final int MACOS = 1;
    public static final int LINUX = 2;
    public static final int AIX = 3;
    public static final int UNKNOWN = -1;
    /**
     * -1 - undefined+
     * 0 - windows
     * 1 - macos
     * 2 - linux
     * 3 - other
     * @return returns the "os type"
     */
    public static int getOS(){
        String os = System.getProperty("os.name").toLowerCase();
        if(os.contains("win")){return WINDOWS;}
        else if (os.contains("mac")){return MACOS;}
        else if (os.contains("nix") || os.contains("nux")){return LINUX;}
        else if (os.contains("aix")){return AIX;}
        return UNKNOWN;
    }
    public static boolean isDarkTheme() {
        int os = getOS();

        if (os == WINDOWS) {
            try {
                ProcessBuilder pb = new ProcessBuilder(
                        "reg", "query",
                        "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize",
                        "/v", "AppsUseLightTheme"
                );
                pb.redirectErrorStream(true);
                Process process = pb.start();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.contains("0x0")) {
                            return true; // dark mode
                        }
                    }
                }
            } catch (Exception e) {
                ErrorWindow.show(e);
            }
            return false; // default light
        }

        else if (os == MACOS) {
            try {
                ProcessBuilder pb = new ProcessBuilder("defaults", "read", "-g", "AppleInterfaceStyle");
                pb.redirectErrorStream(true);
                Process process = pb.start();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line = reader.readLine();
                    return line != null && line.equalsIgnoreCase("Dark");
                }
            } catch (Exception e) {
                ErrorWindow.show(e);
            }
            return false;
        }

        else if (os == LINUX) {
            try {
                ProcessBuilder pb = new ProcessBuilder("gsettings", "get", "org.gnome.desktop.interface", "color-scheme");
                pb.redirectErrorStream(true);
                Process process = pb.start();

                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line = reader.readLine();
                    return line != null && line.contains("dark");
                }
            } catch (Exception e) {
                ErrorWindow.show(e);
            }
            return false;
        }

        // AIX / unknown == default light
        return false;
    }

    public static void SetCssTheme(Scene scene){
        try{
            String darkModeCSS = Objects.requireNonNull(OnStartup.class.getResource("/org/anton/nea/dark.css")).toExternalForm();
            String lightModeCSS = Objects.requireNonNull(OnStartup.class.getResource("/org/anton/nea/light.css")).toExternalForm();

            // get System default theme
            if (isDarkTheme()){
                scene.getStylesheets().add(darkModeCSS);
            }
            else{
                scene.getStylesheets().add(lightModeCSS);
            }
        } catch (Exception e){
            ErrorWindow.show(e);
        }

    }
    public static void SetDefaultParametersFromConfigFile(){}
    public static void RunOnStartup(Scene scene){
        SetCssTheme(scene);


    }
}
