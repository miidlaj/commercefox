package com.ecommerce.admin.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class AbstractReportService {

    protected DateFormat dateFormatter;

    public List<ReportItem> getReportDataLast7Days(ReportType reportType){
        return  getReportLastXDays(7, reportType);
    }

    public List<ReportItem> getReportDataLast28Days(ReportType reportType){
        return  getReportLastXDays(28, reportType);
    }

    protected List<ReportItem> getReportLastXDays(int days, ReportType reportType){
        Date endTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -(days - 1));
        Date startTime = cal.getTime();

        System.out.println("Start time:" + startTime);
        System.out.println("End Time:" + endTime);

        dateFormatter = new SimpleDateFormat("yyy-MM-dd");
        return getReportDataByDateRangeInternal(startTime, endTime, reportType);
    }

    public List<ReportItem> getReportDataLast6Months(ReportType reportType) {
        return getReportLastXMonths(6, reportType);
    }

    public List<ReportItem> getReportDataLastYear(ReportType reportType) {
        return getReportLastXMonths(12, reportType);
    }
    protected List<ReportItem> getReportLastXMonths(int months, ReportType reportType){
        Date endTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -(months - 1));
        Date startTime = cal.getTime();

        System.out.println("Start time:" + startTime);
        System.out.println("End Time:" + endTime);

        dateFormatter = new SimpleDateFormat("yyy-MM");
        return getReportDataByDateRangeInternal(startTime, endTime, reportType);
    }

    public List<ReportItem> getReportDataByDateRange(Date startTime, Date endTime, ReportType reportType){
        dateFormatter = new SimpleDateFormat("yyy-MM-dd");
        return getReportDataByDateRangeInternal(startTime, endTime, reportType);
    }

    protected abstract List<ReportItem> getReportDataByDateRangeInternal(Date startDate, Date endDate, ReportType reportType);

}
