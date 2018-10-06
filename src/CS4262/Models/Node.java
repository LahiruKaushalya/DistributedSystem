package CS4262.Models;

import java.util.ArrayList;

/**
 *
 * @author Sankaja
 */
public class Node extends NodeDTO
{
    private final String username;
    private final String id;
    
    private ArrayList<String> content;
    private Node successor;
    private Node predecessor;
    
    public Node(String ip, int port, String username, String nodeID)
    { 
        super(ip, port);
        this.username = username;
        this.id = nodeID;
        this.content = new ArrayList<String>();
    }
    
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
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
    public ArrayList<String> getContent() {
        return content;
    }

    /**
     * @param content the content to set
     * 
     * In case of a content update e.g. graceful departure,
     * get content list, update it and set it
     */
    public void setContent(ArrayList<String> content) {
        this.content = content;
    }
    
    /**
     * @return the successor
     */
    public Node getSuccessor() {
        return successor;
    }

    public void setSuccessor(Node successor) {
        this.successor = successor;
    }
    
    /**
     * @return the predecessor
     */
    public Node getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Node predecessor) {
        this.predecessor = predecessor;
    }
    
}

