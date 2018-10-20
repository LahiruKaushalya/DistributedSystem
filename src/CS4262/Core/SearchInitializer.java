package CS4262.Core;

import CS4262.Helpers.IDCreator;
import CS4262.Helpers.RangeChecker;
import CS4262.MainController;
import CS4262.Models.Node;
import CS4262.Models.NodeDTO;
import CS4262.Network.MessageSender;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lahiru Kaushalya
 */
public class SearchInitializer {

    private final MainController mainController;
    private final Node node;
    private final IDCreator idCreator;
    private final RangeChecker rangeChecker;
    private final MessageSender msgSender;

    private static SearchInitializer instance;

    public static SearchInitializer getInstance() {
        if (instance == null) {
            instance = new SearchInitializer();
        }
        return instance;
    }

    private SearchInitializer() {
        this.mainController = MainController.getInstance();
        this.node = mainController.getNode();
        this.idCreator = new IDCreator();
        this.rangeChecker = new RangeChecker();
        this.msgSender = MessageSender.getInstance();
    }

    public void serachFile(NodeDTO sender, String fileName) {

        fileName = filterFileName(fileName);

        String searchingFileID = idCreator.generateFileID(fileName);

        //Check searching file index available
        Map<String, List<NodeDTO>> index = node.getFileIndex();
        List<NodeDTO> fileHolders = null;
        
        for (String fileID : index.keySet()) {
            if (fileID.equals(searchingFileID)) {
                fileHolders = index.get(fileID);
                break;
            }
        }

        //File index locally available
        if (fileHolders != null) {
            String senderID = idCreator.generateNodeID(sender.getIpAdress(), sender.getPort());
            //Check for local request
            if(senderID.equals(node.getId())){
                //request files
            }
            //Not a local request
            else{
                msgSender.searchOK(sender, fileHolders);
            }
        } 
        
        else {
            int fileIntID = idCreator.getComparableID(searchingFileID);

            Node[] neighbours = node.getRoutes();
            int nodeIntID = idCreator.getComparableID(node.getId());
            Node neighbour;
            NodeDTO receiver = null;
            int neighbourIntID;

            //Find nearest node to file 
            for (int i = neighbours.length - 1; i >= 0; i--) {
                neighbour = neighbours[i];
                if (neighbour != null) {
                    neighbourIntID = idCreator.getComparableID(neighbour.getId());
                    if (rangeChecker.isInRange(nodeIntID, neighbourIntID, fileIntID)) {
                        receiver = neighbour;
                        break;
                    }
                }
            }
            //Send search message
            if(sender == null){
                sender = node;
            }
            msgSender.search(receiver, sender, fileName);
        }
    }

    private String filterFileName(String fileName) {
        //Implement filter logic
        return fileName;
    }

}
