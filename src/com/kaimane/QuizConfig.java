package com.kaimane;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.util.config.Configuration;

public class QuizConfig {

    // files used by plugin
    public final static String configname = "config.yml";
    public final static File pluginFolder = new File("plugins", Quiz.name);
    public final static File configfile = new File(pluginFolder, configname);
    // settings
    public boolean whiteSpaceInsensitive = true,
            specialCharInsensitive = true,
            andtheIgnore = true, 
            competitiveMode = false;
    // time intervals in milliseconds
    public long questionDelay = 60000,
            questionWait = 60000;
    public int minPlayersOnline = 3, maxTopPlayers = 5;

    public QuizConfig() {
        extract(configfile, configname);
        load();
    }

    public final boolean load() {

        try {
            Configuration config = new Configuration(configfile);
            config.load();

            whiteSpaceInsensitive = config.getBoolean("whiteSpaceInsensitive", whiteSpaceInsensitive);
            specialCharInsensitive = config.getBoolean("specialCharInsensitive", specialCharInsensitive);
            andtheIgnore = config.getBoolean("andtheIgnore", andtheIgnore);
            
            /*String in = config.getString("questionDelay");
            if (in != null) {
            try {
            questionDelay = CheckInput.GetBigInt_TimeSpanInSec(in, 's').longValue();
            } catch (Exception e) {
            Quiz.Log(Level.WARNING, "Invalid value for questionDelay");
            }
            }
            in = config.getString("questionWait");
            if (in != null) {
            try {
            questionWait = CheckInput.GetBigInt_TimeSpanInSec(in, 's').longValue();
            } catch (Exception e) {
            Quiz.Log(Level.WARNING, "Invalid value for questionWait");
            }
            }*/
            questionWait = config.getInt("questionWait", (int) questionWait / 1000) * 1000;
            questionDelay = config.getInt("questionDelay", (int) questionDelay / 1000) * 1000;

            competitiveMode = config.getBoolean("competitiveMode", competitiveMode);
            
            minPlayersOnline = config.getInt("minPlayersOnline", minPlayersOnline);
            maxTopPlayers = config.getInt("maxTopPlayers", maxTopPlayers);
        } catch (Exception ex) {
            Quiz.Log(Level.SEVERE, "Error parsing configuration file", ex);
            return false;
        }
        return true;
    }

    protected static void extract(File file, String filename) {
        pluginFolder.mkdirs();
        if (!file.exists()) {
            InputStream res = Quiz.class.getResourceAsStream("/" + filename);
            if (res != null) {
                Quiz.Log(filename + " not found. Creating new file.");
                try {
                    file.createNewFile();
                    FileWriter tx = new FileWriter(file);
                    try {
                        for (int i = 0; (i = res.read()) > 0;) {
                            tx.write(i);
                        }
                    } finally {
                        tx.flush();
                        tx.close();
                        res.close();
                    }
                } catch (IOException ex) {
                    Quiz.Log(Level.SEVERE, "Failed creating new config file ", ex);
                }
            }
        }
    }
}
