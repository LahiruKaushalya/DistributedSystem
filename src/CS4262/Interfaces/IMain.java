package CS4262.Interfaces;

import CS4262.Helpers.IDCreator;
import CS4262.Helpers.RangeChecker;
import CS4262.Helpers.UICreator;
import CS4262.MainController;
import CS4262.Models.Node;

/**
 *
 * @author Lahiru Kaushalya
 */
public interface IMain {
    
    public MainController mainController = MainController.getInstance();
    public Node node = mainController.getNode();
    
    public IDCreator idCreator = new IDCreator();
    public UICreator uiCreator = new UICreator();
    public RangeChecker rangeChecker = new RangeChecker();
    
    
}
