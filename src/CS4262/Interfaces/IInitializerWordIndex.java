package CS4262.Interfaces;

import CS4262.Core.WordIndexInitializer;

/**
 *
 * @author Lahiru Kaushalya
 */
public interface IInitializerWordIndex extends IMain{
    
    public WordIndexInitializer wordIndexInitializer = WordIndexInitializer.getInstance();
    
}
