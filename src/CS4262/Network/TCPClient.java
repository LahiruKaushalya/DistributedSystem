package CS4262.Network;

import CS4262.Interfaces.IMain;
import CS4262.Models.DataTransfer.FileDTO;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Sankaja
 */
public class TCPClient implements IMain
{
    /**
     * Client will be the message sender -> uploader of the file
     * and the server will listen -> downloader
     */
    
    private final FileDTO file ;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    
    public TCPClient(FileDTO fileTrnasferObj)
    {
        this.file = fileTrnasferObj;
    }
    
    public String sendFile() throws IOException 
    {
        startConnection(file.getReceiver().getipAdress(),file.getReceiver().getTcpPort());
        String reply = sendMessage(file.getSerializedObj());
        stopConnection();
        return reply;
    }
    
    private void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }
 
    private String sendMessage(String msg) throws IOException {
        out.println(msg);
        String resp = in.readLine();
        return resp;
    }
 
    private void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }
}
