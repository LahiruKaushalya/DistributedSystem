package CS4262.Helpers;

import CS4262.Models.Node;
import CS4262.Models.NodeDTO;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class PredecessorHandler extends MsgHandler{
    
    public PredecessorHandler(){
        super();
    }
    
    @Override
    public void handle(StringTokenizer st) {
        //predecessor node 
        String preIP = st.nextToken();
        int prePort = Integer.parseInt(st.nextToken());
        String preID = idCreator.generateNodeID(preIP, prePort);
        Node predecessor = new Node(preIP, prePort, preID);
        
        mainController.getMainFrame().updatePredecessorDetails(predecessor);
    }
    
}
