package CS4262.Models;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lahiru Kaushalya
 */
public class MessageDTO {
    
    private NodeDTO sender;
    private NodeDTO receiver;
    private int hopCount = 0;
    private String fileNameOrID;
    private List<NodeDTO> additional = new ArrayList<>();
    private List<NodeDTO> fileHolders = new ArrayList<>();
    
    public MessageDTO(NodeDTO receiver){
        this.receiver = receiver;
    }
    
    public MessageDTO(NodeDTO receiver, List<NodeDTO> fileHolders){
        this.receiver = receiver;
        this.fileHolders = fileHolders;
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
    
    public MessageDTO(NodeDTO receiver, NodeDTO sender, String fileNameOrID){
        this.receiver = receiver;
        this.sender = sender;
        this.fileNameOrID = fileNameOrID;
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
    
    public String getFileNameOrID() {
        return fileNameOrID;
    }

    public List<NodeDTO> getAdditional() {
        return additional;
    }
    
    public List<NodeDTO> getFileHolders() {
        return fileHolders;
    }
    
}
