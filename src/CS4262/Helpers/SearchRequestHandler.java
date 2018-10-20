package CS4262.Helpers;

import CS4262.Models.NodeDTO;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class SearchRequestHandler extends MsgHandler{

    public SearchRequestHandler() {
        super();
    }

    @Override
    public void handle(StringTokenizer st) {
        //Message sender
        String senderIP = st.nextToken();
        int senderPort = Integer.parseInt(st.nextToken());
        NodeDTO sender = new NodeDTO(senderIP, senderPort);
        
        String searchFile = st.nextToken();
        
        searchInitializer.serachFile(sender, searchFile);
    }
    
}
