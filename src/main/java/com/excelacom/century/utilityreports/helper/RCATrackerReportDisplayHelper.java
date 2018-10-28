package com.excelacom.century.utilityreports.helper;

import com.excelacom.century.utilityreports.beans.RCATrackerBean;
import com.excelacom.century.utilityreports.rowmapper.RCATrackerDisplayReportRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;


public class RCATrackerReportDisplayHelper {

    public JdbcTemplate getRcaTrackerMySQLServerJdbcTemplate() {
        return rcaTrackerMySQLServerJdbcTemplate;
    }

    public void setRcaTrackerMySQLServerJdbcTemplate(JdbcTemplate rcaTrackerMySQLServerJdbcTemplate) {
        this.rcaTrackerMySQLServerJdbcTemplate = rcaTrackerMySQLServerJdbcTemplate;
    }

    private JdbcTemplate rcaTrackerMySQLServerJdbcTemplate;



    public List<RCATrackerBean> getReport() {
        System.out.println("RCATrackerReportDisplayHelper - Enter");

        List<RCATrackerBean> rcaTrackerBean = getRcaTrackerMySQLServerJdbcTemplate().query("select * from RCA_TRACKER_REPORT " +
                "where execution_time = (select distinct(max(execution_time)) from " +
                        " RCA_TRACKER_REPORT) order by INCIDENTTICKET", new RCATrackerDisplayReportRowMapper());

        System.out.println("RCATrackerReportDisplayHelper - Exit");
        return rcaTrackerBean;
    }
}
