package CS4262.Helpers;

import CS4262.Models.NodeDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class SearchResultsHandler extends MsgHandler{
    
    private List<NodeDTO> fileHolders;
    
    public SearchResultsHandler() {
        super();
        this.fileHolders = new ArrayList<>();
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
            dataText += idCreator.generateNodeID(fileHolder.getIpAdress(), fileHolder.getPort()) + " "
                     + fileHolder.getIpAdress() + " "
                     + fileHolder.getPort() + " "
                     + " - " + "\n";
        }
        mainController.getMainFrame().updateSearchResponse(dataText);
    }
}
