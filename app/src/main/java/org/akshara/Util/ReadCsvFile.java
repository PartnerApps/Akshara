package org.akshara.Util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadCsvFile {
    private static boolean D = Util.DEBUG;
    private static String TAG = ReadCsvFile.class.getSimpleName();

    public static ArrayList readCSVStudent(String fileName) {
        ArrayList rowList = new ArrayList();

        //Input file which needs to be parsed
        String fileToParse = Util.getFilePath(fileName);
        BufferedReader fileReader = null;

        //Delimiter used in CSV file
        final String DELIMITER = ",";
        try {

            String line = "";
            //Create the file reader
            fileReader = new BufferedReader(new FileReader(fileToParse));
            int lineNo = 0;

            //Read the file line by line
            while ((line = fileReader.readLine()) != null) {
                lineNo++;
                if (lineNo == 1) {
                    //don't do anything
                } else {
                    ArrayList<String> cellList = new ArrayList();
                    //Get all tokens available in line
                    String[] tokens = line.split(DELIMITER);
                    for (String token : tokens) {
                        //Print all tokens
                   /* if(D)
                        Log.d(TAG,"token: "+token);
                   */
                        cellList.add(token.trim());
                        // System.out.println(token);
                    }
                    rowList.add(cellList);
                }

            }
            if (D)
                Log.d("CSV ", "rowList====> " + rowList.size());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rowList;
    }

    public static ArrayList readCSVStudentDifferentFormat(String fileName) {

        ArrayList rowList = new ArrayList();


        //Input file which needs to be parsed
        String fileToParse = Util.getFilePath(fileName);
        BufferedReader fileReader = null;

        //Delimiter used in CSV file
        final String DELIMITER = ",";
        try {

            String line = "";
            //Create the file reader
            fileReader = new BufferedReader(new FileReader(fileToParse));
            int lineNo = 0;

            //Read the file line by line
            while ((line = fileReader.readLine()) != null) {
                lineNo++;
                if (lineNo == 1 || lineNo == 3) {
                    //don't do anything
                } else {
                    ArrayList<String> cellList = new ArrayList();
                    //Get all tokens available in line
                    String[] tokens = line.split(DELIMITER);

                    for (String token : tokens) {
                        //Print all tokens
                        String cell[] = token.split("\\|");
                        for (String cellElement : cell) {
                            cellList.add(cellElement.trim());
                        }

                    }
                    rowList.add(cellList);
                }

            }
            if (D)
                Log.d("CSV ", "rowList====> " + rowList.size());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rowList;
    }

    public static ArrayList readCSVSchool(String fileName) {
        ArrayList rowList = new ArrayList();


        //Input file which needs to be parsed
        String fileToParse = Util.getFilePath(fileName);
        BufferedReader fileReader = null;

        //Delimiter used in CSV file
        final String DELIMITER = ",";
        try {

            String line = "";
            //Create the file reader
            fileReader = new BufferedReader(new FileReader(fileToParse));
            int lineNo = 0;
            //Read the file line by line
            while ((line = fileReader.readLine()) != null) {
                lineNo++;
                if (lineNo == 1) {
                    //don't do anything
                } else {
                    ArrayList<String> cellList = new ArrayList();
                    //Get all tokens available in line
                    String[] tokens = line.split(DELIMITER);

                    for (String token : tokens) {
                        //Print all tokens
                        String cell[] = token.split("\\|");
                        for (String cellElement : cell) {
                            cellList.add(cellElement.trim());
                        }

                    }
                    rowList.add(cellList);

                }


            }
            if (D)
                Log.d("CSV ", "rowList====> " + rowList.size());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rowList;
    }
}
