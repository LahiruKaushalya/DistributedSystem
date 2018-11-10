package CS4262.Network;

import CS4262.Interfaces.IMessage;
import CS4262.Message.Route.*;
import CS4262.Message.Search.*;
import CS4262.Message.FileIndex.*;
import CS4262.Message.WordIndex.*;
import CS4262.Message.Download.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 *
 * @author Lahiru Kaushalya
 */
public class MessageReceiver extends Thread{
    
    private final DatagramPacket incoming; 
    private final DatagramSocket server;
    
    public MessageReceiver(DatagramPacket incoming, DatagramSocket server){
        this.incoming = incoming;
        this.server = server;
    }
    
    @Override
    public void run(){
        try {
            byte[] data = incoming.getData();
            String incomingMsg = new String(data, 0, incoming.getLength());
            
            //Process incoming message
            String response = createResponse(incomingMsg);
            response = String.format("%04d", response.length() + 5) + " " + response;
        
            DatagramPacket dpReply = new DatagramPacket(response.getBytes() , response.getBytes().length , incoming.getAddress() , incoming.getPort());
            server.send(dpReply);
        } 
        catch (IOException ex) {
            Logger.getLogger(MessageReceiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String createResponse(String incomingMsg){
        
        StringTokenizer st = new StringTokenizer(incomingMsg, " ");
        String length = st.nextToken();
        String command = st.nextToken();
        
        String response = "";
        switch(command){
            case "JOIN":
                response = processMsg(new Join(), command, st);
                return response;
                
            case "LEAVE":
                response = processMsg(new Leave(), command, st);
                return response;
                
            case "UPDATE_ROUTES":
                response = processMsg(new UpdateRoutes(), command, st);
                return response;
                
            case "ADD_FI":
                response = processMsg(new AddSingleFileIndex(), command, st);
                return response;
                
            case "REMOVE_FI":
                response = processMsg(new RemoveSingleFileIndex(), command, st);
                return response;
            
            case "SEND_FI_PRE":
                response = processMsg(new SendFileIndexToPre(), command, st);
                return response;
                
            case "SEND_FI_SUC":
                response = processMsg(new SendFileIndexToSuc(), command, st);
                return response;
            
            case "BACKUP_FI":
                response = processMsg(new BackupFileIndex(), command, st);
                return response;
                
            case "ADD_WI":
                response = processMsg( new AddSingleWordIndex(), command, st);
                return response;
                
            case "SEND_WI":
                response = processMsg(new SendWordIndex(), command, st);
                return response;
                
            case "BACKUP_WI":
                response = processMsg(new BackupWordIndex(), command, st);
                return response;
                
            case "SER":
                response = processMsg(new FileSearchRequest(), command, st);
                return response;
                
            case "SER_WORD":
                response = processMsg(new WordSearchRequest(), command, st);
                return response;
                
            case "SEROK":
                response = processMsg(new SearchResults(), command, st);
                return response;
                
            case "DOWN":
                response = processMsg(new DownloadRequest(), command, st);
                return response;
                
            case "ALIVE":
                response = processMsg(new Active(), command, st);
                return response;
                
            case "PRE":
                response = processMsg(new UpdatePredecessor(), command, st);
                return response;
                
            case "ISALIVE":
                response = "ALIVE";
                return response;
                
            default:
                return response;
        }
    }
    
    private String processMsg(IMessage msgHandler, String code, StringTokenizer st){
        try {
            msgHandler.handle(st);
            return code + " 0"; 
        } 
        catch (Exception e) {
            return code + " 9999";
        }

    }
  
}
