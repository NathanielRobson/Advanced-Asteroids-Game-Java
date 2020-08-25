package AsteroidsFinal.Score;

import java.util.Date;

public class Score {

    //Player Name
    private String name;
    private int score;
    private Date date;

    //Score constructor which allows me to store the players scores in a file
    public Score(String name, int score, Date d) {
        this.name = name;
        this.score = score;
        this.date = d;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public Date getDate() {
        return date;
    }


    @Override
    public String toString() {

        return "Name: " + name + ", Score: " + score + ", Date: " + date;
    }
}
