package com.excelacom.century.utilityreports.rowmapper;

import com.excelacom.century.utilityreports.beans.RCATrackerBean;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RCATrackerDisplayReportRowMapper implements RowMapper <RCATrackerBean> {
    @Override
    public RCATrackerBean mapRow(ResultSet resultSet, int i) throws SQLException {
        RCATrackerBean rcaTrackerBean = new RCATrackerBean();
        rcaTrackerBean.setId(resultSet.getInt("ID"));
        rcaTrackerBean.setExecutionTime(resultSet.getString("EXECUTION_TIME"));
        rcaTrackerBean.setIncidentTicketNumber(resultSet.getString("INCIDENTTICKET"));
        rcaTrackerBean.setIncidentRequestor(resultSet.getString("INCIDENTREQUESTOR"));
        rcaTrackerBean.setIncidentOwner(resultSet.getString("INCIDENTOWNER"));
        rcaTrackerBean.setIncidentSubject(resultSet.getString("INCIDENTSUBJECT"));
        rcaTrackerBean.setIncidentRequestorName(resultSet.getString("INCIDENTREQUESTORNAME"));
        rcaTrackerBean.setIncidentResolvedBy(resultSet.getString("INCIDENTRESOLVEDBY"));
        rcaTrackerBean.setIncidentDescription(resultSet.getString("INCIDENTDESCRIPTION"));
        rcaTrackerBean.setIncidentStatus(resultSet.getString("INCIDENTSTATUS"));
        rcaTrackerBean.setIncidentCreatedDate(resultSet.getString("INCIDENTCREATEDDATE"));
        rcaTrackerBean.setIncidentAssignedDate(resultSet.getString("INCIDENTASSIGNEDDATE"));
        rcaTrackerBean.setIncidentModifiedDate(resultSet.getString("INCIDENTMODIFIEDDATE"));
        rcaTrackerBean.setIncidentResolvedDate(resultSet.getString("INCIDENTRESOLVEDDATE"));
        rcaTrackerBean.setIncidentPriority(resultSet.getString("PRIORITY"));

        return  rcaTrackerBean;
    }
}
