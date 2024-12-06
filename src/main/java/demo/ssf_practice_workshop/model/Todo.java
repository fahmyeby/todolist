package demo.ssf_practice_workshop.model;

public class Todo {
    private String id;
    private String name;
    private String description;
    private String due_date;
    private String priority_level;
    private String status;
    private String created_at;
    private String updated_at;
    public Todo() {
    }
    public Todo(String id, String name, String description, String due_date, String priority_level, String status,
            String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.due_date = due_date;
        this.priority_level = priority_level;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDue_date() {
        return due_date;
    }
    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }
    public String getPriority_level() {
        return priority_level;
    }
    public void setPriority_level(String priority_level) {
        this.priority_level = priority_level;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getCreated_at() {
        return created_at;
    }
    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
    public String getUpdated_at() {
        return updated_at;
    }
    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
    @Override
    public String toString() {
        return id + ", " + name + ", " + description + ", " + due_date
                + ", " + priority_level + ", " + status + ", " + created_at
                + ", " + updated_at;
    }

    
    
}
