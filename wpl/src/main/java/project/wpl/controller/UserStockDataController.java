package project.wpl.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.joda.time.LocalDate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
public class UserStockDataController {


    @GetMapping(value = "/currentPrice/{symbol}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public double currentValue(@PathVariable("symbol") String symbol){

        final String uri = "http://localhost:9092/currentValue/"+symbol;

        RestTemplate restTemplate = new RestTemplate();
        double result = restTemplate.getForObject(uri, Double.class);
        return result;
    }


    @GetMapping(value = "/pastWeek/{symbol}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getPastWeekData(@PathVariable("symbol") String symbol){

        final String uri = "http://localhost:9092/pastWeek/"+symbol;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        return result;
    }

    @GetMapping(value = "/currentWeek/{symbol}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getCurrentWeekData(@PathVariable("symbol") String symbol){

        final String uri = "http://localhost:9092/currentWeek/"+symbol;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        return result;
    }

    @GetMapping(value = "/currentDay/{symbol}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getCurrentDay(@PathVariable("symbol") String symbol){

        final String uri = "http://localhost:9092/currentDay/"+symbol;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        return result;
    }

    @GetMapping(value = "/monthToDate/{symbol}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getMonthToDate(@PathVariable("symbol") String symbol){

        final String uri = "http://localhost:9092/monthToDate/"+symbol;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);


        return result;
    }

    @GetMapping(value = "/yearToDate/{symbol}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getYearToDate(@PathVariable("symbol") String symbol){

        final String uri = "http://localhost:9092/yearToDate/"+symbol;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        return result;
    }

    @GetMapping(value = "/fiveYear/{symbol}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getFiveYear(@PathVariable("symbol") String symbol){

        final String uri = "http://localhost:9092/fiveYear/"+symbol;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(uri, String.class);
        return result;
    }







}
