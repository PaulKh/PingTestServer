package model;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Paul on 05/07/15.
 */
public class Ping implements Serializable {
    private String pingResult;
    private InetAddress sourceAddress;
    private InetAddress destinationAddress;

    public static Ping pingBuilder(String pingResult, String sourceAddress, String destinationAddress) throws UnknownHostException {
        InetAddress sourceAddr = InetAddress.getByName(sourceAddress);
        InetAddress destAddr = InetAddress.getByName(destinationAddress);
        return new Ping(pingResult, sourceAddr, destAddr);
    }

    private Ping(String pingResult, InetAddress sourceAddress, InetAddress destinationAddress) {
        this.pingResult = pingResult;
        this.sourceAddress = sourceAddress;
        this.destinationAddress = destinationAddress;
    }

    public String getPingResult() {
        return pingResult;
    }

    public InetAddress getSourceAddress() {
        return sourceAddress;
    }

    public InetAddress getDestinationAddress() {
        return destinationAddress;
    }
}
