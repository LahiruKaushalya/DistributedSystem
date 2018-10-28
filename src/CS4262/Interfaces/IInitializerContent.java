package CS4262.Interfaces;

import CS4262.Core.ContentInitializer;

/**
 *
 * @author Lahiru Kaushalya
 */
public interface IInitializerContent extends IMain{
    
    public ContentInitializer contentInitializer = ContentInitializer.getInstance();
    
}
