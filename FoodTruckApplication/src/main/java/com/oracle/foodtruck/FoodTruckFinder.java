package com.oracle.foodtruck;

import com.oracle.foodtruck.service.FoodTruckService;
import java.util.*;

import com.oracle.foodtruck.util.FoodTruckDataUtil;
import org.apache.log4j.Logger;

public class FoodTruckFinder {
    static Logger logger = Logger.getLogger(FoodTruckFinder.class);

    public static void main(String... args) {
        FoodTruckService fts = new FoodTruckService();
        Properties properties = FoodTruckDataUtil.readProperties();
        int offset = 0;
        int defaultSize = 10;
        int count = fts.getTotalRecordsCount();
        if(properties != null &&
                properties.getProperty("app.foodtruckdata.pagination.size") != null) {
            defaultSize = Integer.valueOf(properties.getProperty("app.foodtruckdata.pagination.size"));
        }
        int maxPages = count/defaultSize;
        String s;
        if(count == 0) {
            logger.debug("Food truck data is empty at the current moment");
            throw new RuntimeException("Food truck data is empty at the current moment");
        }
        try {
            Scanner scanner = new Scanner(System.in);
            do {
                fts.getFoodTruckData(offset++);
                System.out.println("Press Enter key to get next set of records...");
            } while ((s = scanner.nextLine()) != null && offset < maxPages);
        }catch(InputMismatchException ime) {
            System.out.println("Please only enter key");
        } catch(Exception e) {
            System.out.println("Exception occurred "+e.getMessage());
        }
    }
}