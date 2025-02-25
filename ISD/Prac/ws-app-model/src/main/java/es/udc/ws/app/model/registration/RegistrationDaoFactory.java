package es.udc.ws.app.model.registration;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class RegistrationDaoFactory {
    private final static String CLASS_NAME_PARAMETER = "RegistrationDaoFactory.className";
    private static RegistrationDao dao = null;

    private RegistrationDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static RegistrationDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (RegistrationDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Obtain unique instance
    public synchronized static RegistrationDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }
}

