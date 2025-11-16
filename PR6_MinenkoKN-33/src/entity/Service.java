package entity;

public class Service {
    /**
     * Унікальний ідентифікатор послуги.
     */
    private int id;
    
    /**
     * Назва послуги.
     */
    private String name;
    
    /**
     * Опис послуги.
     */
    private String description;

    /**
     * Конструктор за замовчуванням.
     * Створює порожній об'єкт послуги.
     */
    public Service() {
    }

    /**
     * Конструктор з параметрами.
     * Створює послугу з вказаними даними.
     * 
     * @param id унікальний ідентифікатор послуги
     * @param name назва послуги
     * @param description опис послуги
     */
    public Service(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    /**
     * Отримує ідентифікатор послуги.
     * 
     * @return ідентифікатор послуги
     */
    public int getId() {
        return id;
    }

    /**
     * Встановлює ідентифікатор послуги.
     * 
     * @param id ідентифікатор послуги
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Отримує назву послуги.
     * 
     * @return назва послуги
     */
    public String getName() {
        return name;
    }

    /**
     * Встановлює назву послуги.
     * 
     * @param name назва послуги
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Отримує опис послуги.
     * 
     * @return опис послуги
     */
    public String getDescription() {
        return description;
    }

    /**
     * Встановлює опис послуги.
     * 
     * @param description опис послуги
     */
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Service{id=" + id + ", name='" + name + "', description='" + description + "'}";
    }
}

