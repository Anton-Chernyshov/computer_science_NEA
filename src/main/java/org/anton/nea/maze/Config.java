package org.anton.nea.maze;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import org.anton.nea.ui.ErrorWindow;

import java.io.InputStream;
import java.util.Properties;


public class Config {
    // Yay i get to do configs bcs im putting off the actual hard part (making the maze)
    //so.. do i go the "normal" java way and do a .properties file, OR do i use yaml
    // eh i cba to figure out how to do yaml ( even though it is literally a package )
    // .properties is a java thingy, and if its good enough for minecraft its good enough for me

    /*

    Anyways, quick lesson to whoever is reading this
    this is a bit of a "weird" class, as you can see , the class constructor is private
    ooOOOOOOoOOOOO, a private constructor??? how tf does that work anton
    SO, what this means is the class is actually "created" by the class itself????
    there is a static method called getInstance, and tbh im not gonna bother explaining mroe bcs its pretty obvious what it does
    it only allows for one instance of the Config class to be created, and if antoher "instance" is created ( by doing myInst = Config.getInstance(); it returns the original object,
    so "new" objects just point to the one original instance. ez
     */
    private static final Properties prop = new Properties();
    private static Config myInstance;

    private Config(){

            try (InputStream input = Config.class.getResourceAsStream("/org/anton/nea/config.properties")){
                if (input == null) throw new RuntimeException("config.properties not found");
                prop.load(input);

            }catch (Exception e){
                Platform.runLater(() -> {
                    ErrorWindow.show(e);
                });
            }

    }

    public static Config getInstance(){
        if (myInstance == null){
            myInstance = new Config();
        }
        return myInstance;
    }


    public String getProperty(String key) {
        return prop.getProperty(key);
    }
    public Integer getInteger(String key){
        return Integer.parseInt(prop.getProperty(key));
    }
    public Color getColor(String key){
        // fuck me i hate this, for some reason it wasnt working so i need to fully sanitize it, even though .getProperty *SHOULD* do it???
        // ok nvm i was being stupid ( wrong .properties path... ) BUT im leaving the comment because it shows my "Iterative progress" so JER goes "oh my anton u have done so well" or whatever
        // hes probably gonna moan at us for a double period again
        // boys why havent you finished your NEA
            return Color.web(getProperty(key));


    }
}
