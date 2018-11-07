package CS4262.Helpers;

import CS4262.Interfaces.IMain;
import CS4262.Models.File;
import CS4262.Models.Node;
import CS4262.Models.DataTransfer.NodeDTO;
import CS4262.Models.SearchResult;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Lahiru Kaushalya
 */
public class UICreator implements IMain{
    
    private final String contentHeader;
    private final String routingHeader;
    private final String searchResultsHeader;
    private final String fileIndexHeader;
    private final String wordIndexHeader;
    
    public UICreator(){
        this.contentHeader = "File ID\tFile Name\n\n";
        this.routingHeader = "Index\tRange\tNode ID\tIP Address\tPort\n\n";
        this.searchResultsHeader = "File Name\t\t\tFile Holder\n\t\t\tIP Adress\tUDP\tTCP\n";
        this.fileIndexHeader = "File ID\t\t\tNode\n\t\tIP Address\t\tPort\n\n";
        this.wordIndexHeader = "Word\t\t\tFile\nID\tName\t\tId\tName";
    }

    public void updateFileIndexUI(){
        String displayText = fileIndexHeader;
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
        String displayText = wordIndexHeader;
        Map<String, List<File>> indices = node.getWordIndex();
        
        for(String wordName : indices.keySet()) {
            displayText += idCreator.generateWordID(wordName) + "\t" + wordName + "\t";
            int count = 0;
            for(File file : indices.get(wordName)){
                if(count != 0){
                    displayText += "\t\t\t";
                }
                displayText += file.getId() + "\t" + file.getName().replace('_', ' ') + "\n";
                count ++;
            }
            displayText += "\n";
        }
        mainController.getMainFrame().updateWordIndex(displayText);
    }
    
    public void updateRoutesUI(){
        String displayText = routingHeader;
        
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
    
    public void updateSearchResultsUI(){
        String dataText = searchResultsHeader;
        List<SearchResult> searchResults = node.getSearchResults();
        for(SearchResult searchResult : searchResults){
            
            File file = searchResult.getFile();
            NodeDTO fileHolder = searchResult.getFileHolder();
            
            dataText += file.getName().replace('_', ' ') + "\t\t\t"
                    + fileHolder.getIpAdress() + "\t"
                    + fileHolder.getPort() + "\t"
                    + "-\n";
        }
        mainController.getMainFrame().updateSearchResponse(dataText);
    }
    
    public String getContentHeader() {
        return contentHeader;
    }

    public String getRoutingHeader() {
        return routingHeader;
    }

    public String getSearchResultsHeader() {
        return searchResultsHeader;
    }

    public String getFileIndexHeader() {
        return fileIndexHeader;
    }

    public String getWordIndexHeader() {
        return wordIndexHeader;
    }
    
}
