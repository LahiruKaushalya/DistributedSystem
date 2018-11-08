package CS4262.Core;

import CS4262.Interfaces.IInitializerDownload;

/**
 *
 * @author Lahiru Kaushalya
 */
public class DownloadInitializer implements IInitializerDownload{
    
    private static DownloadInitializer instance;

    public static DownloadInitializer getInstance() {
        if (instance == null) {
            instance = new DownloadInitializer();
        }
        return instance;
    }

    private DownloadInitializer() {}
}
