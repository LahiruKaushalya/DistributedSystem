package CS4262.Core;

import CS4262.Interfaces.IInitializerWordIndex;

/**
 *
 * @author Lahiru Kaushalya
 */
public class WordIndexInitializer implements IInitializerWordIndex{
    
    private static WordIndexInitializer instance;

    public static WordIndexInitializer getInstance() {
        if (instance == null) {
            instance = new WordIndexInitializer();
        }
        return instance;
    }

    private WordIndexInitializer() {
        
    }
}
