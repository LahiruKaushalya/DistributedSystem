/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CS4262;

import java.util.ArrayList;

/**
 *
 * @author Sankaja
 */

//Create node object for data transfer perposes

class NodeDTO{
    
    private final String ipAddress;
    private final int port;
    
    public NodeDTO(String ip, int port)
    { 
        this.ipAddress = ip;
        this.port = port;
    }
    
    public String getIpAdress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

}

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
