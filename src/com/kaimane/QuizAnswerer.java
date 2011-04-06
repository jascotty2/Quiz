/**
 * Programmer: Jacob Scott
 * Program Name: QuizAnswerer
 * Description: runs quiz events
 * Date: Apr 5, 2011
 */
package com.kaimane;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author jacob
 */
public class QuizAnswerer {

    Quiz plugin = null;
    AskWait askWaiter = null;
    AnswerWait ansWaiter = null;
    QuizQuestion currentQuestion = null;

    public QuizAnswerer(Quiz quizplugin) {
        plugin = quizplugin;
    }

    public void start() {
        if (askWaiter == null && plugin.getServer().getOnlinePlayers().length >= Quiz.config.minPlayersOnline) {
            askWaiter = new AskWait();
            askWaiter.start(Quiz.config.questionDelay);
        }
    }

    public void startAsker() {
        if (plugin.getServer().getOnlinePlayers().length >= Quiz.config.minPlayersOnline
                && plugin.questions.count() > 0) {
            if (ansWaiter != null) {
                ansWaiter.cancel();
            }
            currentQuestion = plugin.questions.randomQuestion();
            plugin.answers.clearAnswers();
            plugin.getServer().broadcastMessage(
                    ChatColor.GREEN + "[Quiz] " + ChatColor.WHITE + currentQuestion.question);
            ansWaiter = new AnswerWait();
            ansWaiter.start(Quiz.config.questionWait);
        }
    }

    public String getCurrentQuestion() {
        if (currentQuestion == null || ansWaiter == null) {
            return "(No Active Question)";
        } else {
            return ChatColor.GREEN + "[Quiz] " + ChatColor.WHITE + currentQuestion.question;
        }
    }

    public void answerQuestion(String answer) {
        if (currentQuestion.isCorrectAnswer(answer)) {
            answerQuestion();
        }
    }

    public void answerQuestion(Player p, String answer) {
        if (currentQuestion.isCorrectAnswer(answer)) {
            if (p != null) {
                p.sendMessage(ChatColor.GREEN + "[Quiz] " + ChatColor.GOLD + "Your Answer was Correct!  +" + currentQuestion.rewardStr());
            }
            plugin.answers.correctAnswer(p.getName(), currentQuestion.reward);

            plugin.getServer().broadcastMessage(ChatColor.GREEN + "[Quiz] " + ChatColor.AQUA + p.getDisplayName() + " Correctly Answered the Question ");
            plugin.getServer().broadcastMessage(ChatColor.GREEN + "[Quiz] " + ChatColor.AQUA + "Answer: "
                    + ChatColor.WHITE + currentQuestion.answer);

            if (plugin.getServer().getOnlinePlayers().length >= Quiz.config.minPlayersOnline) {
                start();
            }

        }
        //else if (p != null) p.sendMessage(ChatColor.GREEN + "[Quiz] " + ChatColor.RED + "Incorrect Answer");

    }

    public void answerQuestion() {
        if (askWaiter != null) {
            askWaiter.cancel();
            askWaiter = null;
        }
        if (ansWaiter != null) {
            ansWaiter.cancel();
            ansWaiter = null;
        }
        if (currentQuestion != null) {
            ArrayList<QuizPlayerAnswer> answers = plugin.answers.toArrayList();
            int numCorrect = 0;
            for (QuizPlayerAnswer a : answers) {
                if (a.playerAnswer.length() > 0) {
                    Player p = plugin.getServer().getPlayer(a.playerName);
                    if (currentQuestion.isCorrectAnswer(a.playerAnswer)) {
                        if (p != null) {
                            p.sendMessage(ChatColor.GREEN + "[Quiz] " + ChatColor.GOLD + "Your Answer was Correct!  +" + currentQuestion.rewardStr());
                        }
                        plugin.answers.correctAnswer(a.playerName, currentQuestion.reward);
                        ++numCorrect;
                    }
                    //else if (p != null) p.sendMessage(ChatColor.GREEN + "[Quiz] " + ChatColor.AQUA + "Your Answer was Not Correct");
                }
            }
            plugin.getServer().broadcastMessage(ChatColor.GREEN + "[Quiz] " + ChatColor.AQUA + "Time's Up! (" + numCorrect + " Correct)  ");
            plugin.getServer().broadcastMessage(ChatColor.GREEN + "[Quiz] " + ChatColor.AQUA + "Answer: "
                    + ChatColor.WHITE + currentQuestion.answer);
            if (plugin.getServer().getOnlinePlayers().length >= Quiz.config.minPlayersOnline) {
                start();
            }
        }
    }

    protected class AskWait extends TimerTask {

        public void start(long time) {
            (new Timer()).schedule(this, time);
        }

        @Override
        public void run() {
            startAsker();
        }
    }

    protected class AnswerWait extends TimerTask {

        public void start(long time) {
            (new Timer()).schedule(this, time);
        }

        @Override
        public void run() {
            answerQuestion();
        }
    }
} // end class QuizAnswerer

