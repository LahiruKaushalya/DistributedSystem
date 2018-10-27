package CS4262.Core;

import CS4262.Helpers.IDCreator;
import CS4262.Helpers.RangeChecker;
import CS4262.MainController;
import CS4262.Models.Node;

/**
 *
 * @author Lahiru Kaushalya
 */
public interface Initializer {
    
    public MainController mainController = MainController.getInstance();
    public Node node = mainController.getNode();
    public IDCreator idCreator = new IDCreator();
    public RangeChecker rangeChecker = new RangeChecker();
    
}
