package com.excelacom.century.utilityreports.jobexecutor;


import com.excelacom.century.utilityreports.beans.DBHealthCheckBean;
import com.excelacom.century.utilityreports.email.EmailSender;
import com.excelacom.century.utilityreports.rowmapper.DBHealthCheckDisplayReportRowMapper;
import com.excelacom.century.utilityreports.rowmapper.DBHealthCheckGeneratorRowMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DBHealthCheckReportGenerator extends QuartzJobBean {

    private JdbcTemplate jdbcTemplate;

    public EmailSender getDbHealthCheckEmailComponent() {
        return dbHealthCheckEmailComponent;
    }

    public void setDbHealthCheckEmailComponent(EmailSender dbHealthCheckEmailComponent) {
        this.dbHealthCheckEmailComponent = dbHealthCheckEmailComponent;
    }

    private EmailSender dbHealthCheckEmailComponent;


    private String queryInputFile;

    private String outputFilePath;

    public String getQueryInputFile() {
        return queryInputFile;
    }

    public void setQueryInputFile(String queryInputFile) {
        this.queryInputFile = queryInputFile;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public void setOutputFilePath(String outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }

    public String getOutputFileExtension() {
        return outputFileExtension;
    }

    public void setOutputFileExtension(String outputFileExtension) {
        this.outputFileExtension = outputFileExtension;
    }

    private String outputFileName;

    private String outputFileExtension;


    public JdbcTemplate getMySQLJdbcTemplate() {
        return mySQLJdbcTemplate;
    }

    public void setMySQLJdbcTemplate(JdbcTemplate mySQLJdbcTemplate) {
        this.mySQLJdbcTemplate = mySQLJdbcTemplate;
    }

    private JdbcTemplate mySQLJdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("DB Health Check Report Generator - Enter");
        setJdbcTemplate((JdbcTemplate) jobExecutionContext.getJobDetail().getJobDataMap().get("jdbcTemplate"));
        setMySQLJdbcTemplate((JdbcTemplate) jobExecutionContext.getJobDetail().getJobDataMap().get("localMySQLServerJdbcTemplate"));
        setQueryInputFile((String) jobExecutionContext.getJobDetail().getJobDataMap().get("queryInputFile"));
        setOutputFilePath((String) jobExecutionContext.getJobDetail().getJobDataMap().get("outputFilePath"));
        setOutputFileName((String) jobExecutionContext.getJobDetail().getJobDataMap().get("outputFileName"));
        setOutputFileExtension((String) jobExecutionContext.getJobDetail().getJobDataMap().get("outputFileExtension"));
        setDbHealthCheckEmailComponent((EmailSender) jobExecutionContext.getJobDetail().getJobDataMap().get("dbHealthCheckEmailComponent"));
        LocalDateTime dateFileName = LocalDateTime.now();
        generateReport(dateFileName);
        String outputFileName1 = generateFile(dateFileName);
        //String outputFileName1 = "C:\\Loganathan\\DBHealthCheckReport_2018-03-09.xlsx";
        try {
            System.out.println("outputFileName1 : " + outputFileName1);
            getDbHealthCheckEmailComponent().distributeReport(outputFileName1, dateFileName);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        System.out.println("DB Health Check Report Generator - Exit");
    }


    private void generateReport(LocalDateTime dateFileName) {

        String sqlScriptPath = getQueryInputFile();

        try {

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime date = LocalDateTime.now();
            String currentDate = date.format(dateTimeFormatter);
            System.out.println(currentDate);

            System.out.println(sqlScriptPath);

            Scanner sqlScriptReader = new Scanner(new File(sqlScriptPath));
            StringBuilder queryBuilder = new StringBuilder();
            List<StringBuilder> queryList = new ArrayList<StringBuilder>();

            while (sqlScriptReader.hasNextLine()) {
                String line = sqlScriptReader.nextLine();
                if (!line.contains("#DELIM#")) {
                    queryBuilder.append(line);
                    queryBuilder.append(" ");
                } else {
                    queryList.add(queryBuilder);
                    queryBuilder = new StringBuilder();
                }
            }

            List<DBHealthCheckBean> dbHealthCheckBeanList = new ArrayList<DBHealthCheckBean>();

            for (StringBuilder query : queryList) {
                System.out.println(query);
                DBHealthCheckBean dbHealthCheckBean = getJdbcTemplate().queryForObject(query.toString(),
                        new DBHealthCheckGeneratorRowMapper());

                dbHealthCheckBeanList.add(dbHealthCheckBean);

            }

            for (DBHealthCheckBean dbHealthCheckBean : dbHealthCheckBeanList) {

                getMySQLJdbcTemplate().update("INSERT INTO DB_HEALTH_CHECK_REPORT " +
                                "(EXECUTION_TIME, VALIDATION_SCENARIO, COUNT) VALUES (?, ?, ?)", currentDate,
                        dbHealthCheckBean.getScenarioName(), dbHealthCheckBean.getCount());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateFile(LocalDateTime dateFileName) {
        DateTimeFormatter dateTimeFormatterFileName = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmm");
        DateTimeFormatter dateTimeFormatterQuery = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String currentDateFileName = dateFileName.format(dateTimeFormatterFileName);
        String executionDateForTheQuery = dateFileName.format(dateTimeFormatterQuery);
        System.out.println("currentDateFileName : " + currentDateFileName);
        System.out.println("executionDateForTheQuery : " + executionDateForTheQuery);

        String sqlWritePath = getOutputFilePath() + getOutputFileName()
                + "_" + currentDateFileName + "." + getOutputFileExtension();

        try {

            System.out.println(sqlWritePath);

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("DB Health Check");

            int rowNum = 0;

            Row headerRow = sheet.createRow(rowNum);
            Cell headerCell = headerRow.createCell(0);
            headerCell.setCellValue("Validation Scenario");

            headerCell = headerRow.createCell(1);
            headerCell.setCellValue("Count");

            List<DBHealthCheckBean> dbHealthCheckBeanList = getMySQLJdbcTemplate().query("select * from DB_HEALTH_CHECK_REPORT " +
                    "where execution_time = (select distinct(max(execution_time)) from " +
                    " DB_HEALTH_CHECK_REPORT) order by count desc", new DBHealthCheckDisplayReportRowMapper());


            for (DBHealthCheckBean dbHealthCheckBean : dbHealthCheckBeanList) {

                System.out.println(dbHealthCheckBean.getScenarioName());
                System.out.println(dbHealthCheckBean.getCount());
                Row row = sheet.createRow(++rowNum);
                Cell cell = row.createCell(0);
                cell.setCellValue(dbHealthCheckBean.getScenarioName());

                cell = row.createCell(1);
                cell.setCellValue(dbHealthCheckBean.getCount());
            }

            FileOutputStream fileOutputStream = new FileOutputStream(sqlWritePath);
            workbook.write(fileOutputStream);
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sqlWritePath;
    }
}