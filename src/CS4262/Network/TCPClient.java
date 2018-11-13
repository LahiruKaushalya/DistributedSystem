package CS4262.Network;

import CS4262.Models.DataTransfer.FileDTO;
import CS4262.Models.DataTransfer.NodeDTO;

/**
 *
 * @author Sankaja
 */
public class TCPClient 
{
    /**
     * Client will be the message sender -> uploader of the file
     * and the server will listen -> downloader
     */
    
    private FileDTO file;
    
    public TCPClient(FileDTO fileTrnasferObj)
    {
        this.file = fileTrnasferObj;
        
    }
    
    public String sendFile()
    {
        String reply = "";
        
        
        
        return reply;
    }
}
