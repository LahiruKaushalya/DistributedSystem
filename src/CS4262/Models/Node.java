package CS4262.Models;

import CS4262.Helpers.IDCreator;
import CS4262.MainController;
import java.util.ArrayList;

/**
 *
 * @author Sankaja
 */
public class Node extends NodeDTO
{
    private final String id;
    
    private ArrayList<String> content;
    private Node[] routes;

    public Node(String ip, int port, String nodeID)
    { 
        super(ip, port);
        this.id = nodeID;
        this.content = new ArrayList<String>();
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
    
    public Node[] getRoutes() {
        return routes;
    }

    public void setRoutes(Node[] routes) {
        this.routes = routes;
    }
    
}

