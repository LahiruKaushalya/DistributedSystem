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
        
        this.routingHeader =  rpad("Index",1) + rpad("Range",1) + rpad("Node ID",1) 
                                + rpad("IP Address",1) + rpad("UDP port",1) + "TCP port\n\n";
        
        this.searchResultsHeader = rpad("File Name",4) + "File Holder\n" + rpad("",2)
                                    + rpad("IP Address",1) + rpad("UDP port",1) + "TCP port\n\n";
        
        this.fileIndexHeader = "File ID\t\t\tNode\n\t\tIP Address\t\tPort\n\n";
        
        this.wordIndexHeader = rpad("Word", 3) + rpad("File", 2) +  "\n"
                                + rpad("ID", 1) + rpad("Name", 2) + rpad("ID", 1) + rpad("Name", 2) +"\n\n";
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
            displayText += rpad(""+idCreator.generateWordID(wordName), 1) + rpad(wordName, 2) ;
            int count = 0;
            for(File file : indices.get(wordName)){
                if(count != 0){
                    displayText += rpad("", 2);
                }
                displayText += rpad(file.getId(), 1) + rpad(file.getName().replace('_', ' '), 2) + "\n";
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
            
            dataText += rpad(file.getName().replace('_', ' ') , 3)
                    + rpad(fileHolder.getIpAdress() , 1)
                    + rpad("" + fileHolder.getPort() , 1)
                    + rpad("-" , 1)
                    + "\n";
        }
        mainController.getMainFrame().updateSearchResponse(dataText);
    }
    
    private String rpad(String word, int tabs) {
        int tabsReq = ((12 * tabs) - word.length()) / 12;
        String pad = "\t";
        while(tabsReq > 0){
            pad += "\t";
            tabsReq--;
        }
        return word + pad;
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
