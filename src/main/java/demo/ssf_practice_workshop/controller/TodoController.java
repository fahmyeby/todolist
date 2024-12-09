package demo.ssf_practice_workshop.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import demo.ssf_practice_workshop.model.Todo;
import demo.ssf_practice_workshop.repo.RedisRepo;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/todo")
public class TodoController {
    @Autowired
    RedisRepo repo;

    @GetMapping("")
    public String landingPage(Model model){
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("name") String name, @RequestParam("age") Integer age, HttpSession session){
        session.setAttribute("name", name);
        session.setAttribute("age", age);

        if (age >= 10){
            return "redirect:/todo/list";
        } else {
            return "underage";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.getAttribute("name");
        return "logout";
    }

    @GetMapping("/list")
    public String todoList(@RequestParam(required = false) String status, HttpSession session, Model model) {
        if (session.getAttribute("name") == null || session.getAttribute("age") == null){
            return "refused";
        }

        List<Todo> todos = repo.getAll();
        
        if (status != null && !status.isEmpty()) {
            todos = todos.stream()
                    .filter(todo -> todo.getStatus().equalsIgnoreCase(status))
                    .collect(Collectors.toList());
        }
        
        model.addAttribute("todos", todos);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("statusOptions", 
            Arrays.asList("Pending", "In_Progress", "Completed"));
        
        return "list";
    }

    @GetMapping("/create")
    public String showForm(Model model) {
        model.addAttribute("register", new Todo());
        model.addAttribute("isUpdate", false);
        return "create";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable String id, Model model) {
        Optional<Todo> todo = repo.getById(id);
        if (todo.isPresent()) {
            model.addAttribute("register", todo.get());
            model.addAttribute("isUpdate", true);
            return "create";
        }
        return "redirect:/todo/list";
    }

    @PostMapping("/save")
    public String saveTodo(@ModelAttribute("register") Todo todo) {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        // For updates, preserve created_at
        if (todo.getId() != null && !todo.getId().trim().isEmpty()) {
            Optional<Todo> existingTodo = repo.getById(todo.getId());
            if (existingTodo.isPresent()) {
                todo.setCreated_at(existingTodo.get().getCreated_at());
            }
        } else {
            // For new todos
            todo.setCreated_at(timestamp);
        }
        
        todo.setUpdated_at(timestamp);
        repo.save(todo);  // ID will be generated here for new todos
        return "redirect:/todo/list";
    }

    @PostMapping("/delete/{id}")
    public String deleteTodo(@PathVariable String id) {
        repo.delete(id);
        return "redirect:/todo/list";
    }
}