package CS4262.Helpers;

import CS4262.Interfaces.IMain;
import CS4262.Models.Node;
import CS4262.Models.NodeDTO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lahiru Kaushalya
 */
public class UICreator implements IMain{
    
    public void updateFileIndexUI(){
        String displayText = "File ID\t\t\tNode\n\t\tIP Address\t\tPort\n\n";
        Map<String, List<NodeDTO>> indices = node.getFileIndex();
        
        for(String fileID : indices.keySet()) {
            displayText += fileID + "\t\t";
            int count = 0;
            for(NodeDTO _node : indices.get(fileID)){
                count++;
                if(count > 1){
                    displayText += "\t\t";
                }
                displayText += _node.getIpAdress() + "\t\t" + _node.getPort() + "\n";
            }
            displayText += "\n";
        }
        mainController.getMainFrame().updateFileIndex(displayText);
    }
    
    public void updateWordIndexUI(){
        String displayText = "Word ID\tWord\tFile IDs\n\n";
        Map<String, List<String>> indices = node.getWordIndex();
        
        for(String wordName : indices.keySet()) {
            displayText += idCreator.generateWordID(wordName) + "\t" + wordName + "\t";
            for(String fileID : indices.get(wordName)){
                displayText += fileID + "\t";
            }
            displayText += "\n";
        }
        mainController.getMainFrame().updateWordIndex(displayText);
    }
    
    public void updateRoutesUI(){
        String displayText = "Index\tRange\tNode ID\tIP Address\tPort\n\n";
        
        Node[] routes = node.getRoutes();
        int nodeID = idCreator.getComparableID(node.getId());
        int m = idCreator.getBIN_ID_LENGTH();
        int bp = (int)Math.pow(2, m);
        
        String ip = "-", id = "-";
        int port = 0, rend, lbound = 0;
        NodeDTO tempNode;
        
        for(int i = 0; i < m; i++){
            tempNode = routes[i];
            if(tempNode != null){
                ip = tempNode.getIpAdress();
                port = tempNode.getPort();
                id = idCreator.generateNodeID(ip, port);
                
                for(int k = lbound; k <= i; k++){
                    rend = (nodeID + (int)Math.pow(2, k)) % bp;
                    displayText += k + "\t" +rend + "\t" + id + "\t" + ip + "\t" + port + "\n";
                }
                lbound = i + 1;
            }
            else{
                if(i == m - 1){
                    for (int k = lbound; k <= i; k++) {
                        rend = (nodeID + (int)Math.pow(2, k)) % bp;
                        displayText += k + "\t" + rend + "\t-\t-\t-\n";
                    }
                }
            }
        }
        mainController.getMainFrame().updateSuccessorDetails(node.getSuccessor());
        mainController.getMainFrame().updateRoutingTable(displayText);
    }
    
}
