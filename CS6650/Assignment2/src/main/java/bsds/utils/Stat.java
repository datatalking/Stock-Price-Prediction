package bsds.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Statistics for the Program
 */
public class Stat {
    private int successRequestsNum = 0;
    private int sentRequestsNum = 0;
    private long totalLatency = 0;
    private boolean isSorted = false;
    private List<Long> latencies = new ArrayList<Long>();

    synchronized public void recordLatency(long latency) {
        totalLatency += latency;
        latencies.add(latency);
    }

    synchronized public void recordSentRequestNum() {
        sentRequestsNum += 1;
    }

    synchronized public void recordSuccessfulRequestNum(boolean isSuccess) {
        successRequestsNum += isSuccess ? 1 : 0;
    }

    public long getMeanLatency() {
        return totalLatency / successRequestsNum;
    }

    public long getMedianLatency() {
        if (!isSorted) {
            Collections.sort(latencies);
            isSorted = true;
        }

        return latencies.get(latencies.size() / 2);
    }

    public long get95thLatency() {
        if (!isSorted) {
            Collections.sort(latencies);
            isSorted = true;
        }

        return latencies.get((int)Math.floor(latencies.size() * 0.95));
    }

    public long get99thLatency() {
        if (!isSorted) {
            Collections.sort(latencies);
            isSorted = true;
        }

        return latencies.get((int)Math.floor(latencies.size() * 0.99));
    }

    public int getSuccessRequestsNum() {
        return successRequestsNum;
    }

    public int getSentRequestsNum() {
        return sentRequestsNum;
    }

    public long getTotalLatency() {
        return totalLatency;
    }
}