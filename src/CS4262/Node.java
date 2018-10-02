/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CS4262;

import java.util.List;

/**
 *
 * @author Sankaja
 */
public class Node 
{
    private String ipAdress;
    private String port;
    private List<String> content;
    
    public Node(String ip, String port, List<String> content)
    {
        this.content = content;
        this.port = port;
        this.ipAdress = ip;
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
    public String getPort() {
        return port;
    }

    /**
     * @return the content
     */
    public List<String> getContent() {
        return content;
    }

    /**
     * @param content the content to set
     * 
     * In case of a content update e.g. graceful departure,
     * get content list, update it and set it
     */
    public void setContent(List<String> content) {
        this.content = content;
    }
    
    
}
