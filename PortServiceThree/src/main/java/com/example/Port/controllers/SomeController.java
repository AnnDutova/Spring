package com.example.Port.controllers;
import com.example.Port.schedule.TimeTableJSONParser;
import com.example.Port.services.ServiceOne;
import com.example.Port.services.ServiceThree;
import com.example.Port.services.ServiceTwo;

import com.example.Port.ships.DockedShip;
import com.example.Port.threads.Statistic;
import com.google.gson.Gson;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.Semaphore;

@RestController
public class SomeController
{
    private String fromFileTimeTable = null;
    private Statistic stata = null;
    private RestTemplate restTemplate = new RestTemplate();

    SomeController()
    {
        String url = "http://localhost:8002/show_file";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        this.fromFileTimeTable = response.getBody();
    }

    @GetMapping("/show")
    public String showTimeTableFile()
    {
        return new Gson().toJson(new TimeTableJSONParser().readJSON(fromFileTimeTable));
    }

    @PostMapping(value = "/addStatistic", produces = "application/json")
    public String collectStatistic()
    {
        Semaphore mutex = new Semaphore(1);
        ServiceThree three  = new ServiceThree(mutex);
        three.start(fromFileTimeTable);
        try
        {
            mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Statistic stats = three.sentStatistic();
        mutex.release();
        Statistic sentPart = stats.returnForJson();

        String url = "http://localhost:8002/addStatistic";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(new Gson().toJson(sentPart), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return stats.toString();
    }

}
