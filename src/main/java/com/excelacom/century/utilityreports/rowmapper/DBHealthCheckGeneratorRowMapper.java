package com.excelacom.century.utilityreports.rowmapper;

import com.excelacom.century.utilityreports.beans.DBHealthCheckBean;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBHealthCheckGeneratorRowMapper implements RowMapper <DBHealthCheckBean> {
    @Override
    public DBHealthCheckBean mapRow(ResultSet resultSet, int i) throws SQLException {
        DBHealthCheckBean dbHealthCheckBean = new DBHealthCheckBean();
        dbHealthCheckBean.setScenarioName(resultSet.getString("COL1"));
        dbHealthCheckBean.setCount(resultSet.getInt("COL2"));
        return  dbHealthCheckBean;
    }
}
