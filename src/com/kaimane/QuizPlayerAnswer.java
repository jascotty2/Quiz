package com.kaimane;

public class QuizPlayerAnswer {

    public String playerName;
    public String playerAnswer;
    public double points;

    public QuizPlayerAnswer(String name) {
        playerName = name;
        playerAnswer = "";
        points = 0;
    }

    public QuizPlayerAnswer(String name, String answer) {
        playerName = name;
        playerAnswer = answer;
        points = 0;
    }

    public QuizPlayerAnswer(String name, double points) {
        playerName = name;
        playerAnswer = "";
        this.points = points;
    }

    public QuizPlayerAnswer getIncPoints(double inc) {
        points += inc;
        return this;
    }

    public void addScore(double inc){
        points += inc;
    }

    public void setAnswer(String ans) {
        if (ans == null) {
            playerAnswer = "";
        } else {
            playerAnswer = ans;
        }
    }

    public String pointsStr() {
        if (Math.abs(points - Math.floor(points)) >= .01) {
            return String.format("%.2f", points);
        } else {
            return String.valueOf((int) Math.floor(points));
        }
    }

} // end class QuizPlayerAnswer

