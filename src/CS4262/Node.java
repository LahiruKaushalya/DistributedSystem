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
public class Node 
{
    private String username;
    private final String ipAdress;
    private final int port;
    private ArrayList<String> content;
    
    public Node(String ip, int port)
    { 
        this.content = new ArrayList<String>();
        this.port = port;
        this.ipAdress = ip;
    }
    
    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the ipAdress
     */
    public String getIpAdress() {
        return ipAdress;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
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
    
    
}
