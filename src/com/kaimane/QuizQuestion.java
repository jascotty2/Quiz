/**
 * Programmer: Jacob Scott
 * Program Name: QuizQuestion
 * Description:
 * Date: Apr 5, 2011
 */
package com.kaimane;

import com.jascotty2.CheckInput;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author jacob
 */
public class QuizQuestion {

    public static double defaultReward = 0;
    public String question = null;
    public String answer = "";
    public ArrayList<String> answers = new ArrayList<String>();
    public double reward = 0;

    public static String ignoreWords[] = new String[] {
        "a", "an", "and", "or", "the", "its", "it's", "their", "his", "her"};

    public QuizQuestion() {
    }

    public void setAnswers(String ans) {
        answers.clear();
        answers.addAll(Arrays.asList(ans.split(",")));
        answer = answers.size() > 0 ? answers.get(0) : "" ;
        if (Quiz.config.specialCharInsensitive) {
            for (int i = 0; i < answers.size(); ++i) {
                String chars = "";
                for (char c : answers.get(i).toCharArray()) {
                    if (Character.isLetterOrDigit(c) || c == ' ') {
                        chars += c;
                    }
                }
                answers.set(i, chars.trim());
            }
        }
    }
    
    public boolean isCorrectAnswer(String ans) {
        return isCorrectAnswer(ans, Quiz.config.specialCharInsensitive, 
                Quiz.config.whiteSpaceInsensitive,
                Quiz.config.andtheIgnore);
    }

    public boolean isCorrectAnswer(String ans, boolean specialCharInsensitive, 
            boolean whiteSpaceInsensitive, boolean remWords) {
        if (specialCharInsensitive) {
            String chars = "";
            for (char c : ans.toCharArray()) {
                if (Character.isLetterOrDigit(c)) {
                    chars += c;
                }
            }
            ans = chars;
        }
        for (String a : answers) {
            if (a.equalsIgnoreCase(ans)
                    || (whiteSpaceInsensitive && a.replace(" ", "").equalsIgnoreCase(ans.replace(" ", "")))
                    || (remWords && remWords(a).equalsIgnoreCase(remWords(ans)))
                    || (whiteSpaceInsensitive && remWords && remWords(a).replace(" ", "").equalsIgnoreCase(remWords(ans).replace(" ", "")))) {
                return true;
            }
        }
        return false;
    }

    String remWords(String str){
        String res = str.toLowerCase().trim();
        for(int i=0; i<ignoreWords.length; ++i){
            res = res.replace(ignoreWords[i] + " ", "");
        }
        return res;
    }

    public static QuizQuestion fromLine(String line) {
        String fields[] = line.split("\\|");
        if (fields.length == 3) {
            QuizQuestion ret = new QuizQuestion();
            ret.question = fields[0];
            ret.setAnswers(fields[1]);
            ret.reward = CheckInput.GetDouble(fields[2], defaultReward);
            return ret;
        }else if(fields.length > 3){
            QuizQuestion ret = new QuizQuestion();
            ret.question = fields[0];
            String ans = "";
            for(int i=1; i<fields.length-1; ++i){
                ans += fields[i] + ",";
            }
            ret.setAnswers(ans);
            
            ret.reward = CheckInput.GetDouble(fields[fields.length-1], defaultReward);
            return ret;
        } else {
            System.out.println(fields.length);
        }
        return null;
    }

    public String rewardStr() {
        if (Math.abs(reward - Math.floor(reward)) >= .01) {
            return String.format("%.2f", reward);
        } else {
            return String.valueOf((int) Math.floor(reward));
        }
    }
/*
    public String getAnswer() {
        if (answers.size() > 0) {
            return answers.get(0);
        } else {
            return "";
        }
    }*/
} // end class QuizQuestion

