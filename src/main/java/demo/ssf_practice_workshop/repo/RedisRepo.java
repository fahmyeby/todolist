package demo.ssf_practice_workshop.repo;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import demo.ssf_practice_workshop.model.Todo;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;

@Repository
public class RedisRepo {
    @Autowired
    @Qualifier("redisTemplate")
    RedisTemplate<String, String> template;

    private static final String redisKey = "todos";

    // helper methods for json to object vice versa conversions

    // object to json string
    private String convertToJson(Todo todo) {
        return Json.createObjectBuilder()
                .add("id", todo.getId())
                .add("name", todo.getName())
                .add("description", todo.getDescription())
                .add("due_date", todo.getDue_date())
                .add("priority_level", todo.getPriority_level())
                .add("status", todo.getStatus())
                .add("created_at", todo.getCreated_at())
                .add("updated_at", todo.getUpdated_at())
                .build()
                .toString();
    }

    // json string to object
    private Todo convertToObject(String json) {
        try (JsonReader reader = Json.createReader(new StringReader(json))) {
            JsonObject todoObject = reader.readObject();
            Todo todo = new Todo();
            todo.setId(todoObject.getString("id"));
            todo.setName(todoObject.getString("name"));
            todo.setDescription(todoObject.getString("description"));
            todo.setDue_date(todoObject.getString("due_date"));
            todo.setPriority_level(todoObject.getString("priority_level"));
            todo.setStatus(todoObject.getString("status"));
            todo.setCreated_at(todoObject.getString("created_at"));
            todo.setUpdated_at(todoObject.getString("updated_at"));
            return todo;
        }
    }

    // save to hash - convert from object to json
    public void save(Todo todo) {
        HashOperations<String, String, String> hashOps = template.opsForHash();

        //generate id
        if (todo.getId() == null){
            todo.setId(generateUUID());
        }

        String jsonString = convertToJson(todo);
        hashOps.put(redisKey, todo.getId(), jsonString);
    }

    public void saveFromForm(String redisKey, String hashKey, String hashValue){
        template.opsForHash().put(redisKey, hashKey, hashValue);
    }

    // get all from redis - convert from json to object
    public List<Todo> getAll() {
        HashOperations<String, String, String> hashOps = template.opsForHash();
        Map<String, String> entries = hashOps.entries(redisKey);
        List<Todo> todos = new ArrayList<>();

        for (String jsonString : entries.values()) {
            todos.add(convertToObject(jsonString));
        }
        return todos;
    }

    public void delete(String id) {
        HashOperations<String, String, String> hashOps = template.opsForHash();
        hashOps.delete(redisKey, id);
    }

    public Optional<Todo> getById(String id) {
        HashOperations<String, String, String> hashOps = template.opsForHash();
        String jsonStr = hashOps.get(redisKey, id);
        
        if (jsonStr == null) {
            return Optional.empty();
        }
        
        return Optional.of(convertToObject(jsonStr));
    }

    public String generateUUID() {
        return UUID.randomUUID().toString().substring(0,20);
    }

    // get by specific id
    /* public Optional<Todo> getById(String id){
        HashOperations<String, String, String> hashOps = template.opsForHash();
        String jsonString = hashOps.get(redisKey, id);
    } */

}
