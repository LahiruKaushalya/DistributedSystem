package CS4262.Helpers;

import CS4262.Core.NodeInitializer;
import CS4262.Models.Node;
import CS4262.Models.NodeDTO;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class FileIndexHandler extends MsgHandler{
    
    private final String incomingMsg;
    
    public FileIndexHandler(String incomingMsg){
        super();
        this.incomingMsg = incomingMsg;
    }
    
    @Override
    public void handle(StringTokenizer st) {
        
        //Hop count
        int hopCount = Integer.parseInt(st.nextToken());
        hopCount--;
        
        //Message sender
        String senderIP = st.nextToken();
        int senderPort = Integer.parseInt(st.nextToken());
        String senderID = idCreator.generateNodeID(senderIP, senderPort);
        NodeDTO sender = new NodeDTO(senderIP, senderPort);
        
        String nodeID = node.getId();
        
        if(!senderID.equals(nodeID) && hopCount > 0){
            Node successor = node.getSuccessor();
            Node[] neighbours = node.getRoutes();
            //Update file index
            boolean same = contentInitializer.updateFileIndex(sender, st);
            
            if (!same) {
                //Flood node file index
                if (successor != null) {
                    msgSender.updateFileIndex(successor, node, NodeInitializer.getHopCount());
                }
            }
            
            
            if(successor != null){
                //Send incoming msg
                StringTokenizer st2 = new StringTokenizer(incomingMsg, " ");
                String msg = st2.nextToken() + " " + st2.nextToken() + " " + hopCount;
                while(st2.hasMoreTokens()){
                    msg += " " + st2.nextToken();
                }
                for (Node neighbour : neighbours) {
                    if (neighbour != null) {
                        msgSender.passFileIndex(neighbour, msg);
                    }
                }
            }
        }
        
    }
}
