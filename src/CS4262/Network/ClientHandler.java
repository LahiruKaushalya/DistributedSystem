package CS4262.Network;

import CS4262.Helpers.FileIndexHandler;
import CS4262.Helpers.JoinHandler;
import CS4262.Helpers.LeaveHandler;
import CS4262.Helpers.MsgHandler;
import CS4262.Helpers.RouteHandler;
import CS4262.Helpers.StateHandler;

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
public class ClientHandler extends Thread{
    
    private final DatagramPacket incoming; 
    private final DatagramSocket server;
    private MsgHandler msgHandler;
    
    public ClientHandler(DatagramPacket incoming, DatagramSocket server){
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
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
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
                    msgHandler = new JoinHandler();
                    msgHandler.handle(st);
                    response = "JOINOK 0";
                } 
                catch (Exception ex) {
                    response = "JOINOK 9999";
                }
                return response;
                
            case "LEAVE":
                try {
                    msgHandler = new LeaveHandler();
                    msgHandler.handle(st);
                    response = "LEAVEOK 0";
                } 
                catch (Exception ex) {
                    response = "LEAVEOK 9999";
                }
                return response;
                
            case "UPDATE_ROUTES":
                try {
                    msgHandler = new RouteHandler();
                    msgHandler.handle(st);
                    response = "UPDATE_ROUTES 0";
                } 
                catch (Exception e) {
                    response = "UPDATE_ROUTES 9999";
                }
                return response;
                
            case "UPDATE_INDEX":
                try {
                    msgHandler = new FileIndexHandler(incomingMsg);
                    msgHandler.handle(st);
                    response = "UPDATE_INDEX 0";
                } 
                catch (Exception e) {
                    response = "UPDATE_INDEX 9999";
                }
                return response;
                
            case "ALIVE":
                try {
                    msgHandler = new StateHandler();
                    msgHandler.handle(st);
                    response = "ALIVE 0";
                } 
                catch (Exception e) {
                    response = "ALIVE 9999";
                }
                return response;
                
            case "ISALIVE":
                response = "ALIVE";
                
            default:
                return response;
        }
    }
  
}
