package CS4262.Models.DataTransfer;

/**
 *
 * @author Lahiru Kaushalya
 */

//Create node object for data transfer perposes
public class NodeDTO{
    
    private final String ipAddress;
    private final int port;
    
    public NodeDTO(String ip, int port)
    { 
        this.ipAddress = ip;
        this.port = port;
    }
    
    public String getIpAdress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

}

