package bsds.client;

import bsds.model.Record;
import bsds.utils.Stat;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Connection to the Database to post the data.
 */
public class PostRecord {

    WebTarget webTarget;
    Stat stat;

    PostRecord(WebTarget webTarget){
        this.webTarget = webTarget;
        stat = new Stat();
    }

    public Response doPost(Record data) {
        Response response;
        long startTime = System.currentTimeMillis();
            response = webTarget.request().post(Entity.json(data));
            //response = webTarget.request().post(Entity.entity(data, MediaType.APPLICATION_JSON), Response.class);
            System.out.println(response.readEntity(String.class));
            response.close();
            stat.recordSentRequestNum();
            stat.recordSuccessfulRequestNum(response.getStatus() == 200);
            long latency = System.currentTimeMillis() - startTime;
            stat.recordLatency(latency);

        return response;
    }
}