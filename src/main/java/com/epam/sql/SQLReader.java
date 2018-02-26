package com.epam.sql;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by Almas_Doskozhin
 * on 2/12/2018.
 */
public class SQLReader {
    private static Map<String, Rate[]> rateMap = new HashMap<>();
    private static int bracketNumber;
    private static Properties prop = new Properties();
    private static boolean newBizVersion;

    public Map<String, Rate[]> getRateMap(Properties prop,
                                          String startDate,
                                          String endDate,
                                          String creationDate,
                                          int bracketNumber,
                                          String fileName,
                                          boolean newBizVersion) {
        SQLReader.prop = prop;
        SQLReader sqlReader = new SQLReader();
        sqlReader.populateRateMap(fileName, bracketNumber);
        setNewValidationDates(startDate, endDate, creationDate);
        SQLReader.newBizVersion = newBizVersion;
        return rateMap;
    }

    private void populateRateMap(String fileName, int bracketNumber) {
        //read file into stream, try-with-resources
        SQLReader.bracketNumber = bracketNumber;
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(this::parseSqlStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseSqlStr(String str) {
        String[] sqlExpressionParts = str.split("values");
        String valuesPart = sqlExpressionParts[1];
        valuesPart = valuesPart.replaceFirst("\\(", "");
        valuesPart = valuesPart.replaceFirst("\\);", "");
        String[] values = valuesPart.split(",");

        Rate rate = new Rate();
        int sqlParameterPos = 0;
        rate.setOid(values[sqlParameterPos++]);
        rate.setUpdate_user(values[sqlParameterPos++]);
        rate.setCode(values[sqlParameterPos++]);
        rate.setDescription(values[sqlParameterPos++]);

        if (!values[sqlParameterPos].equals("null")) {
            rate.setEnd_date(values[sqlParameterPos++] + "," + values[sqlParameterPos++]);
        } else {
            rate.setEnd_date(values[sqlParameterPos++]);
        }

        if (!values[sqlParameterPos].equals("null")) {
            rate.setStart_date(values[sqlParameterPos++] + "," + values[sqlParameterPos++]);
        } else {
            rate.setStart_date(values[sqlParameterPos++]);
        }

        rate.setBiz_version(values[sqlParameterPos++]);
        rate.setLog_route_ref(values[sqlParameterPos++]);
        rate.setGrid_ref(values[sqlParameterPos++]);
        rate.setService_ref(values[sqlParameterPos++]);

        rate.setRate_formula_ref(values[sqlParameterPos++]);
        rate.setCurrency_ref(values[sqlParameterPos++]);

        if (!values[sqlParameterPos].equals("null")) {
            rate.setDeletion_date(values[sqlParameterPos++] + "," + values[sqlParameterPos++]);
        } else {
            rate.setDeletion_date(values[sqlParameterPos++]);
        }

        if (!values[sqlParameterPos].equals("null")) {
            rate.setCreation_date(values[sqlParameterPos++] + "," + values[sqlParameterPos++]);
        } else {
            rate.setCreation_date(values[sqlParameterPos++]);
        }

        if (!values[sqlParameterPos].equals("null")) {
            rate.setUpdate_date(values[sqlParameterPos++] + "," + values[sqlParameterPos++]);
        } else {
            rate.setUpdate_date(values[sqlParameterPos++]);
        }

        rate.setPrev_version_id(values[sqlParameterPos++]);
        rate.setOriginal_version_id(values[sqlParameterPos++]);
        rate.setTech_version(values[sqlParameterPos++]);
        rate.setArchived(values[sqlParameterPos++]);
        rate.setRecord_version(values[sqlParameterPos++]);

        rate.setOwner_id(values[sqlParameterPos++]);
        rate.setSrv_time_qty(values[sqlParameterPos++]);
        rate.setSrv_time_unit(values[sqlParameterPos]);

        String rateCodeParts[] = rate.getCode().split("_\\[.*\\[");
        String logRoute = prop.getProperty(rate.getLog_route_ref());
        String rateBracket = rateCodeParts[1].replaceFirst("]'", "");

        if (!rateMap.containsKey(logRoute)) {
            Rate[] rates = new Rate[bracketNumber];
            rateMap.put(logRoute, rates);
        }
        rateMap.get(logRoute)[Integer.parseInt(rateBracket) - 1] = rate;
        rateMap.get(logRoute);
    }

    private void setNewValidationDates(String startDate, String endDate, String creationDate) {
        for (Rate[] rates : rateMap.values()) {
            for (int i = 0; i < bracketNumber; i++) {
                Rate rate = rates[i];
                rate.setStart_date(startDate);
                rate.setEnd_date(endDate);
                if (newBizVersion) {
                    rate.setBiz_version(String.valueOf(Integer.parseInt(rate.getBiz_version()) + 1));
                    rate.setTech_version("0");
                    rate.setRecord_version("0");
                } else {
                    rate.setTech_version(String.valueOf(Integer.parseInt(rate.getTech_version()) + 1));
                    rate.setRecord_version(String.valueOf(Integer.parseInt(rate.getRecord_version()) + 1));
                }
                rate.setPrev_version_id(rate.getOid());
                rate.setOriginal_version_id(rate.getOid());
                rate.setOid("rId");
                rate.setGrid_ref("rgId");
                rate.setCreation_date(creationDate);
            }
        }
    }
}
