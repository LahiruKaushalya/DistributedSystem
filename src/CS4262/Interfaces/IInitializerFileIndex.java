package CS4262.Interfaces;

import CS4262.Core.FileIndexInitializer;

/**
 *
 * @author Lahiru Kaushalya
 */
public interface IInitializerFileIndex extends IMain{
    
    public FileIndexInitializer fileIndexInitializer = FileIndexInitializer.getInstance();
    
}
