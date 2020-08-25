package AsteroidsFinal.Score;

import utilities.JEasyFrame;

import javax.swing.*;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ScoreWriter {

    private static final DateFormat DF = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    //The file path of the txt file
    private static final String FILE_NAME = "scores.txt";

    private ScoreFrame scoreFrame;

    //List of all Scores.
    private List<Score> scores = new ArrayList<>();

    private JEasyFrame gameframe;

    //Constructor
    public ScoreWriter(JEasyFrame gameframe, ScoreFrame scoreFrame) {
        this.gameframe = gameframe;
        this.scoreFrame = scoreFrame;
        createScoreList();
    }


    //Adds the current score of the player to the txt file. Returns a score object.
    public Score addScoreToFile(Score playerScore) {
        String name = playerScore.getName();
        String score = String.valueOf(playerScore.getScore());

        Date d = new Date();
        String date = DF.format(d);
        String newLine = String.join(",", name, score, date);

        try {
            //Try and open the file and write the players score to it.
            FileWriter fw = new FileWriter(FILE_NAME, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println(newLine);
            pw.close();

        } catch (IOException e) {
            System.out.println("Unable to load scores file.");
        }

        return LineToList(newLine);
    }

    //Adding all scores of the scores in the text file to the scores list
    public void createScoreList() {
        Scanner scan;
        try {
            scan = new Scanner(new File(FILE_NAME));

            //iterate through each line in the file
            while (scan.hasNextLine()) {
                scores.add(LineToList(scan.nextLine()));
            }
            //If the file is not found then inform the player of this message
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(gameframe, "Error: File not found. Play the game once, and a score file will be created.");
        }
        scoreFrame.setScoreList(scores);
    }

    //Method to use each line in the file and add it to the specified lists
    private Score LineToList(String line) {
        List<String> lineSplit = Arrays.asList(line.split(","));
        String name = lineSplit.get(0);
        int score;
        Date d;

        //Attempt to parse the integer found in the file to scores
        try {
            score = Integer.parseInt(lineSplit.get(1));
        } catch (NumberFormatException e) {
            System.out.println("Unable to parse int to string");
            score = 0;
        }
        //Attempt to parse the date found in the file
        try {
            d = DF.parse(lineSplit.get(2));
        } catch (ParseException e) {
            d = new Date();
            System.out.println("Error parsing date to string");
        }
        return new Score(name, score, d);
    }

    //sort the list by score and display them
    public void displayScores() {
        SortScoreCollection();
        scoreFrame.setScoreList(scores);
        scoreFrame.refreshTable();
    }

    //sort the list by score
    public void SortScoreCollection() {
        Collections.sort(scores, (o1, o2) -> {
            if (o1.getScore() > o2.getScore()) {
                return -1;
            } else if (o1.getScore() < o2.getScore()) {
                return 1;
            }
            return 0;
        });
    }
}