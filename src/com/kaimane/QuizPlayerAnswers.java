/**
 * Programmer: Jacob Scott
 * Program Name: QuizPlayerAnswers
 * Description: class for storing & handling all of the answers & scores of players
 * Date: Apr 5, 2011
 */
package com.kaimane;

import com.jascotty2.CheckInput;
import com.jascotty2.JMinecraftFontWidthCalculator;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.logging.Level;

public class QuizPlayerAnswers {

    protected /*static*/ HashMap<String, QuizPlayerAnswer> playerAnswers = new HashMap<String, QuizPlayerAnswer>();
    protected File playerPointsFile = null;

    public static class ScoreComparator implements Comparator<QuizPlayerAnswer> {

        boolean descending = true;

        public ScoreComparator() {
        }

        public int compare(QuizPlayerAnswer o1, QuizPlayerAnswer o2) {
            return (int) ((o2.points - o1.points) * (descending ? 1 : -1));
        }
    }

    public /*static*/ void loadPoints(File pointsFile) {
        playerAnswers.clear();
        playerPointsFile = pointsFile;
        if (pointsFile.exists()) {
            BufferedReader reader = null;
            FileReader fReader = null;
            try {
                fReader = new FileReader(pointsFile.getAbsolutePath());
                reader = new BufferedReader(fReader);
                String line = null;
                int linenum = 0;
                while ((line = reader.readLine()) != null) {
                    ++linenum;

                    if (line.startsWith("//") || line.startsWith("#") || line.equals("")) {
                        continue;
                    }
                    String[] currentLine = line.split("\\|");
                    if (currentLine.length == 2 && CheckInput.IsDouble(currentLine[1])) {
                        if (playerAnswers.containsKey(currentLine[0].toLowerCase())) {
                            playerAnswers.put(currentLine[0],
                                    playerAnswers.get(currentLine[0]).getIncPoints(
                                    CheckInput.GetDouble(currentLine[1], 0)));
                        } else {
                            playerAnswers.put(currentLine[0], new QuizPlayerAnswer(currentLine[0], CheckInput.GetDouble(currentLine[1], 0)));
                        }
                    } else {
                        Quiz.Log(Level.WARNING, "Quiz: points.txt INVALID LINE FORMAT at line " + linenum);
                    }
                }
            } catch (IOException exception) {
                Quiz.Log(Level.SEVERE, "Error loading points file", exception);
            }
        }
    }

    public void save() {
        if (playerPointsFile != null) {
            FileWriter fstream = null;
            BufferedWriter out = null;
            try {
                fstream = new FileWriter(playerPointsFile.getAbsolutePath());
                out = new BufferedWriter(fstream);

                for (String p : playerAnswers.keySet()) {
                    out.write(p + "|" + playerAnswers.get(p).points);
                    out.newLine();
                }

                //Close the output stream
                out.flush();

            } catch (Exception e) {
                Quiz.Log(Level.SEVERE, "Failed to save " + playerPointsFile.getName(), e);
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                    if (fstream != null) {
                        fstream.close();
                    }
                } catch (IOException ex) {
                }
            }
        }
    }

    public void setAnswer(String name, String ans) {
        if (playerAnswers.containsKey(name.toLowerCase())) {
            playerAnswers.get(name.toLowerCase()).setAnswer(ans);
        } else {
            playerAnswers.put(name.toLowerCase(), new QuizPlayerAnswer(name, ans));
        }
    }

    public void correctAnswer(String name, double reward) {
        if (playerAnswers.containsKey(name.toLowerCase())) {
            playerAnswers.get(name.toLowerCase()).addScore(reward);
        }
    }
    
    public String getAnswer(String name) {
        if (playerAnswers.containsKey(name.toLowerCase())) {
            return playerAnswers.get(name.toLowerCase()).playerAnswer;
        } else {
            return "";
        }
    }

    public boolean hasAnswered(String name) {
        return playerAnswers.containsKey(name.toLowerCase())
                && playerAnswers.get(name.toLowerCase()).playerAnswer.length() > 0;
    }

    public void clearAnswers() {
        for (String u : playerAnswers.keySet()) {
            playerAnswers.get(u).setAnswer(null);
        }
    }

    public ArrayList<QuizPlayerAnswer> toArrayList() {
        ArrayList<QuizPlayerAnswer> ret = new ArrayList<QuizPlayerAnswer>();
        for (QuizPlayerAnswer a : playerAnswers.values()) {
            ret.add(a);
        }
        return ret;
    }

    public ArrayList<QuizPlayerAnswer> sortedArrayList() {
        ArrayList<QuizPlayerAnswer> arr = toArrayList();
        java.util.Collections.sort(arr, new ScoreComparator());
        return arr;
    }

    public ArrayList<String> topPlayers(int max) {
        ArrayList<QuizPlayerAnswer> ans = sortedArrayList();
        ArrayList<String> top = new ArrayList<String>();
        for (int i = 0; i < max && i < ans.size(); ++i) {
            top.add(ans.get(i).playerName + ":  " + ans.get(i).pointsStr());
        }
        return top;
    }

    public ArrayList<String> topPlayers(int max, int width, boolean isPlayer) {
        ArrayList<QuizPlayerAnswer> ans = sortedArrayList();
        ArrayList<String> top = new ArrayList<String>();
        for (int i = 0; i < max && i < ans.size(); ++i) {
            if (isPlayer) {
                top.add(JMinecraftFontWidthCalculator.strPadRight(ans.get(i).playerName + ":  ", width, ' ') + ans.get(i).pointsStr());
            } else {
                top.add(JMinecraftFontWidthCalculator.unformattedPadRight(ans.get(i).playerName + ":  ", width, ' ') + ans.get(i).pointsStr());
            }
        }
        return top;
    }
}
