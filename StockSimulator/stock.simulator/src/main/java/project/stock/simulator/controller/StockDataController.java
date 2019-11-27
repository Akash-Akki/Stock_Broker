package project.stock.simulator.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import project.stock.simulator.model.SingleStock;
import project.stock.simulator.model.Stocks;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

@RestController
public class StockDataController {

    @GetMapping(value = "/pastWeek/{symbol}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getPastWeekData(@RequestParam(required = true) String symbol)  {
        int count = 1;
        Stocks stocks = new Stocks();
        List<SingleStock> list = new ArrayList<SingleStock>();
        Gson gson = new Gson();
        LocalDate now = new LocalDate();
        while(count<=7){
            if(now.getDayOfWeek() != 6 && now.getDayOfWeek() != 7) {
            String date = now.toString();
            double low = (Math.random()*((500.0-10.0)+1))+10;
            low = Math.round(low * 100.0) / 100.0;
            double high = (Math.random()*((500-low)+1))+low;
            high = Math.round(high * 100.0) / 100.0;
            double open = (Math.random()*((high-low)+1))+low;
            open = Math.round(open * 100.0) / 100.0;
            double close = (Math.random()*((high-low)+1))+low;
            close = Math.round(close * 100.0) / 100.0;
            Random r = new Random();
            int volume = r.nextInt((100000 - 0) + 1) + 0;


            SingleStock singleStock = new SingleStock();
            singleStock.setDate(date);
            singleStock.setLow(low);
            singleStock.setHigh(high);
            singleStock.setClose(close);
            singleStock.setOpen(open);
            //  singleStock.setSymbol(symbol);
            singleStock.setVolume(volume);
            list.add(singleStock);
            count++;
            }
            now = now.minusDays(1);


        }

        stocks.setStocksList(list);
        JsonElement json = gson.toJsonTree(stocks);
       /* FileWriter fileWriter = new FileWriter("output.json");
        gson.toJson(stocks, fileWriter);
        fileWriter.flush();
        fileWriter.close();*/
        //System.out.println(json.toString());
        return json.toString();

    }
    @GetMapping(value = "/currentWeek/{symbol}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getCurrentWeekData(@RequestParam(required = true) String symbol)  {
        Gson gson = new Gson();
        LocalDate now = new LocalDate();
        LocalDate prev = now.withDayOfWeek(DateTimeConstants.MONDAY);
        now = now.plusDays(1);
        System.out.println(now);
        Stocks stocks = new Stocks();
        List<SingleStock> list = new ArrayList<SingleStock>();

        while (!prev.equals(now)) {
            //System.out.println(now);
            double low = (Math.random()*((500.0-10.0)+1))+10;
            low = Math.round(low * 100.0) / 100.0;
            double high = (Math.random()*((500-low)+1))+low;
            high = Math.round(high * 100.0) / 100.0;
            double open = (Math.random()*((high-low)+1))+low;
            open = Math.round(open * 100.0) / 100.0;
            double close = (Math.random()*((high-low)+1))+low;
            close = Math.round(close * 100.0) / 100.0;
            Random r = new Random();
            int volume = r.nextInt((100000 - 0) + 1) + 0;
            String date = String.valueOf(prev);


            SingleStock singleStock = new SingleStock();
            singleStock.setDate(date);
            singleStock.setLow(low);
            singleStock.setHigh(high);
            singleStock.setClose(close);
            singleStock.setOpen(open);
            //  singleStock.setSymbol(symbol);
            singleStock.setVolume(volume);
            list.add(singleStock);

            prev = prev.plusDays(1);
        }
        stocks.setStocksList(list);
        JsonElement json = gson.toJsonTree(stocks);
       /* FileWriter fileWriter = new FileWriter("output.json");
        gson.toJson(stocks, fileWriter);
        fileWriter.flush();
        fileWriter.close();*/

        return json.toString();
    }

    @GetMapping(value = "/currentDay/{symbol}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getCurrentDay(@RequestParam(required = true) String symbol)  {
        Gson gson = new Gson();
        Stocks stocks = new Stocks();
        List<SingleStock> list = new ArrayList<SingleStock>();

        LocalDate today = new LocalDate();
        if(today.getDayOfWeek() == 6)
            today = today.minusDays(1);
        else if(today.getDayOfWeek() == 7)
            today = today.minusDays(2);

        double low = (Math.random()*((500.0-10.0)+1))+10;
        low = Math.round(low * 100.0) / 100.0;
        double high = (Math.random()*((500-low)+1))+low;
        high = Math.round(high * 100.0) / 100.0;
        double open = (Math.random()*((high-low)+1))+low;
        open = Math.round(open * 100.0) / 100.0;
        double close = (Math.random()*((high-low)+1))+low;
        close = Math.round(close * 100.0) / 100.0;
        Random r = new Random();
        int volume = r.nextInt((100000 - 0) + 1) + 0;
        String date = String.valueOf(today);

        SingleStock singleStock = new SingleStock();
        singleStock.setDate(date);
        singleStock.setLow(low);
        singleStock.setHigh(high);
        singleStock.setClose(close);
        singleStock.setOpen(open);
        //  singleStock.setSymbol(symbol);
        singleStock.setVolume(volume);
        list.add(singleStock);

        stocks.setStocksList(list);
        JsonElement json = gson.toJsonTree(stocks);
       /* FileWriter fileWriter = new FileWriter("output.json");
        gson.toJson(stocks, fileWriter);
        fileWriter.flush();
        fileWriter.close();*/

        return json.toString();
    }

    @GetMapping(value = "/monthToDate/{symbol}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getMonthToDate(@RequestParam(required = true) String symbol) {
        Gson gson = new Gson();
        Stocks stocks = new Stocks();
        List<SingleStock> list = new ArrayList<SingleStock>();

        LocalDate today = new LocalDate();
        LocalDate firstDay = today.dayOfMonth().withMinimumValue();
        while(!firstDay.equals(today)) {

            if(firstDay.getDayOfWeek() != 6 && firstDay.getDayOfWeek() != 7) {
                double low = (Math.random() * ((500.0 - 10.0) + 1)) + 10;
                low = Math.round(low * 100.0) / 100.0;
                double high = (Math.random() * ((500 - low) + 1)) + low;
                high = Math.round(high * 100.0) / 100.0;
                double open = (Math.random() * ((high - low) + 1)) + low;
                open = Math.round(open * 100.0) / 100.0;
                double close = (Math.random() * ((high - low) + 1)) + low;
                close = Math.round(close * 100.0) / 100.0;
                Random r = new Random();
                int volume = r.nextInt((100000 - 0) + 1) + 0;
                String date = String.valueOf(firstDay);

                SingleStock singleStock = new SingleStock();
                singleStock.setDate(date);
                singleStock.setLow(low);
                singleStock.setHigh(high);
                singleStock.setClose(close);
                singleStock.setOpen(open);
                //  singleStock.setSymbol(symbol);
                singleStock.setVolume(volume);
                list.add(singleStock);
            }
            firstDay = firstDay.plusDays(1);
        }
        stocks.setStocksList(list);
        JsonElement json = gson.toJsonTree(stocks);
        return json.toString();
    }



    public static void main(String[] arg){

          System.out.println(  new StockDataController().getMonthToDate("test"));

    }

}
