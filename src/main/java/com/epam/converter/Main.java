package com.epam.converter;

import com.epam.sql.Rate;
import com.epam.sql.SQLReader;
import com.epam.xlsreader.XLSReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


public class Main {
    private static Map<String, double[]> coeffMap = new HashMap<>();
    private static Map<String,Rate[]> rateMap = new HashMap<>();
    private static Properties prop = new Properties();
    private static Properties prop2 = new Properties();
    private static int MAX_EXISTING_ROW = 47;
    private static int globalBracketNumber = 2;


    public static String FILE_NAME;

    public static void main(String[] args) {
        //params
//        String startDate =  "to_date('01-FEB-18 00:00','DD-MON-RR HH24:MI')";
//        String endDate =    "to_date('31-DEC-99 23:59','DD-MON-RR HH24:MI')";
//        String creationDate =    "to_date('26-FEB-18 00:00','DD-MON-RR HH24:MI')";
//        int bracketNumber = 3;
//        String fileName = "e://converter/RateGridConverter/src/main/resources/sql_RateGrid_LumpsumS_3_F44_Chalavan_S.sql";
//        boolean isTwoCoefficients = false;
//        String resultFileName = "RateGrid_LumpsumS_3_F44_Chalavan_S.sql";
        //params

        //params
//        String startDate =  "to_date('01-FEB-18 00:00','DD-MON-RR HH24:MI')";
//        String endDate =    "to_date('31-DEC-99 23:59','DD-MON-RR HH24:MI')";
//        String creationDate =    "to_date('26-FEB-18 00:00','DD-MON-RR HH24:MI')";
//        int bracketNumber = 2;
//        String fileName = "e://converter/RateGridConverter/src/main/resources/sql_RateGrid_Bulk_F25T+P.JPL_POLL_KDE.sql";
//        boolean isTwoCoefficients = true;
//        String resultFileName = "RateGrid_Bulk_F25T+P.JPL_POLL_KDE.sql";
        //params


//        //params
//        FILE_NAME = "tRateGrid_Bulk_F25T+P.JPL_NDThier.xlsx";
//        String startDate =  "to_date('01-FEB-18 00:00','DD-MON-RR HH24:MI')";
//        String endDate =    "to_date('31-DEC-99 23:59','DD-MON-RR HH24:MI')";
//        String creationDate =    "to_date('26-FEB-18 00:00','DD-MON-RR HH24:MI')";
//        int bracketNumber = 2;
//        String fileName = "e://converter/RateGridConverter/src/main/resources/sql_RateGrid_Bulk_F25T+P.JPL_NDThier.sql";
//        boolean isTwoCoefficients = true;
//        String resultFileName = "RateGrid_Bulk_F25T+P.JPL_NDThier.sql";
//        //params

        //params
        FILE_NAME = "tRoutes RatesGrid EU_KFR_S (4).xlsx";
        String startDate =  "null";
        String endDate =    "null";
        String creationDate =    "to_date('26-FEB-18 00:00','DD-MON-RR HH24:MI')";
        int bracketNumber = 1;
        String fileName = "e://converter/RateGridConverter/src/main/resources/sql_Routes RatesGrid EU_KFR_S (4).sql";
        boolean isTwoCoefficients = false;
        String resultFileName = "Routes RatesGrid EU_KFR_S (4).sql";
        //params


        createLogRouteCache();
        XLSReader reader = new XLSReader();
        SQLReader sqlReader = new SQLReader();
        coeffMap = reader.readXls();
        rateMap = sqlReader.getRateMap(prop, startDate, endDate, creationDate, bracketNumber, fileName);
        StringBuilder stringBuilder = new StringBuilder();

        Rate examplarRate = new Rate();

        try {
            examplarRate = rateMap.values().iterator().next()[0].clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }

        examplarRate.setPrev_version_id("null");
        examplarRate.setOriginal_version_id("null");
        examplarRate.setTech_version("0");
        examplarRate.setRecord_version("0");


        int row = MAX_EXISTING_ROW;

        for(String logRoute :coeffMap.keySet()) {
            double[] coeffValues = coeffMap.get(logRoute);
            if(rateMap.containsKey(logRoute)) {
                globalBracketNumber = rateMap.get(logRoute).length;
                for (int i = 0; i < globalBracketNumber; i++) {
                    createRateWithDependentTables(stringBuilder, rateMap.get(logRoute)[i], i, isTwoCoefficients);
                }
            } else {
                row = row + 1;
                for (int i = 0; i < globalBracketNumber; i++) {
                    examplarRate.setLog_route_ref(prop2.getProperty(logRoute));
                    createNewRate(stringBuilder, row , i, examplarRate ,isTwoCoefficients);
                }
            }
        }


        try(PrintWriter writer = new PrintWriter(resultFileName, "UTF-8")) {
            writer.println(stringBuilder.toString());
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }

    private static void createLogRouteCache() {
        InputStream input = null;
        InputStream input2 = null;

        try {

            String filename = "logRouteCache.properties";
            input = Main.class.getClassLoader().getResourceAsStream(filename);
            if(input==null){
                System.out.println("Sorry, unable to find " + filename);
                return;
            }

            //load a properties file from class path, inside static method
            prop.load(input);


            String filename2 = "inverseLogRouteCache.properties";
            input2 = Main.class.getClassLoader().getResourceAsStream(filename2);
            if(input2==null){
                System.out.println("Sorry, unable to find " + filename2);
                return;
            }

            //load a properties file from class path, inside static method
            prop2.load(input2);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally{
            if(input!=null){
                try {
                    input.close();
                    input2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    ///CONDOFUSE CAN VARY!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public static void createRateWithDependentTables(StringBuilder stringBuilder, Rate rate, int bracketNumber, boolean twoCoeffs){
        stringBuilder.append(
                "rId := rId + 1;\n" +
                        "coeff := coeff + 1;\n" +
                        "rMsId := rMsId + 1;");
        stringBuilder.append("\n");
        stringBuilder.append(rate.toString());
        stringBuilder.append("\n");
        stringBuilder.append(
                "Insert into MEASURE_VALUE (OID,UNIT_REF,DIMENSION_REF) values (rMsId,33,360);\n" +
                        "Insert into RATE_MEASVALUE (RATE_REF,MEASURE_VALUE_REF) values (rId,rMsId);\n" +
                        "Insert into RATE_CONTRACT (CONTRACT_REF,RATE_REF) values (CONTRACTID,rId);\n" +
                        "rCou := rCou + 1;\n" +
                        "Insert into RATE_CONDOFUSE (RATE_REF,COND_OF_USE_REF,OID,OPERATOR) values (rId,8355,rCou,'IS_EQUAL');\n" +
                        "rCou := rCou + 1;\n" +
                        "Insert into RATE_CONDOFUSE (RATE_REF,COND_OF_USE_REF,OID,OPERATOR) values (rId,8350,rCou,'IS_EQUAL');\n" +
                        "Insert into RATE_BRACKET (RATE_REF,BRACKET_REF) values (rId,br"+bracketNumber+");\n"
        );
/////CHange
        String logRoute = prop.getProperty(rate.getLog_route_ref());
        if(!twoCoeffs) {
            double coeffValue = coeffMap.get(logRoute)[bracketNumber];
            stringBuilder.append("Insert into COEFFICIENT_VALUE (OID,LABEL,RATE_REF,RATE_TEMPLATE_REF,VALUE,SMC3_ENABLED,SMC3_TARIFF_NAME,SMC3_EFFECTIVE_DATE) " +
                    "values (SEQ_COVA_COEFFICIENT_VALUE_OID.nextval,'C0',rId,null," + coeffValue + ",0,null,null);");
            stringBuilder.append("\n");
        } else {
            double coeffValue = coeffMap.get(logRoute)[bracketNumber*2];
            stringBuilder.append("Insert into COEFFICIENT_VALUE (OID,LABEL,RATE_REF,RATE_TEMPLATE_REF,VALUE,SMC3_ENABLED,SMC3_TARIFF_NAME,SMC3_EFFECTIVE_DATE) " +
                    "values (coeff,'C0',rId,null," + coeffValue + ",0,null,null);");
            stringBuilder.append("\n");
            stringBuilder.append("coeff := coeff + 1;");
            stringBuilder.append("\n");
            double coeffValue2 = coeffMap.get(logRoute)[bracketNumber*2+1];
            stringBuilder.append("Insert into COEFFICIENT_VALUE (OID,LABEL,RATE_REF,RATE_TEMPLATE_REF,VALUE,SMC3_ENABLED,SMC3_TARIFF_NAME,SMC3_EFFECTIVE_DATE) " +
                    "values (coeff,'C1',rId,null," + coeffValue2 + ",0,null,null);");
            stringBuilder.append("\n");

        }
        stringBuilder.append("\n");
    }

    public static void createNewRate(StringBuilder stringBuilder,int rowNum, int bracketNumber, Rate rate2, boolean twoCoeffs) {
        Rate rate = new Rate();
        try {
            rate = rate2.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        stringBuilder.append(
                "rId := rId + 1;\n" +
                "coeff := coeff + 1;\n" +
                "rMsId := rMsId + 1;");
        stringBuilder.append("\n");
        String origCode = rate.getCode();
        int columnNum = bracketNumber + 1;
        rate.setCode("'"+rate.getDescription().replaceAll("'","")+"_["+rowNum+"]["+columnNum+"]" + "'");
        stringBuilder.append(rate.toString());
        stringBuilder.append("\n");
//        rate.setCode(origCode);
        stringBuilder.append(
                "Insert into MEASURE_VALUE (OID,UNIT_REF,DIMENSION_REF) values (rMsId,33,360);\n" +
                        "Insert into RATE_MEASVALUE (RATE_REF,MEASURE_VALUE_REF) values (rId,rMsId);\n" +
                        "rCou := rCou + 1;\n" +
                        "Insert into RATE_CONDOFUSE (RATE_REF,COND_OF_USE_REF,OID,OPERATOR) values (rId,8355,rCou,'IS_EQUAL');\n" +
                        "rCou := rCou + 1;\n" +
                        "Insert into RATE_CONDOFUSE (RATE_REF,COND_OF_USE_REF,OID,OPERATOR) values (rId,8350,rCou,'IS_EQUAL');\n" +
                        "Insert into RATE_BRACKET (RATE_REF,BRACKET_REF) values (rId,br"+bracketNumber+");\n"
        );
/////CHange
        String logRoute = prop.getProperty(rate.getLog_route_ref());
        if(!twoCoeffs) {
            double coeffValue = coeffMap.get(logRoute)[bracketNumber];
            stringBuilder.append("Insert into COEFFICIENT_VALUE (OID,LABEL,RATE_REF,RATE_TEMPLATE_REF,VALUE,SMC3_ENABLED,SMC3_TARIFF_NAME,SMC3_EFFECTIVE_DATE) " +
                    "values (coeff.nextval,'C0',rId,null," + coeffValue + ",0,null,null);");
            stringBuilder.append("\n");
        } else {
            double coeffValue = coeffMap.get(logRoute)[bracketNumber*2];
            stringBuilder.append("Insert into COEFFICIENT_VALUE (OID,LABEL,RATE_REF,RATE_TEMPLATE_REF,VALUE,SMC3_ENABLED,SMC3_TARIFF_NAME,SMC3_EFFECTIVE_DATE) " +
                    "values (coeff,'C0',rId,null," + coeffValue + ",0,null,null);");
            stringBuilder.append("\n");
            stringBuilder.append("coeff := coeff + 1;");
            stringBuilder.append("\n");
            double coeffValue2 = coeffMap.get(logRoute)[bracketNumber*2+1];
            stringBuilder.append("Insert into COEFFICIENT_VALUE (OID,LABEL,RATE_REF,RATE_TEMPLATE_REF,VALUE,SMC3_ENABLED,SMC3_TARIFF_NAME,SMC3_EFFECTIVE_DATE) " +
                    "values (coeff,'C1',rId,null," + coeffValue2 + ",0,null,null);");
            stringBuilder.append("\n");

        }
        stringBuilder.append("\n");
        stringBuilder.append("\n");
    }
}



