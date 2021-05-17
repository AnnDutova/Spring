package com.example.Port.controllers;
import com.example.Port.schedule.TimeTable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SomeController
{
    @GetMapping("/show")
    public String showTimeTable()
    {
        return new TimeTable().returnJsonObject();
    }
}
