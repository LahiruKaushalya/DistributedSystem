package CS4262.Helpers.Messages;

import CS4262.Models.NodeDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class SearchResults implements Message{
    
    private List<NodeDTO> fileHolders;
    
    public SearchResults() {
        this.fileHolders = new ArrayList<>();
    }
    
    public String send(NodeDTO receiver, List<NodeDTO> fileHolders){
        this.fileHolders = fileHolders;
        String message = createMsg();
        return msgSender.sendMsg(receiver, message);
    }
    
    @Override
    public String createMsg() {
        String msg = " SEROK " + fileHolders.size();
        for(NodeDTO holder : fileHolders){
            msg += " " + holder.getIpAdress() + " " + holder.getPort();
        }
        return String.format("%04d", msg.length() + 5) + " " + msg; 
    }
    
    @Override
    public void handle(StringTokenizer st) {
        int filesFound = Integer.parseInt(st.nextToken());
        for(int i = 0; i < filesFound; i++){
            fileHolders.add(new NodeDTO(st.nextToken(), Integer.parseInt(st.nextToken())));
        }
        updateResultsUI();
    }
    
    private void updateResultsUI(){
        String dataText = "Node ID\tNode IP Adress\tUDP Port\tTCP Port\n\n";
        for(NodeDTO fileHolder : fileHolders){
            dataText += idCreator.generateNodeID(fileHolder.getIpAdress(), fileHolder.getPort()) + "\t"
                     + fileHolder.getIpAdress() + "\t\t"
                     + fileHolder.getPort() + "\t"
                     + " - " + "\n";
        }
        mainController.getMainFrame().updateSearchResponse(dataText);
    }

}
