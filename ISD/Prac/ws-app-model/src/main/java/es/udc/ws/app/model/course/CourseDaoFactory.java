package es.udc.ws.app.model.course;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class CourseDaoFactory {
    private final static String CLASS_NAME_PARAMETER = "CourseDaoFactory.className";
    private static CourseDao dao = null;

    private CourseDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static CourseDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (CourseDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // Obtain unique instance
    public synchronized static CourseDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }
}

