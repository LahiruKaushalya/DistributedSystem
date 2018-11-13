package CS4262.Models.DataTransfer;

import java.io.Serializable;

/**
 *
 * @author Lahiru Kaushalya
 */

//Create node object for data transfer perposes
public class NodeDTO implements Serializable
{    
    
    private String ipAddress;
    private int udpPort;
    private int tcpPort;
    
    public NodeDTO(String ip, int udpPort)
    { 
        this.ipAddress = ip;
        this.udpPort = udpPort;
        this.tcpPort = udpPort + 10000;
    }
    
    public String getipAdress() {
        return ipAddress;
    }

    public int getUdpPort() {
        return udpPort;
    }

    public int getTcpPort() {
        return tcpPort;
    }
    
    /**
     * @param ipAddress the ipAddress to set
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * @param udpPort the udpPort to set
     */
    public void setUdpPort(int udpPort) {
        this.udpPort = udpPort;
    }

    /**
     * @param tcpPort the tcpPort to set
     */
    public void setTcpPort(int tcpPort) {
        this.tcpPort = tcpPort;
    }
    
}

