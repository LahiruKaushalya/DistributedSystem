package CS4262.Helpers;

import CS4262.Core.NodeInitializer;
import CS4262.Interfaces.*;
import CS4262.Message.Route.*;
import CS4262.Models.DataTransfer.MessageDTO;
import CS4262.Models.Node;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Lahiru Kaushalya
 */
public class TaskCheckSucessor extends TimerTask implements IMain, IInitializerNode, IInitializerRoute{
    
    private final int hopCount;
    private final long delay, period;
    private final Timer timer;
    
    private int retryCount;
    
    
    public TaskCheckSucessor() {
        this.delay = 10000; //10 seconds
        this.period = 10000; //10 seconds
        this.retryCount = 0;
        this.hopCount = NodeInitializer.getHopCount();
        this.timer = new Timer();
    }
    
    public void startTask(){
        timer.scheduleAtFixedRate(this, delay, period);
    }
    
    public void stopTask(){
        this.cancel();
        this.timer.cancel();
    }
    
    @Override
    public void run() {
        Node successor = node.getSuccessor();
        if (successor != null) {
            //Check successor node is alive
            String response = new UpdateSuccessor().send(new MessageDTO(successor));
            if (response == null) {
                retryCount++;
            } else {
                retryCount = 0;
            }

            if (retryCount == 2) {
                //Remove dead node from routing table
                routeInitializer.removeAndUpdate(successor);
                uiCreator.updateRoutesUI();

                //Inform neighbours 
                Node[] neighbours = node.getRoutes();
                for (Node neighbour : neighbours) {
                    if (neighbour != null) {
                        new Leave().send(new MessageDTO(neighbour, successor, hopCount));
                        new UpdateRoutes().send(new MessageDTO(neighbour, node, hopCount, null));
                    }
                }

                //Activate file index backup
                nodeInitializer.activateFileIndexBackup();

                //Activate word index backup
                nodeInitializer.activateWordIndexBackup();

                retryCount = 0;
            }
        }
    }
    
}
