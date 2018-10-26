package CS4262.Core;

import CS4262.Helpers.IDCreator;
import CS4262.Helpers.RangeChecker;
import CS4262.MainController;
import CS4262.Models.File;
import CS4262.Models.Node;
import CS4262.Models.NodeDTO;
import CS4262.Network.MessageSender;
import java.util.ArrayList;
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
    
    public void localSearch(String fileName){
        if(node != null){
            //Filter user input
            fileName = filterFileName(fileName);
            
            String searchingFileID = idCreator.generateFileID(fileName);
            //Check in local files
            boolean isAvailble = isFileAvailable(searchingFileID);
            if (isAvailble) {
                //File locally available
            }
            else{
                //Check in file index
                List<NodeDTO> fileHolders = getFileHolders(searchingFileID);
                if(fileHolders != null){
                    //request file from file Holders
                }
                else{
                    //File or File index not available locally. Start global search..
                    globalSearch(node, fileName);
                }
            }
        }
    }
    
    public void globalSearch(NodeDTO sender, String fileName) {
        String searchingFileID = idCreator.generateFileID(fileName);
        String senderID = idCreator.generateNodeID(sender.getIpAdress(), sender.getPort());
        
        if (!senderID.equals(node.getId())) {
            //Search locally first
            List<NodeDTO> fileHolders = getFileHolders(searchingFileID);
            //Check in local files
            boolean isAvailble = isFileAvailable(searchingFileID);
            if (isAvailble) {
                //File locally available
                NodeDTO thisNode = new NodeDTO(node.getIpAdress(), node.getPort());
                if (fileHolders != null) {
                    fileHolders.add(thisNode);
                } else {
                    fileHolders = new ArrayList<>();
                    fileHolders.add(thisNode);
                }
            }
            //Check at least one file holder has been found
            if (fileHolders != null) {
                msgSender.searchOK(sender, fileHolders);
            } 
            //File holder not found. 
            else {
                //Check for a search redirector node
                findRedirector(sender, fileName);
            }
        }
        else{
            //Check for a search redirector node
            findRedirector(sender, fileName);
        }
    }

    private String filterFileName(String fileName) {
        //Implement filter logic
        return fileName;
    }
    
    private boolean isFileAvailable(String searchingFileID){
        List<File> files = node.getContent();
        for (File file : files) {
            if (file.getId().equals(searchingFileID)) {
                return true;
            }
        }
        return false;
    }
    
    private List<NodeDTO> getFileHolders(String searchingFileID){
        //Check searching file index availability
        Map<String, List<NodeDTO>> index = node.getFileIndex();
        for (String fileID : index.keySet()) {
            if (fileID.equals(searchingFileID)) {
                return index.get(fileID);
            }
        }
        return null;
    }
    
    private void findRedirector(NodeDTO sender, String fileName) {
        String searchingFileID = idCreator.generateFileID(fileName);
        int fileIntID = idCreator.getComparableID(searchingFileID);
        Node redirector = null;
        Node[] neighbours = node.getRoutes();
        
        if (neighbours != null) {
            int nodeIntID = idCreator.getComparableID(node.getId());
            Node neighbour;
            int neighbourIntID;
            //Find nearest node to file 
            for (int i = neighbours.length - 1; i >= 0; i--) {
                neighbour = neighbours[i];
                if (neighbour != null) {
                    neighbourIntID = idCreator.getComparableID(neighbour.getId());
                    if (rangeChecker.isInRange(nodeIntID, neighbourIntID, fileIntID)) {
                        redirector = neighbour;
                        break;
                    }
                }
            }
            if(redirector != null){
                msgSender.search(redirector, sender, fileName);
            }
            else{
                //File not found
            }
        }
        else{
            //File not found
        }
    }
}
