package CS4262.Helpers;

import CS4262.Core.RouteInitializer;
import CS4262.MainController;
import CS4262.Models.Node;
import CS4262.Network.MessageSender;
import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public abstract class MsgHandler {
    
    protected final Node node;
    protected final MainController mainController;
    protected final RouteInitializer routeInitializer;
    protected final MessageSender msgSender;
    protected final IDCreator idCreator;
    
    public MsgHandler(){
        this.mainController = MainController.getInstance();
        this.node = mainController.getNode();
        this.routeInitializer = new RouteInitializer();
        this.msgSender = MessageSender.getInstance();
        this.idCreator = new IDCreator();
    }
    
    /**
     *
     * @param st
     */
    public abstract void handle(StringTokenizer st);
}
