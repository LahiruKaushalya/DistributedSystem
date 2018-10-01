package CS4262;

/**
 *
 * @author Lahiru Kaushalya
 */
public class MainController {
    
    private static MainFrame mainFrame;

    public static void main(String args[]){
        mainFrame = new MainFrame();
        mainFrame.setVisible(true);
    }
    
    public MainFrame getMainFrame() {
        return mainFrame;
    }
}
