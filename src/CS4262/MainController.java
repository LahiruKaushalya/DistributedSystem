package CS4262;

/**
 *
 * @author Lahiru Kaushalya
 */
public class MainController {
    
    private static Node node;

    private static MainFrame mainFrame;

    public static void main(String args[]){
        mainFrame = new MainFrame();
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
}
