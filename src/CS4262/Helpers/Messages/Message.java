package CS4262.Helpers.Messages;

import CS4262.Core.ContentInitializer;
import CS4262.Core.RouteInitializer;
import CS4262.Core.SearchInitializer;
import CS4262.Helpers.IDCreator;
import CS4262.MainController;
import CS4262.Models.Node;
import CS4262.Network.MessageSender;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public interface Message {
    
    public MainController mainController = MainController.getInstance();
    public Node node = mainController.getNode();
    public RouteInitializer routeInitializer = RouteInitializer.getInstance();
    public ContentInitializer contentInitializer = ContentInitializer.getInstance();
    public SearchInitializer searchInitializer = SearchInitializer.getInstance();
    public MessageSender msgSender = MessageSender.getInstance();
    public IDCreator idCreator = new IDCreator();
    
    /**
     *
     * @param st
     */
    public void handle(StringTokenizer st);
    
    public String createMsg();
    
}
