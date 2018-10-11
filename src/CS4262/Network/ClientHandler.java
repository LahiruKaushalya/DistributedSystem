package CS4262.Network;

import CS4262.Core.RouteInitializer;
import CS4262.Helpers.IDCreator;
import CS4262.Models.Node;
import CS4262.Models.NodeDTO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
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
    private final MessageHandler messageHandler;
    private final RouteInitializer routeInitializer; 
    
    public ClientHandler(Node node, Socket socket, DataInputStream inStream, DataOutputStream outStream){
        this.node = node;
        this.socket = socket;
        this.inStream = inStream;
        this.outStream = outStream;
        this.idCreator = new IDCreator();
        this.messageHandler = MessageHandler.getInstance();
        this.routeInitializer = new RouteInitializer();
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
        if(command.equals("UPDATE_ROUTES")){
            try{
                updateRoutesMsgHandler(st);
            }
            catch(Exception e){
                response = "0025 UPDATE_ROUTESOK 9999";
            }
            response = "0023 UPDATE_ROUTESOK 0";
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
        routeInitializer.updateLocalRoutes(new NodeDTO(ip, port));
        routeInitializer.updateRoutesUI();
        
        Node successor = node.getSuccessor();
        if (successor != null) {
            messageHandler.updateRoutes(successor, node, null);
        }
    }
    
    private void updateRoutesMsgHandler(StringTokenizer st) {
        String ip = st.nextToken();
        int port = Integer.parseInt(st.nextToken());
        int routes = Integer.parseInt(st.nextToken());
        
        NodeDTO temp;
        ArrayList<Node> addi = new ArrayList<>();
        
        if(routes > 1){
            for(int i = 0; i < routes; i++){
                temp = new NodeDTO(st.nextToken(), Integer.parseInt(st.nextToken()));
                Node tempN;
                if(i != 0){
                    tempN = routeInitializer.updateLocalRoutes(temp);
                    if(tempN != null){
                        addi.add(tempN);
                    }
                }
            }
            routeInitializer.updateRoutesUI();
        }
        
        String id = node.getId();
        String senderID = idCreator.generateNodeID(ip, port);
        
        if(!id.equals(senderID)){
            Node sucessor = node.getSuccessor();
            if(sucessor != null){
                messageHandler.updateRoutes(sucessor, new NodeDTO(ip, port), addi);
            }
        }
    }
    
    private void leaveMsgHandler(StringTokenizer st) {
        String ip = st.nextToken();
        int port = Integer.parseInt(st.nextToken());
        String id = st.nextToken();
        
    }
}
