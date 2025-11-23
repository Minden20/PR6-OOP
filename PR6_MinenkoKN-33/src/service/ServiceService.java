package service;

import dao.ServiceDAO;
import entity.Service;
import java.io.IOException;
import java.util.List;


public class ServiceService {
    /**
     * Data Access Object для роботи з послугами.
     */
    private ServiceDAO serviceDAO;

    /**
     * Конструктор.
     * Ініціалізує ServiceDAO для роботи з даними.
     */
    public ServiceService() {
        this.serviceDAO = new ServiceDAO();
    }

    /**
     * Створює нову послугу з валідацією даних.
     * 
     * @param name назва послуги
     * @param description опис послуги
     * @return створена послуга або null, якщо створення не вдалося
     * @throws IllegalArgumentException якщо дані невалідні
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public Service createService(String name, String description) 
            throws IllegalArgumentException, IOException {
        // Валідація назви
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Назва послуги не може бути порожньою");
        }
        
        // Валідація опису
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Опис послуги не може бути порожнім");
        }
        
        // Створення послуги
        Service service = new Service(0, name.trim(), description.trim());
        boolean created = serviceDAO.create(service);
        
        if (created) {
            return service;
        }
        
        return null;
    }

    /**
     * Отримує послугу за ідентифікатором.
     * 
     * @param id ідентифікатор послуги
     * @return послуга або null, якщо не знайдено
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public Service getServiceById(int id) throws IOException {
        return serviceDAO.findById(id);
    }

    /**
     * Отримує всі послуги.
     * 
     * @return список всіх послуг
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public List<Service> getAllServices() throws IOException {
        return serviceDAO.findAll();
    }
}

