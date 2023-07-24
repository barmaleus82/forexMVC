package com.spring.forex.service.jdbc;

import com.spring.forex.exception.ExceptionJDBC;
import com.spring.forex.model.RateFile;
import com.spring.forex.service.FileService;
import com.spring.forex.service.RateJDBCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Properties;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

@Service
public class RateJDBCServiceImpl implements RateJDBCService {

    @Autowired
    private FileService rateFileService;
    private Properties properties;

    private static final Logger LOGGER = Logger.getLogger(RateJDBCServiceImpl.class.getName());

    @Autowired
    private void readPropertis() {
        try (FileInputStream fis = new FileInputStream("src/main/resources/application.properties")) {
            Properties prop = new Properties();
            prop.load(fis);
            this.properties = prop;
        } catch (Exception ex) {
            throw new ExceptionJDBC("Exception read properties");
        }
    }

    @Override
    public void fillRateFromFile(RateFile rateFile) {
        if (rateFile.getCurrency() == null || rateFile.getPeriod() == null) {
            return;
        }
        long currency_id = rateFile.getCurrency().getId();
        long period_id = rateFile.getPeriod().getId();

        LocalDateTime lastUploadedTime = rateFile.getLastUploaded();
        if (lastUploadedTime == null) {
            lastUploadedTime = LocalDateTime.parse("2001-01-01T00:00:00");
        }

        String query = "insert into rates (date_time, currency_id, period_id, open, max, min, close, volume, spread )" +
                "values(?,?,?,?,?,?,?,?,?)";

        int statInsertLines = 0;
        int statErrorLines = 0;

        try (Connection con = DriverManager.getConnection(
                properties.getProperty("spring.datasource.url"),
                properties.getProperty("spring.datasource.username"),
                properties.getProperty("spring.datasource.password"));
             FileReader fr = new FileReader(properties.getProperty("ratefiles.directory") + "/" + rateFile.getFileName());
             BufferedReader br = new BufferedReader(fr)) {

            PreparedStatement prepareStatement = con.prepareStatement(query);
            String line = br.readLine();
            if (line.indexOf("DATE") > 0) { //import
                line = br.readLine();   //start from 2nd line
                while (line != null) {
                    String[] strArr = line.split("\\t");
                    LocalDateTime dateTime = LocalDateTime.parse(strArr[0].replaceAll("\\.", "-") + "T" + strArr[1]);
                    if (dateTime.compareTo(lastUploadedTime) > 0) {

                        prepareStatement.setTimestamp(1, Timestamp.valueOf(dateTime));
                        prepareStatement.setLong(2, currency_id);
                        prepareStatement.setLong(3, period_id);
                        prepareStatement.setDouble(4, Double.valueOf(strArr[2]));
                        prepareStatement.setDouble(5, Double.valueOf(strArr[3]));
                        prepareStatement.setDouble(6, Double.valueOf(strArr[4]));
                        prepareStatement.setDouble(7, Double.valueOf(strArr[5]));
                        prepareStatement.setInt(8, Integer.valueOf(strArr[6]));
                        prepareStatement.setInt(9, Integer.valueOf(strArr[8]));

                        if (prepareStatement.executeUpdate() > 0) {
                            lastUploadedTime = dateTime;
                            statInsertLines++;
                        } else {
                            statErrorLines++;
                        }
                    }
                    line = br.readLine();
                }
            } else {
                //simple save
            }
            rateFile.setLastUploaded(lastUploadedTime);
            rateFileService.update(rateFile);
        } catch (IOException ex) {
            throw new ExceptionJDBC("Error reading/loading files");
        } catch (SQLException ex) {
            throw new ExceptionJDBC("Error create connection");
        }
        LOGGER.info(rateFile.getFileName() + " Insert lines:" + statInsertLines + ", Error lines:" + statErrorLines);
    }
}
