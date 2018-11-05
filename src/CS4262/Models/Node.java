package CS4262.Models;

import CS4262.Helpers.IDCreator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Sankaja
 */
public class Node extends NodeDTO
{
    private final String id;
    
    private Node successor;
    private Node predecessor;
    
    private Map<String, List<NodeDTO>> fileIndex;
    private Map<String, List<NodeDTO>> fileIndexBackup;
    
    private Map<String, List<File>> wordIndex;
    private Map<String, List<File>> wordIndexBackup;

    private List<File> content;
    //Routes has fixed length -> m 
    private Node[] routes;

    public Node(String ip, int port, String nodeID)
    { 
        super(ip, port);
        this.id = nodeID;
        this.content = new ArrayList<>();
        this.routes = new Node[new IDCreator().getBIN_ID_LENGTH()];
        this.fileIndex = new HashMap<>();
        this.wordIndex = new HashMap<>();
        this.fileIndexBackup = new HashMap<>();
        this.wordIndexBackup = new HashMap<>();
    }
    
    /**
     * @return the node id
     */
    public String getId() {
        return id;
    }

    /**
     * @return the content
     */
    public List<File> getContent() {
        return content;
    }
    
    /**
     * @return the routes
     */
    public Node[] getRoutes() {
        return routes;
    }
    
    /**
     * @return the successor
     */
    public Node getSuccessor() {
        return successor;
    }
    
    /**
     * @return the predecessor
     */
    public Node getPredecessor() {
        return predecessor;
    }

    public Map<String, List<NodeDTO>> getFileIndex() {
        return fileIndex;
    }
    
    public Map<String, List<NodeDTO>> getFileIndexBackup() {
        return fileIndexBackup;
    }
    
    public Map<String, List<File>> getWordIndex() {
        return wordIndex;
    }
    
    public Map<String, List<File>> getWordIndexBackup() {
        return wordIndexBackup;
    }

    /**
     * @param content the content to set
     * 
     * In case of a content update e.g. graceful departure,
     * get content list, update it and set it
     */
    public void setContent(List<File> content) {
        this.content = content;
    }
    
    public void setRoutes(Node[] routes) {
        this.routes = routes;
    }
    
    public void setSuccessor(Node successor) {
        this.successor = successor;
    }
    
    public void setPredecessor(Node predecessor) {
        this.predecessor = predecessor;
    }
    
    public void setFileIndex(Map<String, List<NodeDTO>> fileIndex) {
        this.fileIndex = fileIndex;
    }
    
    public void setFileIndexBackup(Map<String, List<NodeDTO>> fileIndexBackup) {
        this.fileIndexBackup = fileIndexBackup;
    }
    
    public void setWordIndex(Map<String, List<File>> wordIndex) {
        this.wordIndex = wordIndex;
    }
    
    public void setWordIndexBackup(Map<String, List<File>> wordIndexBackup) {
        this.wordIndexBackup = wordIndexBackup;
    }

}

