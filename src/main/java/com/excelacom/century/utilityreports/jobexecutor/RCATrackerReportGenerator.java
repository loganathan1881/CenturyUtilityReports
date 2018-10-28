package com.excelacom.century.utilityreports.jobexecutor;


import com.excelacom.century.utilityreports.beans.RCATrackerBean;
import com.excelacom.century.utilityreports.email.RCATrackerEmailSender;
import com.excelacom.century.utilityreports.rowmapper.RCATrackerDisplayReportRowMapper;
import com.excelacom.century.utilityreports.rowmapper.RCATrackerGeneratorRowMapper;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RCATrackerReportGenerator extends QuartzJobBean {

    public JdbcTemplate getRcaTrackerGeneratorJdbcTemplate() {
        return rcaTrackerGeneratorJdbcTemplate;
    }

    public void setRcaTrackerGeneratorJdbcTemplate(JdbcTemplate rcaTrackerGeneratorJdbcTemplate) {
        this.rcaTrackerGeneratorJdbcTemplate = rcaTrackerGeneratorJdbcTemplate;
    }

    private JdbcTemplate rcaTrackerGeneratorJdbcTemplate;

    public RCATrackerEmailSender getRcaTrackerEmailComponent() {
        return rcaTrackerEmailComponent;
    }

    public void setRcaTrackerEmailComponent(RCATrackerEmailSender rcaTrackerEmailComponent) {
        this.rcaTrackerEmailComponent = rcaTrackerEmailComponent;
    }

    private RCATrackerEmailSender rcaTrackerEmailComponent;

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


    public JdbcTemplate getRcaTrackerMySQLServerJdbcTemplate() {
        return rcaTrackerMySQLServerJdbcTemplate;
    }

    public void setRcaTrackerMySQLServerJdbcTemplate(JdbcTemplate rcaTrackerMySQLServerJdbcTemplate) {
        this.rcaTrackerMySQLServerJdbcTemplate = rcaTrackerMySQLServerJdbcTemplate;
    }

    private JdbcTemplate rcaTrackerMySQLServerJdbcTemplate;



    @Override
    public void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("RCA Tracker Report Generator - Enter");
        setRcaTrackerGeneratorJdbcTemplate((JdbcTemplate) jobExecutionContext.getJobDetail().getJobDataMap().get("rcaTrackerGeneratorJdbcTemplate"));
        setRcaTrackerMySQLServerJdbcTemplate((JdbcTemplate) jobExecutionContext.getJobDetail().getJobDataMap().get("rcaTrackerMySQLServerJdbcTemplate"));
        setQueryInputFile((String) jobExecutionContext.getJobDetail().getJobDataMap().get("queryInputFile"));
        setOutputFilePath((String) jobExecutionContext.getJobDetail().getJobDataMap().get("outputFilePath"));
        setOutputFileName((String) jobExecutionContext.getJobDetail().getJobDataMap().get("outputFileName"));
        setOutputFileExtension((String) jobExecutionContext.getJobDetail().getJobDataMap().get("outputFileExtension"));
        setRcaTrackerEmailComponent((RCATrackerEmailSender) jobExecutionContext.getJobDetail().getJobDataMap().get("rcaTrackerEmailComponent"));
        LocalDateTime dateFileName = LocalDateTime.now();
        generateReport(dateFileName);
        String outputFileName1 = generateFile(dateFileName);
        //String outputFileName1 = "C:\\Loganathan\\DBHealthCheckReport_2018-03-09.xlsx";
        try {
            System.out.println("RCA Tracker - Output File Name : " + outputFileName1);
            getRcaTrackerEmailComponent().distributeReport(outputFileName1, dateFileName);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        System.out.println("RCA Tracker Report Generator - Exit");
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

            List<RCATrackerBean> rcaTrackerBeanList = new ArrayList<RCATrackerBean>();

            for (StringBuilder query : queryList) {
                System.out.println(query);

                try {
                    System.out.println("Query Timeout - Default Value : " + getRcaTrackerGeneratorJdbcTemplate().getQueryTimeout());
                    getRcaTrackerGeneratorJdbcTemplate().setQueryTimeout(200);
                    System.out.println("Query Timeout - Modified Value : " + getRcaTrackerGeneratorJdbcTemplate().getQueryTimeout());

                    System.out.println("java.net.useSystemProxies - Default Value : " + System.getProperty("java.net.useSystemProxies"));
                    System.setProperty("java.net.useSystemProxies", "false");
                    System.out.println("java.net.useSystemProxies - Modified Value : " + System.getProperty("java.net.useSystemProxies"));

                    rcaTrackerBeanList = getRcaTrackerGeneratorJdbcTemplate().query(query.toString(),
                            new RCATrackerGeneratorRowMapper());
                } catch(Exception e) {
                    e.printStackTrace();
                }

                //rcaTrackerBeanList.add(rcaTrackerBean);

            }

            System.out.println("After Query Execution...");
            for (RCATrackerBean rcaTrackerBean : rcaTrackerBeanList) {

                getRcaTrackerMySQLServerJdbcTemplate().update("INSERT INTO RCA_TRACKER_REPORT " +
                                "(EXECUTION_TIME, INCIDENTTICKET, INCIDENTREQUESTOR, INCIDENTOWNER," +
                                "INCIDENTSUBJECT, INCIDENTREQUESTORNAME, INCIDENTRESOLVEDBY, INCIDENTDESCRIPTION," +
                                "INCIDENTSTATUS, INCIDENTCREATEDDATE, INCIDENTASSIGNEDDATE, INCIDENTMODIFIEDDATE," +
                                "INCIDENTRESOLVEDDATE, PRIORITY) VALUES (?, ?, ?, ?," +
                                "?, ?, ?, ?," +
                                "?, ?, ?, ?," +
                                "?, ?)", currentDate,
                        rcaTrackerBean.getIncidentTicketNumber(), rcaTrackerBean.getIncidentRequestor(),
                        rcaTrackerBean.getIncidentOwner(), rcaTrackerBean.getIncidentSubject(),
                        rcaTrackerBean.getIncidentRequestorName(), rcaTrackerBean.getIncidentResolvedBy(),
                        rcaTrackerBean.getIncidentDescription(), rcaTrackerBean.getIncidentStatus(),
                        rcaTrackerBean.getIncidentCreatedDate(), rcaTrackerBean.getIncidentAssignedDate(),
                        rcaTrackerBean.getIncidentModifiedDate(), rcaTrackerBean.getIncidentResolvedDate(),
                        rcaTrackerBean.getIncidentPriority()
                );

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
            XSSFSheet sheet = workbook.createSheet("RCA Tracker");

            int rowNum = 0;

            Row headerRow = sheet.createRow(rowNum);
            Cell headerCell = headerRow.createCell(0);
            headerCell.setCellValue("Incident Ticket Number");

            headerCell = headerRow.createCell(1);
            headerCell.setCellValue("Incident Requestor");

            headerCell = headerRow.createCell(2);
            headerCell.setCellValue("Incident Owner");

            headerCell = headerRow.createCell(3);
            headerCell.setCellValue("Incident Subject");

            headerCell = headerRow.createCell(4);
            headerCell.setCellValue("Incident Requestor Name");

            headerCell = headerRow.createCell(5);
            headerCell.setCellValue("Incident Resolved By");

            headerCell = headerRow.createCell(6);
            headerCell.setCellValue("Incident Description");

            headerCell = headerRow.createCell(7);
            headerCell.setCellValue("Incident Status");

            headerCell = headerRow.createCell(8);
            headerCell.setCellValue("Incident Created Date");

            headerCell = headerRow.createCell(9);
            headerCell.setCellValue("Incident Assigned Date");

            headerCell = headerRow.createCell(10);
            headerCell.setCellValue("Incident Modified Date");

            headerCell = headerRow.createCell(11);
            headerCell.setCellValue("Incident Resolved Date");

            headerCell = headerRow.createCell(12);
            headerCell.setCellValue("Incident Priority");


            List<RCATrackerBean> rcaTrackerBeanList = getRcaTrackerMySQLServerJdbcTemplate().query("select * from RCA_TRACKER_REPORT " +
                    "where execution_time = (select distinct(max(execution_time)) from " +
                    " RCA_TRACKER_REPORT) order by INCIDENTTICKET", new RCATrackerDisplayReportRowMapper());


            for (RCATrackerBean rcaTrackerBean : rcaTrackerBeanList) {

                System.out.println(rcaTrackerBean.getId());
                System.out.println(rcaTrackerBean.getIncidentTicketNumber());
                System.out.println(rcaTrackerBean.getIncidentDescription());
                Row row = sheet.createRow(++rowNum);
                Cell cell = row.createCell(0);
                cell.setCellValue(rcaTrackerBean.getIncidentTicketNumber());

                cell = row.createCell(1);
                cell.setCellValue(rcaTrackerBean.getIncidentRequestor());

                cell = row.createCell(2);
                cell.setCellValue(rcaTrackerBean.getIncidentOwner());

                cell = row.createCell(3);
                cell.setCellValue(rcaTrackerBean.getIncidentSubject());

                cell = row.createCell(4);
                cell.setCellValue(rcaTrackerBean.getIncidentRequestorName());

                cell = row.createCell(5);
                cell.setCellValue(rcaTrackerBean.getIncidentResolvedBy());

                cell = row.createCell(6);
                cell.setCellValue(rcaTrackerBean.getIncidentDescription());

                cell = row.createCell(7);
                cell.setCellValue(rcaTrackerBean.getIncidentStatus());

                cell = row.createCell(8);
                cell.setCellValue(rcaTrackerBean.getIncidentCreatedDate());

                cell = row.createCell(9);
                cell.setCellValue(rcaTrackerBean.getIncidentAssignedDate());

                cell = row.createCell(10);
                cell.setCellValue(rcaTrackerBean.getIncidentModifiedDate());

                cell = row.createCell(11);
                cell.setCellValue(rcaTrackerBean.getIncidentResolvedDate());

                cell = row.createCell(12);
                cell.setCellValue(rcaTrackerBean.getIncidentPriority());
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