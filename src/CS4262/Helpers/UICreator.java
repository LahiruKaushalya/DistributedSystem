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
    private final String fileIndexHeader;
    private final String wordIndexHeader;
    
    public UICreator(){
        this.contentHeader = rpad("File ID",1) + rpad("File Name",2) + "\n\n";
        
        this.routingHeader =  rpad("Index",1) + rpad("Range",1) + rpad("Node ID",1) 
                                + rpad("IP Address",2) + rpad("TCP port",1) + "UDP port\n\n";
        
        this.fileIndexHeader = "File ID\t\t\tNode\n\t\tIP Address\t\tPort\n\n";
        
        this.wordIndexHeader = rpad("Word", 3) + rpad("File", 2) +  "\n"
                                + rpad("ID", 1) + rpad("Name", 2) + rpad("ID", 1) + rpad("Name", 2) + "\n\n";
    }
    
    //Getters
    public String getContentHeader() {
        return contentHeader;
    }

    public String getRoutingHeader() {
        return routingHeader;
    }

    public String getFileIndexHeader() {
        return fileIndexHeader;
    }

    public String getWordIndexHeader() {
        return wordIndexHeader;
    }
    
    
    public void updateContentUI(){
        List<File> content = node.getContent();
        String displayText = contentHeader;
        
        for(File file : content){
            String fileName = Capitalize(file.getName());
            displayText += rpad(file.getId(),1) + rpad(fileName,2) +"\n";
        }
        mainController.getMainFrame().updateContent(displayText);
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
                displayText += _node.getipAdress() + "\t\t" + _node.getUdpPort() + "\n";
            }
            displayText += "\n";
        }
        mainController.getMainFrame().updateFileIndex(displayText);
    }
    
    public void updateWordIndexUI(){
        String displayText = wordIndexHeader;
        Map<String, List<File>> indices = node.getWordIndex();
        
        for(String wordName : indices.keySet()) {
            displayText += rpad(""+idCreator.generateWordID(wordName), 1) + rpad(Capitalize(wordName), 2);
            int count = 0;
            for(File file : indices.get(wordName)){
                if(count != 0){
                    displayText += rpad("", 2);
                }
                displayText += rpad(file.getId(), 1) + rpad(Capitalize(file.getName()), 2) + "\n";
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
        
        String ip, id;
        int udpPort, tcpPort, rend, lbound = 0;
        NodeDTO tempNode;
        
        for(int i = 0; i < m; i++){
            tempNode = routes[i];
            if(tempNode != null){
                ip = tempNode.getipAdress();
                udpPort = tempNode.getUdpPort();
                tcpPort = tempNode.getTcpPort();
                id = idCreator.generateNodeID(ip, udpPort);
                
                for(int k = lbound; k <= i; k++){
                    rend = (nodeID + (int)Math.pow(2, k)) % bp;
                    displayText += k + "\t" 
                                + rend + "\t" 
                                + id + "\t" 
                                + ip + "\t\t" 
                                + tcpPort + "\t" 
                                + udpPort + "\n";
                }
                lbound = i + 1;
            }
            else{
                if(i == m - 1){
                    for (int k = lbound; k <= i; k++) {
                        rend = (nodeID + (int)Math.pow(2, k)) % bp;
                        displayText += k + "\t" + rend + "\t-\t-\t\t-\t-\n";
                    }
                }
            }
        }
        mainController.getMainFrame().updateSuccessorDetails(node.getSuccessor());
        mainController.getMainFrame().updateRoutingTable(displayText);
    }
    
    public void updateSearchResultsUI(){
        List<SearchResult> searchResults = node.getSearchResults();
        Object[][] out = new Object[searchResults.size()][];
        int index = 0;
        for(SearchResult searchResult : searchResults){
            File file = searchResult.getFile();
            NodeDTO fileHolder = searchResult.getFileHolder();
            out[index] = new Object[]{
                    Capitalize(file.getName()), 
                    fileHolder.getipAdress(), 
                    fileHolder.getUdpPort(), 
                    fileHolder.getTcpPort()
            };
            index++;
        }
        mainController.getMainFrame().updateSearchResponse(out);
    }
    
    public void displayFileContent(File file){
        String dataText = "";
        
        dataText += rpad("File Name", 1) + rpad(Capitalize(file.getName()), 2) + "\n"
                + rpad("File Size", 1) + rpad(file.getFileSize() + " MB", 2) + "\n"
                + rpad("Hash", 1) + rpad(file.getHashCode(), 2);

        mainController.getMainFrame().displayFileContent(dataText);
    }
    
    public void updateInMsgCount(String value){
        mainController.getMainFrame().updateInMsg(value);
    }
    
    public void updateOutMsgCount(String value){
        mainController.getMainFrame().updateOutMsg(value);
    }
    
    private String Capitalize(String input){
        StringBuffer sb = new StringBuffer();
        for(String word : input.split("_")){
            sb.append(word.substring(0,1).toUpperCase());
            sb.append(word.substring(1));
            sb.append(" ");
        }
        return sb.toString();
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
}
