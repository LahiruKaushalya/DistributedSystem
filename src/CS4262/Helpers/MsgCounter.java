package CS4262.Helpers;

import CS4262.Interfaces.IMain;

/**
 *
 * @author Lahiru Kaushalya
 */
public class MsgCounter implements IMain{
    
    private static int inMsgCount;
    private static int outMsgCount;
    private static MsgCounter instance;
    
    public static MsgCounter getInstance(){
        if(instance == null){
            instance = new MsgCounter();
        }
        return instance;
    }
    
    private MsgCounter() {
        MsgCounter.inMsgCount = 0;
        MsgCounter.outMsgCount = 0;
    }

    public int getInMsgCount() {
        return inMsgCount;
    }

    public int getOutMsgCount() {
        return outMsgCount;
    }

    public void resetInMsgCount() {
        MsgCounter.inMsgCount = 0;
        uiCreator.updateInMsgCount("0");
    }

    public void resetOutMsgCount() {
        MsgCounter.outMsgCount = 0;
        uiCreator.updateOutMsgCount("0");
    }
    
    public void incInMsgCount() {
        MsgCounter.inMsgCount++;
        uiCreator.updateInMsgCount(""+inMsgCount);
    }

    public void incOutMsgCount() {
        MsgCounter.outMsgCount++;
        uiCreator.updateOutMsgCount(""+outMsgCount);
    }
    
}
