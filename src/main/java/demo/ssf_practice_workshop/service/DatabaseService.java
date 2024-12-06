package demo.ssf_practice_workshop.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;


import demo.ssf_practice_workshop.model.Todo;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class DatabaseService {
    public List<Todo> readFile(String fileName) throws FileNotFoundException{
        List<Todo> todoList = new ArrayList<>();

        InputStream inputStream = new FileInputStream(fileName);
        JsonReader jsonReader = Json.createReader(inputStream);
        JsonArray jsonArray = jsonReader.readArray();

        for (int i = 0; i < jsonArray.size(); i++){
            JsonObject todoObject = jsonArray.getJsonObject(i);
            Todo todo = new Todo();

            todo.setId(todoObject.getString("id"));
            todo.setName(todoObject.getString("name"));
            todo.setDescription(todoObject.getString("description"));
            todo.setDue_date(todoObject.getString("due_date"));
            todo.setPriority_level(todoObject.getString("priority_level"));
            todo.setStatus(todoObject.getString("status"));
            todo.setCreated_at(todoObject.getString("created_at"));
            todo.setUpdated_at(todoObject.getString("updated_at"));
            todoList.add(todo);
        }
        return todoList;
    }
    
}
