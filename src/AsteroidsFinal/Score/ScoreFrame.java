package AsteroidsFinal.Score;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class ScoreFrame extends JFrame {

    private final static DateFormat DF = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    //Simple date format
    private java.util.List<Score> scores;
    private ScoreWriter scoreManager;
    private ScoreTable theTable;
    private JPanel panelOne = new JPanel();

    //Create the score frame
    public ScoreFrame() {
        super("Top 10 Asteroid Scores!");
        setLayout(new BorderLayout());
        panelOne.setLayout(new BoxLayout(panelOne, BoxLayout.Y_AXIS));
        panelOne.setPreferredSize(new Dimension(360, 200));
        getContentPane().add(BorderLayout.CENTER, panelOne);
        scoreFrameContents();
        pack();
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void scoreFrameContents() {
        JPanel northPanel = new JPanel();
        panelOne.add(northPanel, BorderLayout.NORTH);

        //Creating the score table.
        theTable = new ScoreTable();
        //The JTable which will display the scores.
        JTable table = new JTable(theTable);

        table.setPreferredScrollableViewportSize(getPreferredSize());
        table.setFillsViewportHeight(true);

        JScrollPane p = new JScrollPane(table);
        northPanel.add(p);
        northPanel.setPreferredSize(new Dimension(p.getBounds().width, getBounds().height));
    }

    //Setter method for ScoreManager
    public void ScoreBuilder(ScoreWriter scorewriter) {
        scoreManager = scorewriter;
        scorewriter.SortScoreCollection();
        refreshTable();

    }

    //Setter methods for the scores
    public void setScoreList(java.util.List<Score> scores) {
        this.scores = scores;
    }


    //Method to update the score table. Takes a string as an argument which is either "Past 24 Hours" or "Top 10"
    public void refreshTable() {
        theTable.resetData();

        List<Score> listofscores;

        Object[][] doublearray = theTable.getEmptyCopy();

        scoreManager.SortScoreCollection();
        listofscores = scores;

        for (int x = 0; x < doublearray.length && x < listofscores.size(); x++) {
            Score s = listofscores.get(x);
            String date = DF.format(s.getDate());

            doublearray[x][0] = x + 1;
            doublearray[x][1] = s.getName();
            doublearray[x][2] = s.getScore();
            doublearray[x][3] = date;
        }

        theTable.setData(doublearray);
        theTable.fireTableDataChanged();
    }
}


