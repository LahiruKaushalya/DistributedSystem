package CS4262.Network;

import CS4262.Helpers.Messages.*;

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
    private Message msgHandler;
    
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
                try {
                    msgHandler = new JoinMsg();
                    msgHandler.handle(st);
                    response = "JOINOK 0";
                } 
                catch (Exception ex) {
                    response = "JOINOK 9999";
                }
                return response;
                
            case "LEAVE":
                try {
                    msgHandler = new Leave();
                    msgHandler.handle(st);
                    response = "LEAVEOK 0";
                } 
                catch (Exception ex) {
                    response = "LEAVEOK 9999";
                }
                return response;
                
            case "UPDATE_ROUTES":
                try {
                    msgHandler = new UpdateRoutes();
                    msgHandler.handle(st);
                    response = "UPDATE_ROUTES 0";
                } 
                catch (Exception e) {
                    response = "UPDATE_ROUTES 9999";
                }
                return response;
                
            case "ADD_FILE_INDEX":
                try {
                    msgHandler = new AddSingleFileIndex();
                    msgHandler.handle(st);
                    response = "ADD_FILE_INDEX 0";
                } 
                catch (Exception e) {
                    response = "ADD_FILE_INDEX 9999";
                }
                return response;
            
            case "REMOVE_FILE_INDEX":
                try {
                    msgHandler = new RemoveSingleFileIndex();
                    msgHandler.handle(st);
                    response = "REMOVE_FILE_INDEX 0";
                } 
                catch (Exception e) {
                    response = "REMOVE_FILE_INDEX 9999";
                }
                return response;
            
            case "SEND_FILE_INDEX":
                try {
                    msgHandler = new SendFileIndex();
                    msgHandler.handle(st);
                    response = "SEND_FILE_INDEX 0";
                } 
                catch (Exception e) {
                    response = "SEND_FILE_INDEX 9999";
                }
                return response;
              
            case "BACKUP_FILE_INDEX":
                try {
                    msgHandler = new BackupFileIndex();
                    msgHandler.handle(st);
                    response = "BACKUP_FILE_INDEX 0";
                } 
                catch (Exception e) {
                    response = "BACKUP_FILE_INDEX 9999";
                }
                return response;
                
            case "SER":
                try {
                    msgHandler = new SearchRequest();
                    msgHandler.handle(st);
                    response = "SEROK 0";
                } 
                catch (Exception e) {
                    response = "SEROK 9999";
                }
                return response;
                
            case "SEROK":
                try {
                    msgHandler = new SearchResults();
                    msgHandler.handle(st);
                    response = "OK";
                } 
                catch (Exception e) {}
                return response;
                
            case "ALIVE":
                try {
                    msgHandler = new Active();
                    msgHandler.handle(st);
                    response = "ALIVE 0";
                } 
                catch (Exception e) {
                    response = "ALIVE 9999";
                }
                return response;
                
            case "PRE":
                try {
                    msgHandler = new UpdatePredecessor();
                    msgHandler.handle(st);
                    response = "PRE 0";
                } 
                catch (Exception e) {
                    response = "PRE 9999";
                }
                return response;
                
            case "ISALIVE":
                response = "ALIVE";
                
            default:
                return response;
        }
    }
  
}
