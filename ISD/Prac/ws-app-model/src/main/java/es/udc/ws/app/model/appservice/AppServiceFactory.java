package es.udc.ws.app.model.appservice;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class AppServiceFactory {

    private final static String CLASS_NAME_PARAMETER = "AppServiceFactory.className";
    private static AppService appService = null;

    private AppServiceFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static AppService getInstance() {
        try {
            String appServiceClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
            if (appServiceClassName == null) {
                throw new RuntimeException("Parameter 'AppServiceFactory.className' is not correctly configured");
            }
            Class appServiceClass = Class.forName(appServiceClassName);
            return (AppService) appServiceClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Obtain unique instance
    public synchronized static AppService getService() {

        if (appService == null) {
            appService = getInstance();
        }
        return appService;
    }
}