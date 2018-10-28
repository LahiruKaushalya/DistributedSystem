package CS4262.Helpers;

import static CS4262.Core.Initializer.mainController;
import static CS4262.Core.Initializer.node;
import CS4262.Models.NodeDTO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lahiru Kaushalya
 */
public class UICreator {
    
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
    
}
