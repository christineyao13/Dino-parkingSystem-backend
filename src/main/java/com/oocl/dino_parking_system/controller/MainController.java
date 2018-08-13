package com.oocl.dino_parking_system.controller;

import com.oocl.dino_parking_system.entitie.User;
import com.oocl.dino_parking_system.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping(path = "/")
    public String index(){
        return "Hello,Dino!";
    }

    @Autowired
    EmployeeRepository employeeRepository ;

    @GetMapping(path = "/test")
    public User getEmployees() {

        User e = new User("a", "111@as.com", "1324564", "aas", "adf");
        employeeRepository.save(e);
        User user =  employeeRepository.findById(1L).get();
        return user;
    }
}

