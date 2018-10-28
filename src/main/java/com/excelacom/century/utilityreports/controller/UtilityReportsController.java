package com.excelacom.century.utilityreports.controller;

import com.excelacom.century.utilityreports.helper.DBHealthCheckReportDisplayHelper;
import com.excelacom.century.utilityreports.helper.RCATrackerReportDisplayHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UtilityReportsController {

    public DBHealthCheckReportDisplayHelper getdBHealthCheckReportDisplayHelper() {
        return dBHealthCheckReportDisplayHelper;
    }

    public void setdBHealthCheckReportDisplayHelper(DBHealthCheckReportDisplayHelper dBHealthCheckReportDisplayHelper) {
        this.dBHealthCheckReportDisplayHelper = dBHealthCheckReportDisplayHelper;
    }

    @Autowired
    private DBHealthCheckReportDisplayHelper dBHealthCheckReportDisplayHelper;

    public RCATrackerReportDisplayHelper getRcaTrackerReportDisplayHelper() {
        return rcaTrackerReportDisplayHelper;
    }

    public void setRcaTrackerReportDisplayHelper(RCATrackerReportDisplayHelper rcaTrackerReportDisplayHelper) {
        this.rcaTrackerReportDisplayHelper = rcaTrackerReportDisplayHelper;
    }

    @Autowired
    private RCATrackerReportDisplayHelper rcaTrackerReportDisplayHelper;


    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public ModelAndView dashboardView(ModelMap model) {
        System.out.println("dashboardView - Enter");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("dashboard/dashboard");
        modelAndView.addObject("message", "User");
        return modelAndView;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView initialLoading(ModelMap model) {
        System.out.println("initialLoading - Enter");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("dashboard/dashboard");
        modelAndView.addObject("message", "User");
        return modelAndView;
    }

    @RequestMapping(value = "/dbhealthcheck", method = RequestMethod.GET)
    public ModelAndView dbHealthCheck(ModelMap model) {
        System.out.println("dbHealthCheck - Enter");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("reports/dbhealthcheck");
        modelAndView.addObject("dbHealthCheckList", getdBHealthCheckReportDisplayHelper()
                .getReport());
        //modelAndView.addObject("msg", "No Records Found");
        return modelAndView;
    }

    @RequestMapping(value = "/rcatracker", method = RequestMethod.GET)
    public ModelAndView rcaTracker(ModelMap model) {
        System.out.println("rcaTracker - Enter");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("reports/rcatracker");
        modelAndView.addObject("rcaTrackerList", getRcaTrackerReportDisplayHelper()
                .getReport());
        //modelAndView.addObject("msg", "No Records Found");
        return modelAndView;
    }


}
