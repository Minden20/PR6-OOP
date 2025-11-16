package dao;

import entity.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import util.JsonFileHandler;
import util.SimpleJsonParser;
import util.SimpleJsonParser.JsonObject;

public class ServiceDAO {
    /**
     * Шлях до JSON файлу з даними послуг.
     */
    private static final String FILE_PATH = "data/services.json";
    
    /**
     * Об'єкт для роботи з JSON файлами.
     */
    private JsonFileHandler fileHandler;

    /**
     * Конструктор.
     * Ініціалізує об'єкт для роботи з файлами.
     */
    public ServiceDAO() {
        this.fileHandler = new JsonFileHandler();
    }

    /**
     * Створює нову послугу та зберігає її у файл.
     * 
     * @param service послуга для створення
     * @return true, якщо послуга успішно створена, false - інакше
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public boolean create(Service service) throws IOException {
        List<Service> services = findAll();
        service.setId(generateId(services));
        services.add(service);
        return saveAll(services);
    }

    /**
     * Знаходить послугу за ідентифікатором.
     * 
     * @param id ідентифікатор послуги
     * @return послуга з вказаним ідентифікатором або null, якщо не знайдено
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public Service findById(int id) throws IOException {
        List<Service> services = findAll();
        for (Service service : services) {
            if (service.getId() == id) {
                return service;
            }
        }
        return null;
    }

    /**
     * Отримує всі послуги з файлу.
     * 
     * @return список всіх послуг
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public List<Service> findAll() throws IOException {
        List<Service> services = new ArrayList<>();
        
        if (!fileHandler.fileExists(FILE_PATH)) {
            return services;
        }

        try {
            String content = fileHandler.readFile(FILE_PATH);
            List<JsonObject> jsonArray = SimpleJsonParser.parseArray(content);
            
            for (JsonObject jsonService : jsonArray) {
                Service service = new Service();
                Object idObj = jsonService.get("id");
                if (idObj instanceof Long) {
                    service.setId(((Long) idObj).intValue());
                } else if (idObj instanceof Integer) {
                    service.setId((Integer) idObj);
                }
                service.setName((String) jsonService.get("name"));
                service.setDescription((String) jsonService.get("description"));
                services.add(service);
            }
        } catch (Exception e) {
            throw new IOException("Помилка парсингу JSON файлу", e);
        }
        
        return services;
    }

    /**
     * Оновлює дані послуги у файлі.
     * 
     * @param service послуга з оновленими даними
     * @return true, якщо послуга успішно оновлена, false - інакше
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public boolean update(Service service) throws IOException {
        List<Service> services = findAll();
        for (int i = 0; i < services.size(); i++) {
            if (services.get(i).getId() == service.getId()) {
                services.set(i, service);
                return saveAll(services);
            }
        }
        return false;
    }

    /**
     * Видаляє послугу за ідентифікатором.
     * 
     * @param id ідентифікатор послуги для видалення
     * @return true, якщо послуга успішно видалена, false - інакше
     * @throws IOException якщо виникла помилка при роботі з файлом
     */
    public boolean delete(int id) throws IOException {
        List<Service> services = findAll();
        boolean removed = services.removeIf(service -> service.getId() == id);
        if (removed) {
            return saveAll(services);
        }
        return false;
    }

    /**
     * Зберігає список послуг у JSON файл.
     * 
     * @param services список послуг для збереження
     * @return true, якщо дані успішно збережені
     * @throws IOException якщо виникла помилка при записі файлу
     */
    private boolean saveAll(List<Service> services) throws IOException {
        List<JsonObject> jsonArray = new ArrayList<>();
        
        for (Service service : services) {
            JsonObject jsonService = new JsonObject();
            jsonService.put("id", (long) service.getId());
            jsonService.put("name", service.getName());
            jsonService.put("description", service.getDescription());
            jsonArray.add(jsonService);
        }
        
        fileHandler.writeFile(FILE_PATH, SimpleJsonParser.arrayToJsonString(jsonArray));
        return true;
    }

    /**
     * Генерує новий унікальний ідентифікатор для послуги.
     * 
     * @param services список існуючих послуг
     * @return новий унікальний ідентифікатор
     */
    private int generateId(List<Service> services) {
        if (services.isEmpty()) {
            return 1;
        }
        int maxId = services.stream().mapToInt(Service::getId).max().orElse(0);
        return maxId + 1;
    }
}

