package com.excelacom.century.utilityreports.rowmapper;

import com.excelacom.century.utilityreports.beans.DBHealthCheckBean;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBHealthCheckDisplayReportRowMapper implements RowMapper <DBHealthCheckBean> {
    @Override
    public DBHealthCheckBean mapRow(ResultSet resultSet, int i) throws SQLException {
        DBHealthCheckBean dbHealthCheckBean = new DBHealthCheckBean();
        dbHealthCheckBean.setId(resultSet.getInt("ID"));
        dbHealthCheckBean.setScenarioName(resultSet.getString("VALIDATION_SCENARIO"));
        dbHealthCheckBean.setCount(resultSet.getInt("COUNT"));
        dbHealthCheckBean.setExecutionTime(resultSet.getString("EXECUTION_TIME"));
        return  dbHealthCheckBean;
    }
}
