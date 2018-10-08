package CS4262.Core;

import CS4262.MainController;
import CS4262.Models.Node;
import CS4262.Models.NodeDTO;

/**
 *
 * @author Lahiru Kaushalya
 */
public class RouteInitializer {
    
    private final MainController mainController;
    private final Node node;
    
    public RouteInitializer(){
        this.mainController = MainController.getInstance();
        this.node = mainController.getNode();
    }
    
    public void updateRoutes(NodeDTO neighbour){
        
    }
}
