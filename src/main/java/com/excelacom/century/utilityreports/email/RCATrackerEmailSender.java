package com.excelacom.century.utilityreports.email;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RCATrackerEmailSender {

    private JavaMailSender rcaTrackerEmailSenderImpl;
    private SimpleMailMessage rcaTrackerReportMessage;

    public JavaMailSender getRcaTrackerEmailSenderImpl() {
        return rcaTrackerEmailSenderImpl;
    }

    public void setRcaTrackerEmailSenderImpl(JavaMailSender rcaTrackerEmailSenderImpl) {
        this.rcaTrackerEmailSenderImpl = rcaTrackerEmailSenderImpl;
    }

    public SimpleMailMessage getRcaTrackerReportMessage() {
        return rcaTrackerReportMessage;
    }

    public void setRcaTrackerReportMessage(SimpleMailMessage rcaTrackerReportMessage) {
        this.rcaTrackerReportMessage = rcaTrackerReportMessage;
    }

    public void distributeReport(String outputFileName, LocalDateTime dateTime) throws MessagingException {

        System.out.println("RCATrackerEmailSender.distributeReport - Enter");
        // DateTimeFormatter dateTimeFormatterReportGeneration = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmm");
        DateTimeFormatter dateTimeFormatterReportGeneration = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String reportGenerationTime = dateTime.format(dateTimeFormatterReportGeneration);
        System.out.println("currentDateFileName : " + reportGenerationTime);

        System.out.println("Before mimeMessage Initialization...");
        MimeMessage mimeMessage = getRcaTrackerEmailSenderImpl().createMimeMessage();

        System.out.println("After mimeMessage Initialization...");
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        System.out.println("After mimeMessageHelper Initialization...");
        mimeMessageHelper.setFrom(getRcaTrackerReportMessage().getFrom());
        mimeMessageHelper.setTo(getRcaTrackerReportMessage().getTo());
        mimeMessageHelper.setSubject(String.format(getRcaTrackerReportMessage().getSubject(),
                reportGenerationTime));
        mimeMessageHelper.setText(getRcaTrackerReportMessage().getText());

        System.out.println("After mimeMessageHelper setup...");
        FileSystemResource reportFile = new FileSystemResource(outputFileName);
        System.out.println("After reportFile...");
        mimeMessageHelper.addAttachment(reportFile.getFilename(), reportFile);
        System.out.println("going to send mail : " + getRcaTrackerReportMessage().getTo());

        getRcaTrackerEmailSenderImpl().send(mimeMessage);
        System.out.println("RCATrackerEmailSender Report Delivered Sent Successfully...");
    }

}
