/*
 * ColumnsBean.java
 *
 * Created on November 15, 2006, 9:35 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.icesoft.icefaces.tutorial.component.table.columns;

/**
 *
 * @author jzhang
 */

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import java.util.ArrayList;

public class ColumnsBean {

    // row columna data map
    private DataModel columnDataModel;
    private DataModel rowDataModel;

    // ascii index
    public static final int ASCII_START = 33;
    public static final int ASCII_END = 126;
    public static final int ASCII_RANGE = ASCII_END - ASCII_START;
    private static final AsciiData[] asciiData = new AsciiData[ASCII_RANGE];

    // we only need to initialized the ascii array once.
    private static boolean isInit;

    // default column and row values 
    private int columns = 5;
    private int rows = 0;  // ASCII_RANGE / 5 columsn.

    public ColumnsBean() {

        // calulate rows
        calculateRows();

        // generate some default data.
        init();
        updateTableColumns(null);
    }

    /**
     * Initialize an array of ASCII values which we will display differently
     * depending on the number of columns specified by the user.
     */
    private synchronized void init() {
        if (isInit) {
            return;
        }
        isInit = true;

        // build the asic data set
        AsciiData tmp;
        int index;
        for (int i = 0; i < ASCII_RANGE ; i++) {
            tmp = new AsciiData();
            index = ASCII_START + i;
            tmp.setIndex(index);
            tmp.setIndexChar((char) index);
            tmp.setIndexHex(Integer.toHexString(index));
            asciiData[i] = tmp;
        }
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public DataModel getRowDataModel() {
        return rowDataModel;
    }

    public DataModel getColumnDataModel() {
        return columnDataModel;
    }

    /**
     * Called by the table interator.  This method reads the column and row data
     * models and displays the correct cell value.
     *
     * @return data which should be displayed for the given model state.
     */
    public String getCellValue() {
        if (rowDataModel.isRowAvailable() &&
            columnDataModel.isRowAvailable()) {

            // get the index of the row and column that this method is being
            // called for
            int row = rowDataModel.getRowIndex();
            int col = columnDataModel.getRowIndex();

            // calculate the offset in the asciiData
            int offset = (col * rows) + row;
            return getChar(offset % 26);
        }
        // empty field.
        return "-";
    }


    private void  calculateRows(){
        // calculate the number of columns.
        rows = ASCII_END / columns;

        // make an extra row if there is a modulus
        if ((ASCII_END % columns) != 0) {
            rows += 1;

        }
    }

    /**
     * Updates the table model data.
     *
     * @param event property change event which specifies whether or not the the
     *              column count has changed.
     */
    public void updateTableColumns(ValueChangeEvent event) {
       if (event != null && event.getNewValue() != null &&
            event.getNewValue() instanceof Integer) {
            // get the new column count
            columns = ((Integer) event.getNewValue()).intValue();
        }
//        int numberOfRows = columns * 20;
        int numberOfRows = columns * 20;
        ArrayList columnList = new ArrayList();
        ArrayList rowList = new ArrayList();
        for(int i = 0; i < columns; i++){
            columnList.add(getChar(i));
        }
        int ci = 0;
        for(int i = 0; i < numberOfRows; i++){
            rowList.add(getChar(ci));
            ci++;
            if(ci > 26){
                ci = 0;
            }
        }
        rowDataModel = new ListDataModel(rowList);        
        columnDataModel = new ListDataModel(columnList);
    }

    private String getChar(int i){
        StringBuffer sb = new StringBuffer();
        if(i > 25){
            while(i > 25){
                int ii = i / 25;
                int ir = ii * 25;
                i = i - ir;
                sb.append(_getChar(ii));
            }
        }
        sb.append(_getChar(i));
        return sb.toString();
    }

    private String _getChar(int i){
        i += 65;
        String r = "" + (char)i;
        return r;
    }

    /**
     * Utility class to store index, char and hex data.  Used to store
     * table cell data for this example. 
     */
    public class AsciiData {
        private int index;
        private char indexChar;
        private String indexHex = " ";


        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public char getIndexChar() {
            return indexChar;
        }

        public char getIndexCharString() {
            // only show ASCII values after char 32 (space).
            if (indexChar > 32)
                return indexChar;
            else {
                return ' ';
            }
        }

        public void setIndexChar(char indexChar) {
            this.indexChar = indexChar;
        }

        public String getIndexHex() {
            return indexHex;
        }

        public void setIndexHex(String indexHex) {
            this.indexHex = indexHex;
        }

        public String toString() {
            return index + "  " + indexChar + "  " + indexHex;
        }
    }

}

