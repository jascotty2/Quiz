package com.kaimane;

import com.jascotty2.Str;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Quiz extends JavaPlugin {

    protected final static Logger logger = Logger.getLogger("Minecraft");
    public static final String name = "Quiz";
    protected static QuizConfig config = new QuizConfig();
    protected QuizQuestions questions = new QuizQuestions();
    protected QuizPlayerAnswers answers = new QuizPlayerAnswers();
    protected QuizAnswerer answerer = new QuizAnswerer(this);
    private QuizPlayerListener plListen = new QuizPlayerListener(this);

    public void onEnable() {
        PluginDescriptionFile pdfFile = this.getDescription();

        File quizfile = new File(QuizConfig.pluginFolder, "quiz.txt");
        if (!quizfile.exists()) {
            // extract an example from jar
            QuizConfig.extract(quizfile, "quiz.txt");
        }
        if (!questions.load(quizfile)) {
            Log("Failed to load quiz file");
        }
        answers.loadPoints(new File(QuizConfig.pluginFolder, "points.txt"));

        System.out.println("Quiz: " + questions.count() + " questions loaded!");

        this.getServer().getPluginManager().registerEvent(Type.PLAYER_JOIN, plListen, Priority.Normal, this);

        Log("version " + pdfFile.getVersion() + " is enabled!");
    }

    public void onDisable() {
        answers.save();
        System.out.println("Quiz is disabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command,
            String commandLabel, String[] args) {
        String comm = command.getName();
        if (comm.equals("question")) {
            /*if (sender.isOp() (and no active question)) {
            // manually start next question
            }else*/
            // get the current question
            sender.sendMessage(answerer.getCurrentQuestion());
        } else if (comm.equals("answer")) {
            if (sender instanceof Player) {
                if (args.length == 0) {
                    if (answers.hasAnswered(((Player) sender).getName())) {
                        sender.sendMessage(ChatColor.GREEN + "Your Answer: " + ChatColor.WHITE + answers.getAnswer(((Player) sender).getName()));
                    } else {
                        sender.sendMessage("You have not submitted an answer yet");
                    }
                } else {
                    if (!answers.hasAnswered(((Player) sender).getName())) {
                        answers.setAnswer(((Player) sender).getName(), Str.argStr(args));
                        sender.sendMessage("Answer entered");
                    } else {
                        answers.setAnswer(((Player) sender).getName(), Str.argStr(args));
                        sender.sendMessage("Answer changed");
                    }
                    if (config.competitiveMode) {
                        answerer.answerQuestion((Player) sender, Str.argStr(args));
                    }
                }
            } else {
                sender.sendMessage("Only a player can answer a question");
            }
        } else if (comm.equals("quizscore")) {
            sender.sendMessage("[Quiz] Top Players: -------");
            for (String top : answers.topPlayers(config.maxTopPlayers, 20, sender instanceof Player)) {
                sender.sendMessage(top);
            }
            sender.sendMessage("---------------------------");

        }
        return true;
    }

    public static void Log(String txt) {
        logger.log(Level.INFO, String.format("[%s] %s", name, txt));
    }

    public static void Log(Level loglevel, String txt) {
        Log(loglevel, txt, true);
    }

    public static void Log(Level loglevel, String txt, boolean sendReport) {
        logger.log(loglevel, String.format("[%s] %s", name, txt == null ? "" : txt));
    }

    public static void Log(Level loglevel, String txt, Exception params) {
        if (txt == null) {
            Log(loglevel, params);
        } else {
            logger.log(loglevel, String.format("[%s] %s", name, txt == null ? "" : txt), (Exception) params);
        }
    }

    public static void Log(Level loglevel, Exception err) {
        logger.log(loglevel, String.format("[%s] %s", name, err == null ? "? unknown exception ?" : err.getMessage()), err);
    }
}
