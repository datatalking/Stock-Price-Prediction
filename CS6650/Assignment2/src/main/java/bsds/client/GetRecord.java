package bsds.client;

import bsds.utils.Stat;
import bsds.model.Vertical;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

/**
 * Get the total vertical and number of lift rides from the database
 */
public class GetRecord implements Callable<Vertical> {

    private String protocol;
    private String host;
    private int port;
    private int skierID;
    private int dayNum;
    Client client;
    Stat stat;

    public GetRecord(String protocol, String host, int port, int skierID, int dayNum, Client client, Stat stat) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.skierID = skierID;
        this.dayNum = dayNum;
        this.client = client;
        this.stat = stat;
    }

    public Vertical call() {

        URL url = null;
        String api = "/rest/myvert/" + skierID + "&" + dayNum;
        try{
            url = new URL(protocol, host, port, api);
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        WebTarget webTarget = client.target(url.toString());
        Response response;
        Vertical result = null;
        long startTime = System.currentTimeMillis();
        try {
            response = webTarget.request().get();
            result = response.readEntity(Vertical.class);

            System.out.println("INPUT");
            System.out.println("* SkierID: " + skierID);
            System.out.println("* Day Number: "+dayNum);
            System.out.println("OUTPUT");
            System.out.println("* Total Vertical Height Gain: " + result.getTotalVertical());
            System.out.println("* Number of  Lift Rides: " + result.getLiftTimes());
            response.close();
            stat.recordSentRequestNum();
            stat.recordSuccessfulRequestNum(response.getStatus() == 200);
        } catch (ProcessingException e) {
            e.printStackTrace();
        }
        long latency = System.currentTimeMillis() - startTime;
        stat.recordLatency(latency);
        return result;
    }
}