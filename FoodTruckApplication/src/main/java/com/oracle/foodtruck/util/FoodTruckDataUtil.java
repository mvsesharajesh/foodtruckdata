package com.oracle.foodtruck.util;

import javafx.util.converter.LocalDateTimeStringConverter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;

public class FoodTruckDataUtil {

    static Logger logger = Logger.getLogger(FoodTruckDataUtil.class);

    private static final String LIMIT="$limit";
    private static final String OFFSET="$offset";
    private static final String ORDER="$order";
    private static final String DISTINCT="$distinct";
    private static final String SELECT="$select";
    private static final String WHERE="$where";
    private static final String DAY_ORDER="dayorder";
    private static final String START_24="start24";
    private static final String END_24="end24";
    private static final String SPACE=" ";
    private static final String AMPERSAND="&";
    private static final String EQUALS="=";
    private static final String LESS_THAN="<";
    private static final String GREATER_THAN=">";
    private static final String AND="and";


    public static String buildQueryParamForTruckCount() {
        Properties properties = readProperties();
        Map<String, String> clauseParams = new HashMap<>();
        clauseParams.put(SELECT, "count(applicant)");
        return buildQueryFromMap(clauseParams).toString();
    }

    public static String buildQueryFilterParamsForTruckData(int offset) {
        StringBuilder requestData = new StringBuilder();
        Properties properties = readProperties();
        Map<String, String> clauseParams = new LinkedHashMap<>();
        clauseParams.put(OFFSET, String.valueOf(offset));
        clauseParams.put(LIMIT, String.valueOf(properties.getProperty("app.foodtruckdata.pagination.size")));
        clauseParams.put(ORDER, properties.getProperty("app.foodtruckdata.order.by.property"));
        return buildQueryFromMap(clauseParams).toString();
    }

    public static String buildParamsForTruckData() {
        StringBuilder requestData = new StringBuilder();
        Properties properties = readProperties();
        Map<String, String> params = new HashMap<>();
        params.put(DAY_ORDER, String.valueOf(LocalDate.now().getDayOfWeek().getValue() - 1));
        StringBuilder sb = buildQueryFromMap(params);
        String currentHour = String.valueOf(LocalDateTime.now().getHour());
        try {
            sb.append(AMPERSAND);
            sb.append(URLEncoder.encode(WHERE, "UTF-8"));
            sb.append(EQUALS);
            sb.append(URLEncoder.encode(START_24, "UTF-8"));
            sb.append(LESS_THAN);
            sb.append(EQUALS);
            sb.append(URLEncoder.encode("'"+currentHour+"'", "UTF-8"));
            sb.append(URLEncoder.encode(SPACE, "UTF-8"));
            sb.append(AND);
            sb.append(URLEncoder.encode(SPACE, "UTF-8"));
            sb.append(URLEncoder.encode(END_24, "UTF-8"));
            sb.append(GREATER_THAN);
            sb.append(URLEncoder.encode("'"+currentHour+"'", "UTF-8"));
        }catch(Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static StringBuilder buildQueryFromMap(Map<String, String> paramsMap) {
        StringBuilder requestData = new StringBuilder();
        try {
            for (Map.Entry<String, String> paramMap : paramsMap.entrySet()) {
                if (requestData.length() != 0) {
                    requestData.append('&');
                }
                // Encode the parameter based on the parameter map we've defined
                // and append the values from the map to form a single parameter
                requestData.append(URLEncoder.encode(String.valueOf(paramMap.getKey()), "UTF-8"));
                requestData.append('=');
                requestData.append(URLEncoder.encode(String.valueOf(paramMap.getValue()), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            logger.info("Exception occurred while encoding query params");
        } catch (Exception e) {
            logger.info("Exception occurred while encoding query params");
        }
        return requestData;
    }

    public static Properties readProperties() {
        InputStream is = null;
        Properties prop = null;
        try {
            prop = new Properties();
            is = FoodTruckDataUtil.class.getClassLoader().getResourceAsStream("config.properties");
            //is = ClassLoader.class.getResourceAsStream("../../resources/config.properties");
            prop.load(is);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.info("Exception occurred while rreading properties file");
                }
            }
        }
        return prop;
    }
}
