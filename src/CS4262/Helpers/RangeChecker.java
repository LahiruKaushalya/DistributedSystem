package CS4262.Helpers;

/**
 *
 * @author Lahiru Kaushalya
 */
public class RangeChecker {
    
    private final int breakPoint;
    
    public RangeChecker() {
        this.breakPoint = (int)Math.pow(2, new IDCreator().getBIN_ID_LENGTH());
    }
    
    public boolean isInRange(int lowerbound, int upperbound, int value){
        if(lowerbound == 0){
            return upperbound <= value &&  value <= breakPoint;
        }
        //0 within the range
        else if(lowerbound < upperbound){
            return (0 <= value && value < lowerbound) || (upperbound <= value && value < breakPoint);
        }
        //0 outside the range
        else{
            return upperbound <= value && value < lowerbound;
        }
        
    } 
    
}
