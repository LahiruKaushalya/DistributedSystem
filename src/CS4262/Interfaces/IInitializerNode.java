package CS4262.Interfaces;

import CS4262.Core.NodeInitializer;

/**
 *
 * @author Lahiru Kaushalya
 */
public interface IInitializerNode extends IMain{
    
    public NodeInitializer nodeInitializer = NodeInitializer.getInstance();
    
}
