package CS4262.Models;

import CS4262.Helpers.IDCreator;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sankaja
 */
public class Node extends NodeDTO
{
    private final String id;
    
    private Node successor;
    private List<File> content;
    private Node[] routes;

    public Node(String ip, int port, String nodeID)
    { 
        super(ip, port);
        this.id = nodeID;
        this.content = new ArrayList<>();
        this.routes = new Node[new IDCreator().getBIN_ID_LENGTH()];
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
     * @return the successor
     */
    public Node getSuccessor() {
        return successor;
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
    
    public Node[] getRoutes() {
        return routes;
    }

    public void setRoutes(Node[] routes) {
        this.routes = routes;
    }
    
    public void setSuccessor(Node successor) {
        this.successor = successor;
    }
}

