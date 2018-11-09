package CS4262.Models.DataTransfer;

/**
 *
 * @author Lahiru Kaushalya
 */

//Create node object for data transfer perposes
public class NodeDTO{
    
    private final String ipAddress;
    private final int udpPort;
    private final int tcpPort;
    
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
    
    
    
}

