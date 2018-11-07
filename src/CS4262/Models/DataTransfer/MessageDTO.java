package CS4262.Models.DataTransfer;

import CS4262.Models.*;
import java.util.List;

/**
 *
 * @author Lahiru Kaushalya
 */
public class MessageDTO {
    
    private NodeDTO sender;
    private final NodeDTO receiver;
    private int hopCount = 0;
    private File file;
    private Word word;
    private SearchDTO searchDTO;
    private List<NodeDTO> additional;
    private List<SearchResult> searchResults;
    
    public MessageDTO(NodeDTO receiver){
        this.receiver = receiver;
    }
    
    public MessageDTO(NodeDTO receiver, Word word){
        this.receiver = receiver;
        this.word = word;
    }
    
    public MessageDTO(NodeDTO receiver, List<SearchResult> searchResults){
        this.receiver = receiver;
        this.searchResults = searchResults;
    }
    
    public MessageDTO(NodeDTO receiver, Word word, File file){
        this.receiver = receiver;
        this.file = file;
        this.word = word;
    }
    
    public MessageDTO(NodeDTO receiver, NodeDTO sender){
        this.receiver = receiver;
        this.sender = sender;
    }
    
    public MessageDTO(NodeDTO receiver, NodeDTO sender, int hopCount){
        this.receiver = receiver;
        this.sender = sender;
        this.hopCount = hopCount;
    }
    
    public MessageDTO(NodeDTO receiver, NodeDTO sender, SearchDTO searchDTO){
        this.receiver = receiver;
        this.sender = sender;
        this.searchDTO = searchDTO;
    }
    
    public MessageDTO(NodeDTO receiver, NodeDTO sender, File file){
        this.receiver = receiver;
        this.sender = sender;
        this.file = file;
    }
    
    public MessageDTO(NodeDTO receiver, NodeDTO sender, int hopCount, List<NodeDTO> additional){
        this.receiver = receiver;
        this.sender = sender;
        this.hopCount = hopCount;
        this.additional = additional;
    }
    

    public NodeDTO getSender() {
        return sender;
    }

    public NodeDTO getReceiver() {
        return receiver;
    }

    public int getHopCount() {
        return hopCount;
    }
    
    public File getFile() {
        return file;
    }
    
    public Word getWord() {
        return word;
    }
    
    public SearchDTO getSearchDTO() {
        return searchDTO;
    }

    public List<NodeDTO> getAdditional() {
        return additional;
    }
    
    public List<SearchResult> getSearchResults() {
        return searchResults;
    }
    
}
