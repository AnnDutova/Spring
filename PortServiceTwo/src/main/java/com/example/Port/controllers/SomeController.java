package com.example.Port.controllers;
import com.example.Port.schedule.TimeTableJSONParser;

import com.example.Port.ships.DockedShip;
import com.example.Port.threads.Statistic;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class SomeController
{
    private TimeTableJSONParser parser = new TimeTableJSONParser();
    private RestTemplate restTemplate = new RestTemplate();
    private String timeTable = null;

    SomeController()
    {
        String url = "http://localhost:8001/show";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        this.timeTable = response.getBody();
        parser.writeStringToFile(timeTable);
    }

    @GetMapping("/show")
    public ResponseEntity<String> showTimeTableInFile()
    {
        List<DockedShip> tasker = parser.readJSON(null);
        return new ResponseEntity<>(new Gson().toJson(tasker), HttpStatus.OK);
        //return new ResponseEntity<>(timeTable, HttpStatus.OK);
    }

    @GetMapping("/show_file")
    public ResponseEntity<String>  showTimeTableByPath(@RequestParam(value="path", defaultValue = "timeTable.json") String path)
    {
        TimeTableJSONParser parser = new TimeTableJSONParser();
        List<DockedShip> tasker = parser.readJSON(path);

        if (tasker == null)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File not found");
        }
        else return new ResponseEntity<>(new Gson().toJson(tasker), HttpStatus.OK);
    }

    @PostMapping(value = "/addStatistic", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> takeStatisticAndSave(@RequestBody String stata)
    {
        TimeTableJSONParser parser = new TimeTableJSONParser();
        parser.writeStatistic(stata);
        return new ResponseEntity<>(stata, HttpStatus.OK);
    }

}
