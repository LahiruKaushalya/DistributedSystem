package CS4262.Network;

import CS4262.Core.RouteInitializer;
import CS4262.Helpers.IDCreator;
import CS4262.Helpers.JoinHandler;
import CS4262.Helpers.LeaveHandler;
import CS4262.Helpers.MsgHandler;
import CS4262.Helpers.RouteHandler;
import CS4262.Models.Node;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lahiru Kaushalya
 */
public class ClientHandler extends Thread{
    
    private final Socket socket;
    private final DataInputStream inStream;
    private final DataOutputStream outStream; 
    private MsgHandler msgHandler;
    
    public ClientHandler(Socket socket, DataInputStream inStream, DataOutputStream outStream){
        this.socket = socket;
        this.inStream = inStream;
        this.outStream = outStream;
    }
    
    @Override
    public void run(){
        try {
            String incomingMsg;
            incomingMsg = inStream.readUTF();
            //Process incoming message
            String response = createResponse(incomingMsg);
            outStream.writeUTF(response);

        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            try {
                inStream.close();
                outStream.close();
                socket.close();
            } 
            catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
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
                    response = "0013 JOINOK 0";
                } 
                catch (Exception ex) {
                    response = "0016 JOINOK 9999";
                }
                finally{
                    return response;
                }
                
            case "LEAVE":
                try {
                    msgHandler = new LeaveHandler();
                    msgHandler.handle(st);
                    response = "0014 LEAVEOK 0";
                } 
                catch (Exception ex) {
                    response = "0017 LEAVEOK 9999";
                }
                finally{
                    return response;
                }
                
            case "UPDATE_ROUTES":
                try {
                    msgHandler = new RouteHandler();
                    msgHandler.handle(st);
                    response = "0022 UPDATE_ROUTES 0";
                } 
                catch (Exception e) {
                    response = "0025 UPDATE_ROUTES 9999";
                }
                finally{
                    return response;
                }
            
            default:
                return response;
        }
    }
  
}
