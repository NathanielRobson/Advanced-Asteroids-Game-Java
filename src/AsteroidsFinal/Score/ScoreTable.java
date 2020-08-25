package AsteroidsFinal.Score;

import javax.swing.table.AbstractTableModel;

public class ScoreTable extends AbstractTableModel {

    //String array filled with the column names of the model.
    private final static String[] COLUMN_NAMES = {"Pos", "Name", "Score", "Date"};

    //number of scores to display in the table
    private final static int ROWS = 10;
    //Double array to store scores inside for the table
    private Object[][] data;

    //Constructor
    public ScoreTable() {
        resetData();
    }

    void resetData() {
        data = new Object[ROWS][COLUMN_NAMES.length];
    }

    public void setData(Object[][] data) {
        this.data = data;
    }

    public Object[][] getEmptyCopy() {
        return new Object[data.length][data[0].length];
    }

    @Override
    public int getRowCount() {
        return ROWS;
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int col) {
        return COLUMN_NAMES[col];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }
}
