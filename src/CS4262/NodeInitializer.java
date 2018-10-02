package CS4262;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class NodeInitializer {
    
    private final String ipAddress;
    private final int port;
    private final MainController mc;
    
    public NodeInitializer(){
        this.mc = new MainController();
        this.ipAddress = mc.getIpAddress();
        this.port = mc.getPort();
    }
    
    public void initializeNode(ArrayList<ArrayList<String>> nodes) {

        System.out.print(nodes);
    }
}
