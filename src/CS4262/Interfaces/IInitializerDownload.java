package CS4262.Interfaces;

import CS4262.Core.DownloadInitializer;

/**
 *
 * @author Lahiru Kaushalya
 */
public interface IInitializerDownload extends IMain{
    
    public DownloadInitializer downloadInitializer = DownloadInitializer.getInstance();
    
}
