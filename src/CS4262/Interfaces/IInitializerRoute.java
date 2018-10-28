package CS4262.Interfaces;

import CS4262.Core.RouteInitializer;

/**
 *
 * @author Lahiru Kaushalya
 */
public interface IInitializerRoute extends IMain{
    
    public RouteInitializer routeInitializer = RouteInitializer.getInstance();
    
}
