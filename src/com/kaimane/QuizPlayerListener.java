/**
 * Programmer: Jacob Scott
 * Program Name: QuizPlayerListener
 * Description:
 * Date: Apr 5, 2011
 */
package com.kaimane;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;

/**
 * @author jacob
 */
public class QuizPlayerListener extends PlayerListener {

    Quiz plugin = null;

    public QuizPlayerListener(Quiz quizPlugin) {
        plugin = quizPlugin;
    }


    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.answerer.start();
    }
    
} // end class QuizPlayerListener

