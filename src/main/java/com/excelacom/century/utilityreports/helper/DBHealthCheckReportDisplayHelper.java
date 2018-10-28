package com.excelacom.century.utilityreports.helper;

import com.excelacom.century.utilityreports.beans.DBHealthCheckBean;
import com.excelacom.century.utilityreports.rowmapper.DBHealthCheckDisplayReportRowMapper;
import com.excelacom.century.utilityreports.rowmapper.DBHealthCheckGeneratorRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


public class DBHealthCheckReportDisplayHelper {

    public JdbcTemplate getDbHealthCheckMySQLServerJdbcTemplate() {
        return dbHealthCheckMySQLServerJdbcTemplate;
    }

    public void setDbHealthCheckMySQLServerJdbcTemplate(JdbcTemplate dbHealthCheckMySQLServerJdbcTemplate) {
        this.dbHealthCheckMySQLServerJdbcTemplate = dbHealthCheckMySQLServerJdbcTemplate;
    }

    private JdbcTemplate dbHealthCheckMySQLServerJdbcTemplate;



    public List<DBHealthCheckBean> getReport() {
        System.out.println("DBHealthCheckReportDisplayHelper - Enter");

        List<DBHealthCheckBean> dbHealthCheckBeanList = getDbHealthCheckMySQLServerJdbcTemplate().query("select * from DB_HEALTH_CHECK_REPORT " +
                "where execution_time = (select distinct(max(execution_time)) from " +
                        " DB_HEALTH_CHECK_REPORT) order by count desc", new DBHealthCheckDisplayReportRowMapper());

        System.out.println("DBHealthCheckReportDisplayHelper - Exit");
        return dbHealthCheckBeanList;
    }
}
