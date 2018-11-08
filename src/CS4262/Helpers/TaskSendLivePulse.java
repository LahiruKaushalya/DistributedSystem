package CS4262.Helpers;

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
public class TaskSendLivePulse extends TimerTask implements IMain, IInitializerNode, IInitializerRoute{
    
    private final int hopCount;
    private final long delay, period;
    private final Timer timer;
    
    public TaskSendLivePulse() {
        this.delay = 30000; //30 seconds
        this.period = 30000; //30 seconds
        this.timer = new Timer();
        this.hopCount = 10;
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
            String response = new Active().send(new MessageDTO(successor, node, hopCount));
        }
    }
    
}
