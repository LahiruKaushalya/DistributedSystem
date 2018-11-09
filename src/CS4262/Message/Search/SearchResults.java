package CS4262.Message.Search;

import CS4262.Models.DataTransfer.NodeDTO;
import java.util.StringTokenizer;
import CS4262.Interfaces.IMessage;
import CS4262.Models.DataTransfer.MessageDTO;
import CS4262.Models.File;
import CS4262.Models.SearchResult;
import java.util.List;

/**
 *
 * @author Lahiru Kaushalya
 */
public class SearchResults implements IMessage{
    
    private MessageDTO msgDTO;
    
    @Override
    public String send(MessageDTO msgDTO){
        this.msgDTO = msgDTO;
        String message = createMsg();
        return msgSender.sendMsg(msgDTO.getReceiver(), message);
    }
    
    /*
    Search Results message format 
    length SEROK file_name fileholder_ip fileholder_port......
    */
    @Override
    public String createMsg() {
        List<SearchResult> searchResults = msgDTO.getSearchResults();
        String msg = " SEROK " + searchResults.size() + " ";
        for(SearchResult searchResult : searchResults){
            msg += searchResult.getFile().getName() + " " 
                 + searchResult.getFileHolder().getipAdress() + " "
                 + searchResult.getFileHolder().getUdpPort() + " ";
        }
        return String.format("%04d", msg.length() + 5) + " " + msg; 
    }
    
    @Override
    public void handle(StringTokenizer st) {
        int filesFound = Integer.parseInt(st.nextToken());
        List<SearchResult> searchResults = node.getSearchResults();
        while(filesFound > 0){
            String fileName = st.nextToken();
            String fileHolderIP = st.nextToken();
            int fileHolderPort = Integer.parseInt(st.nextToken());
            searchResults.add(
                    new SearchResult(
                            new File(fileName, idCreator.generateFileID(fileName)),
                            new NodeDTO(fileHolderIP, fileHolderPort)
                    )
            );
            filesFound--;
        }
        node.setSearchResults(searchResults);
        uiCreator.updateSearchResultsUI();
    }
    
}
