package CS4262.Helpers;

import CS4262.Interfaces.IMain;
import CS4262.Models.Node;
import CS4262.Models.NodeDTO;

/**
 *
 * @author Lahiru Kaushalya
 */
public class FindReceiver implements IMain{
    
    //Find relevent node to index files/words 
    public NodeDTO search(String contentID){
        NodeDTO receiver = null;
        int contentIntID = idCreator.getComparableID(contentID);
        int nodeIntID = idCreator.getComparableID(node.getId());
        Node[] neighbours = node.getRoutes();
        
        for(Node neighbour : neighbours){
            if(neighbour != null){
                int neiIntID = idCreator.getComparableID(neighbour.getId());
                if(rangeChecker.isInRange(nodeIntID, neiIntID, contentIntID)){
                    receiver = neighbour;
                }
                else{break;}
            }
        }
        return receiver;
    }
    
}
