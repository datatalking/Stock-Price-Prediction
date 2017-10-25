package bsds.client;

import bsds.model.Vertical;
import bsds.model.Record;
import bsds.utils.Stat;
import org.glassfish.jersey.client.ClientProperties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

// This is the client for the program (Entry Point)
public class MyClient {

    static final private String protocol = "http";
    static final private String host = "34.214.91.35";
    static final private int port = 8080;
    final private String api = "/Assignment2-tomcat_war/rest/load";

    private final String URL = "http://34.214.91.35:8080/Assignment2-tomcat_war/rest/load";

    static final String FILE_PATH = "/Users/divyaagarwal/Desktop/project/Assignment2/BSDSAssignment2Day1-TEST.csv";
    ArrayList<Record> Records = new ArrayList<Record>();

    // Read the .csv file to get the data
    public void readCsvFile(String path) {

        // make sure the ArrayList is empty before you start recording the data
        if (!Records.isEmpty()) {
            Records.clear();
        }

        try {

            // read data from the .csv file
            System.out.println("Reading array list....");
            BufferedReader br = new BufferedReader(new java.io.FileReader(path));
            // Skip the first line
            String line = br.readLine();
            while ((line = br.readLine()) != null) {

                String[] fields = line.split(",");
                Record record = new Record( // the fields in .csv are jumbled
                        Integer.parseInt(fields[2]), // skierID
                        Integer.parseInt(fields[3]), // liftID
                        Integer.parseInt(fields[1]), // dayNum
                        Integer.parseInt(fields[4])); // time
                Records.add(record);
            }

            br.close();
            System.out.println("!!Reading completed!!");
            //outputData(Records);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Using multi threading to post all the info
    public void postTasks(int numThreads) {

        System.out.println("Starting POST requests...");
        long startTime = System.currentTimeMillis();

        // Testing for 10000 data
        // Test for entire dataset use : Records.size()
        System.out.println("Number of Records to be written: "+Records.size());
        System.out.println("Number of Threads: "+numThreads);

        Client client = ClientBuilder.newClient();
        WebTarget webTarget = client.target(URL);
        final PostRecord postRecord = new PostRecord(webTarget);

        ExecutorService pool = Executors.newFixedThreadPool(numThreads);

        for(Record record : Records)
        {
            pool.submit(() -> postRecord.doPost(record));

        }

        pool.shutdown();
        // Stats


        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        int start =0;
//        //int end = Records.size();
//        for(int i=0; i< 1000; i++) {
//            postTasks.add(new PostRecord(protocol, host, port, "/rest/load", Records.get(i), client, stat));
//        }
//        ExecutorService pool = Executors.newFixedThreadPool(numThreads);
//        try {
//            pool.invokeAll(postTasks);
//            pool.shutdown();
//            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        long endTime = System.currentTimeMillis();
        client.close();
        System.out.println("Time Taken =" +(endTime - startTime));

    }

    public void getOneTask(int skierID, int dayNum) {
        long startTime = System.currentTimeMillis();
        Client client = ClientBuilder.newClient();
        String api = "/rest/myvert/" + skierID + "&" + dayNum;
        Stat stat = new Stat();
        GetRecord getRecord = new GetRecord(protocol, host, port, skierID, dayNum, client, stat);
        getRecord.call().toString();
        client.close();

        statsOutput(startTime, stat, 1);
    }

    // Using multi-threading to get all the records of the given DayNum
    public void getAllTasks(int dayNum, int taskSize) {

        System.out.println("Starting GET requests...");
        long startTime = System.currentTimeMillis();
        Client client = ClientBuilder.newClient();
        ArrayList<GetRecord> getRecords = new ArrayList<GetRecord>();
        Stat stat = new Stat();

        // Testing for 10000 data
        // Test for entire dataset use : Records.size()
        for (int i = 0; i < Records.size(); i++) {
            getRecords.add(new GetRecord(protocol, host, port, Records.get(i).getSkierID(), dayNum, client, stat));
        }
        ExecutorService pool = Executors.newFixedThreadPool(taskSize);
        try {
            List<Future<Vertical>> futures = pool.invokeAll(getRecords);
            pool.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        client.close();
        statsOutput(startTime, stat, taskSize);
    }

    // Display all the statistics related to a particular request
    private void statsOutput(long startTime, Stat stat, int taskSize) {

        System.out.println();
        System.out.println("    !!! END OF REQUEST !!!");
        System.out.println();
        System.out.println("----------------------------------");
        System.out.println("     STATISTICS");
        System.out.println("----------------------------------");
        System.out.println("1. Number of threads: " + taskSize);
        System.out.println("2. Total run time: " + (System.currentTimeMillis() - startTime));
        System.out.println("3. Total request sent: " + stat.getSentRequestsNum());
        System.out.println("4. Total successful request: " + stat.getSuccessRequestsNum());
        System.out.println("5. Mean latency: " + stat.getMeanLatency());
        System.out.println("6. Median latency: " + stat.getMedianLatency());
        System.out.println("7. 95th percentile latency: " + stat.get95thLatency());
        System.out.println("8. 99th percentile latency: " + stat.get99thLatency());
    }


    public static void main(String[] args) {
        MyClient myClient = new MyClient();

        /*
        * How to use the file :
        *  1. Uncomment myClient.postTasks(100) to load the data
        *  2. Uncomment myClient.singleGetTask(2,1) to get data for a particular skierID and dayNum
        *  3. Uncomment myClient.getTasks(1,100) to get all data for given dayNum
        *  *  NOTE : taskSize = Number of Threads in the threadpool
         */

        // read the .csv file data
        myClient.readCsvFile(FILE_PATH);
        myClient.postTasks(60);


        //myClient.getOneTask(4,1);
        //myClient.getAllTasks(1,100);

        // Truncate the data present in the Table to start fresh
//        MyRecord myRecord = new MyRecord();
//        try {
//            int i = myRecord.truncateRecord();
//            System.out.println(i);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }


    }
} // end of class