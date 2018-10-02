package CS4262;

/**
 *
 * @author Lahiru Kaushalya
 */
public class MainController {
    
    private String ipAddress;
    private String username;
    private int port;
    private static MainFrame mainFrame;

    public static void main(String args[]){
        mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }
    
    public MainController(){
        this.ipAddress = "";
        this.username = "";
        this.port = 0;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
    
    public MainFrame getMainFrame() {
        return mainFrame;
    }
}
