package CS4262;

import CS4262.Models.Node;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

/**
 *
 * @author Lahiru Kaushalya
 */
public class MainController {
    
    private static Node node;
    private static MainFrame mainFrame;
    private static MainController instance;
    private static Properties prop;
    
    public static MainController getInstance() {
        if(instance == null){
            instance = new MainController();
        }
        return instance;
    }
      
    private MainController(){}
    
    public static void main(String args[]) throws IOException{
        InputStream input = null;
        try {
            prop = new Properties();
            input = new FileInputStream("conf.properties");
            prop.load(input);
            
        } 
        catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } 
        finally {
            try {
                input.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        MainController.mainFrame = MainFrame.getInstance();
        mainFrame.setVisible(true);
    }
    
    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
    
    public MainFrame getMainFrame() {
        return mainFrame;
    }
    
    public static Properties getProp() {
        return prop;
    }
    
}
