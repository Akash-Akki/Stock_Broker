package project.wpl.simulation;

import com.google.gson.JsonElement;
import org.joda.time.DateTime;
import com.google.gson.Gson;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulateApp {

    public String weeklyData() throws IOException {
        int count = 1;
        Stocks stocks = new Stocks();
        List<SingleStock> list = new ArrayList<SingleStock>();
        Gson gson = new Gson();
        while(count<=7){

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
            DateTime dateTime = new DateTime();
            String date = String.valueOf(dateTime.minusDays(count));
            count++;

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
        stocks.setStocksList(list);
        JsonElement json = gson.toJsonTree(stocks);
        FileWriter fileWriter = new FileWriter("output.json");
        gson.toJson(stocks, fileWriter);
        fileWriter.flush();
        fileWriter.close();
        //System.out.println(json.toString());
        return json.toString();

    }

    public static void main(String[] arg){
        try {
            new SimulateApp().weeklyData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
