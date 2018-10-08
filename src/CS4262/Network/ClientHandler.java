package CS4262.Network;

import CS4262.Helpers.IDCreator;
import CS4262.MainController;
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
    
    private final Node node;
    private final Socket socket; 
    private final IDCreator idCreator;
    private final DataInputStream inStream;
    private final DataOutputStream outStream;
    private final MainController mc;
    
    public ClientHandler(Node node, Socket socket, DataInputStream inStream, DataOutputStream outStream){
        this.node = node;
        this.socket = socket;
        this.inStream = inStream;
        this.outStream = outStream;
        this.idCreator = new IDCreator();
        this.mc = MainController.getInstance();
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
        String response = "";
        StringTokenizer st = new StringTokenizer(incomingMsg, " ");

        String length = st.nextToken();
        String command = st.nextToken();
        
        if(command.equals("JOIN")){
            try{
                joinMsgHandler(st);
            }
            catch(Exception e){
                response = "0016 JOINOK 9999";
            }
            response = "0013 JOINOK 0";
        }
        if(command.equals("LEAVE")){
            try{
                leaveMsgHandler(st);
            }
            catch(Exception e){
                response = "0017 LEAVEOK 9999";
            }
            response = "0014 LEAVEOKOK 0";
        }
        return response;
    }
    
    private void joinMsgHandler(StringTokenizer st) {
        String ip = st.nextToken();
        int port = Integer.parseInt(st.nextToken());
        String id = idCreator.generateNodeID(ip, port);
        
    }
    
    private void leaveMsgHandler(StringTokenizer st) {
        String ip = st.nextToken();
        int port = Integer.parseInt(st.nextToken());
        String id = st.nextToken();
        
    }
}
