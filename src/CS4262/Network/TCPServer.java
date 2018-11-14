package CS4262.Network;

import CS4262.Core.DownloadInitializer;
import CS4262.Interfaces.IInitializerDownload;
import CS4262.MainFrame;
import CS4262.Models.Node;
import CS4262.Models.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Lahiru Kaushalya
 */
public class TCPServer extends Thread implements IInitializerDownload {

    private final Node node;

    private static TCPServer instance;

    public static TCPServer getInstance(Node node) {
        if (instance == null) {
            instance = new TCPServer(node);
            instance.start();
        }
        return instance;
    }

    private TCPServer(Node node) {
        this.called = false;
        this.node = node;
    }

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String downloadedFile;
    private File initialFile;
    private boolean called;

    public void startServer(File file) throws IOException {
        this.initialFile = file;

    }

    @Override
    public void run() {
        called = true;
        while (true) {
            try {
                serverSocket = new ServerSocket(node.getTcpPort());
                clientSocket = serverSocket.accept();
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String file = in.readLine();
                JOptionPane.showMessageDialog(MainFrame.getInstance(), "file name : " + file.split(",")[2].replace("_", " ").trim());
                this.downloadedFile = file;
                stopServer();
            } catch (IOException ex) {
                Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void stopServer() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
        String body = this.downloadedFile.split(",")[0];
        this.initialFile.setBody(body);
        this.initialFile.setFileSize(Float.valueOf(this.downloadedFile.split(",")[1]));
        this.initialFile.setHashCode(DownloadInitializer.getInstance().genHash(body));
        uiCreator.displayFileContent(initialFile);
    }

}
