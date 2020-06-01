package com.oracle.foodtruck.service;

import com.google.gson.Gson;
import com.oracle.foodtruck.dto.FoodTruckCount;
import com.oracle.foodtruck.dto.FoodTruckData;
import com.oracle.foodtruck.util.FoodTruckDataUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;

public class FoodTruckService {

    static Logger logger = Logger.getLogger(FoodTruckService.class);

    public int getTotalRecordsCount() {
        HttpURLConnection conn;
        FoodTruckCount[] foodTruckCount;
        int count = 0;
        StringBuilder result = new StringBuilder();
        String params = FoodTruckDataUtil.buildQueryParamForTruckCount();
        Properties properties = FoodTruckDataUtil.readProperties();
        URL url = null;
        BufferedReader br = null;
        String line;
        Properties prop = FoodTruckDataUtil.readProperties();
        try
        {
            url = new URL(prop.getProperty("app.foodtruckdata.url") + "?" + params);
            logger.debug("URL for getting total food truck count data is ===>" + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //Setting timeout for connection
            conn.setConnectTimeout(Integer.valueOf(properties.getProperty("app.foodtrucldata.timeout.connection")));
            //Setting timeout for reading data
            conn.setReadTimeout(Integer.valueOf(properties.getProperty("app.foodtruckdata.timeout.read")));
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Error occured while fetching the food truck details: " + conn
                        .getResponseCode() + " " + conn.getResponseMessage());
            }
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            logger.debug("Data from Food Truck Service is===>"+result);
            conn.disconnect();
            Gson gson = new Gson();
            foodTruckCount = gson.fromJson(result.toString(), FoodTruckCount[].class);
            if (foodTruckCount != null && foodTruckCount.length != 0) {
                return foodTruckCount[0].getCount_applicant();
            }
        } catch (MalformedURLException malformedURLException) {
            logger.debug("Exception occurred while constructing url for getTotalRecordsCount API");
        } catch(IOException ioException) {
            logger.debug("Exception occurred while reading data from food truck rest api");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.debug("Exception occurred while closing bufferrd reader");
                }
            }
        }
        return count;
    }

    public void getFoodTruckData(int offset) {
        HttpURLConnection conn;
        FoodTruckData[] fdtList = null;
        URL url = null;
        BufferedReader br = null;
        String line;
        StringBuilder result = new StringBuilder();
        Properties prop = FoodTruckDataUtil.readProperties();
        String filterParams = FoodTruckDataUtil.buildQueryFilterParamsForTruckData(offset);
        String queryParams = FoodTruckDataUtil.buildParamsForTruckData();
        try
        {
            url = new URL(prop.getProperty("app.foodtruckdata.url") + "?" +  queryParams + "&" + filterParams);
            logger.debug("URL===>" + url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(60000);
            conn.setReadTimeout(60000);
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Error occured while fetching the food truck details: " + conn
                        .getResponseCode() + " " + conn.getResponseMessage());
            }
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            Gson gson = new Gson();
            fdtList = gson.fromJson(result.toString(), FoodTruckData[].class);
            System.out.println("Name" + "\t\t\t\t" + "Address");
            for (FoodTruckData fdt : fdtList) {
                System.out.println(fdt.getApplicant() + "\t\t" + fdt.getLocation());
            }
        } catch (MalformedURLException malformedURLException) {
            logger.debug("Exception occurred while constructing url for getTotalRecordsCount API");
        } catch(IOException ioException) {
            logger.debug("Exception occurred while reading data from food truck rest api");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.debug("Exception occurred while closing bufferrd reader");
                }
            }
        }
    }
}
