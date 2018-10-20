package CS4262.Helpers;

import java.util.StringTokenizer;

/**
 *
 * @author Lahiru Kaushalya
 */
public class SearchResultsHandler extends MsgHandler{
    
    public SearchResultsHandler() {
        super();
    }

    @Override
    public void handle(StringTokenizer st) {
        System.out.println(st);
    }
}
