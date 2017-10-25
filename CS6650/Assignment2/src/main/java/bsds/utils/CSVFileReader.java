//package bsds.utils;
//
//import bsds.model.Record;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//
//// Read CSV file in different manner
//public class CSVFileReader {
//
//    ArrayList<Record> Records = new ArrayList<Record>();
//
//    // Read the .csv file to get the data
//    public ArrayList<Record> readCsvFile(String path) {
//
//        // make sure the ArrayList is empty before you start recording the data
//        if (!Records.isEmpty()) {
//            Records.clear();
//        }
//
//        try {
//
//            // read data from the .csv file
//            System.out.println("Reading array list....");
//
//            BufferedReader br = new BufferedReader(new java.io.FileReader(path));
//
//            String line = br.readLine();
//
//            // Using the first line
//            String[] fields1 = line.split(",");
//
//            HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
//            matchArg(fields1[1], map, 1);
//            matchArg(fields1[2], map, 2);
//            matchArg(fields1[3], map, 3);
//            matchArg(fields1[4], map, 4);
//
//            while ((line = br.readLine()) != null) {
//
//                String[] fields = line.split(",");
//
//                Record record = new Record( // the fields in .csv are jumbled
//                        Integer.parseInt(fields[map.get(1)]), // skierID
//                        Integer.parseInt(fields[map.get(2)]), // liftID
//                        Integer.parseInt(fields[map.get(3)]), // dayNum
//                        Integer.parseInt(fields[map.get(4)])); // time
//                Records.add(record);
//            }
//
//            br.close();
//            System.out.println("!!Reading completed!!");
//            return Records;
//            //outputData(Records);
//
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//        }
//        return Records;
//    }
//
//    private void matchArg(String field, HashMap<Integer, Integer> map, int i) {
//
//        switch (field) {
//            case "Day":
//                map.put(3, i);
//                break;
//            case "SkierID":
//                map.put(1, i);
//                break;
//            case "LiftID":
//                map.put(2, i);
//                break;
//            case "Time":
//                map.put(4, i);
//                break;
//            default:
//        }
//    }
//
//    // Helper to readFile
//    // To print the data read fom the file
//    public void outputData(ArrayList<Record> Records) {
//
//        String newline = System.getProperty("line.separator");
//        int count = 0;
//        System.out.println("Array List contents");
//        for (Record tmp : Records) {
//            System.out.print(String.valueOf(tmp.getRecordID()) + " " +
//                    String.valueOf(tmp.getDayNum()) + " " +
//                    String.valueOf(tmp.getSkierID()) + " " +
//                    String.valueOf(tmp.getLiftID()) + " " +
//                    String.valueOf(tmp.getTime()) + newline
//            );
//            count++;
//        }
//        System.out.println("Record Count = " + count);
//    }
//}
