package dao;

public class DaoFactory {

    public static UserDAO getUserDAO() {
        return UserDAO.getInstance();
    }

    public static ServiceDAO getServiceDAO() {
        return ServiceDAO.getInstance();
    }

    public static ReviewDAO getReviewDAO() {
        return ReviewDAO.getInstance();
    }
}
