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
    private static Map<String,Rate[]> rateMap = new HashMap<>();
    private static int bracketNumber = 0;
    private static Properties prop = new Properties();

    public void populateRateMap(String fileName, int bracketNumber){
        //read file into stream, try-with-resources
        this.bracketNumber = bracketNumber;
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            stream.forEach(str -> parseSqlStr(str));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String,Rate[]> getRateMap (Properties prop ) {
        this.prop = prop;
        SQLReader sqlReader = new SQLReader();
        int bracketNumber = 2;
        sqlReader.populateRateMap("e://converter/RateGridConverter/src/main/resources/urgent3.sql", bracketNumber);

        String startDate =  "to_date('01-JUL-18 00:00','DD-MON-RR HH24:MI')";
        String endDate =    "to_date('31-DEC-99 23:59','DD-MON-RR HH24:MI')";
        String creationDate =    "to_date('14-FEB-18 00:00','DD-MON-RR HH24:MI')";
        setNewValidationDates(startDate, endDate, creationDate);
        return rateMap;
    }

    public void parseSqlStr(String str){
        String[] sqlExpressionParts = str.split("values");
        String valuesPart = sqlExpressionParts[1];
        valuesPart = valuesPart.replaceFirst("\\(", "");
        valuesPart = valuesPart.replaceFirst("\\);", "");
        String[] values = valuesPart.split(",");

        Rate rate = new Rate();
        rate.setOid(values[0]);
        rate.setUpdate_user(values[1]);
        rate.setCode(values[2]);
        rate.setDescription(values[3]);
        rate.setEnd_date(values[4] + "," + values[5]);

        rate.setStart_date(values[6]+ "," + values[7]);
        rate.setBiz_version(values[8]);
        rate.setLog_route_ref(values[9]);
        rate.setGrid_ref(values[10]);
        rate.setService_ref(values[11]);

        rate.setRate_formula_ref(values[12]);
        rate.setCurrency_ref(values[13]);
        rate.setDeletion_date(values[14]);
        rate.setCreation_date(values[15] +","+ values[16]);
        rate.setUpdate_date(values[17]);

        rate.setPrev_version_id(values[18]);
        rate.setOriginal_version_id(values[19]);
        rate.setTech_version(values[20]);
        rate.setArchived(values[21]);
        rate.setRecord_version(values[22]);

        rate.setOwner_id(values[23]);
        rate.setSrv_time_qty(values[24]);
        rate.setSrv_time_unit(values[25]);

        String rateCodeParts[] = rate.getCode().split("_\\[.*\\[");
        String logRoute = prop.getProperty(rate.getLog_route_ref());
        String rateBracket = rateCodeParts[1].replaceFirst("]'","");

        if (!rateMap.containsKey(logRoute)){
            Rate[] rates = new Rate[bracketNumber];
            rateMap.put(logRoute, rates);
        }
        rateMap.get(logRoute)[Integer.parseInt(rateBracket) - 1] = rate;
        rateMap.get(logRoute);
    }

    public static void setNewValidationDates(String startDate, String endDate, String creationDate){
        for(Rate[] rates: rateMap.values()){
            for(int i=0; i < bracketNumber; i++) {
                Rate rate = rates[i];
                rate.setStart_date(startDate);
                rate.setEnd_date(endDate);
rate.setBiz_version(String.valueOf(Integer.parseInt(rate.getBiz_version())+1));
//                rate.setTech_version(String.valueOf(Integer.parseInt(rate.getTech_version())+1));
//                rate.setRecord_version(String.valueOf(Integer.parseInt(rate.getRecord_version())+1));
                rate.setRecord_version("0");
                rate.setPrev_version_id(rate.getOid());
                rate.setOriginal_version_id(rate.getOid());
//                rate.setOriginal_version_id("rId");
                rate.setOid("rId");
                rate.setGrid_ref("rgId");
                rate.setCreation_date(creationDate);
            }
        }
    }
}
