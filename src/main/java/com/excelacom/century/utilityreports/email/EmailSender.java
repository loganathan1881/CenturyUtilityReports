package com.excelacom.century.utilityreports.email;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmailSender {

    public JavaMailSender getEmailSender() {
        return emailSender;
    }

    public void setEmailSender(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public SimpleMailMessage getDbHealthCheckReportMessage() {
        return dbHealthCheckReportMessage;
    }

    public void setDbHealthCheckReportMessage(SimpleMailMessage dbHealthCheckReportMessage) {
        this.dbHealthCheckReportMessage = dbHealthCheckReportMessage;
    }

    private JavaMailSender emailSender;

    private SimpleMailMessage dbHealthCheckReportMessage;

    public void distributeReport(String outputFileName, LocalDateTime dateTime) throws MessagingException {

        System.out.println("UtilityReports.distributeReport - Enter");
        // DateTimeFormatter dateTimeFormatterReportGeneration = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmm");
        DateTimeFormatter dateTimeFormatterReportGeneration = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String reportGenerationTime = dateTime.format(dateTimeFormatterReportGeneration);
        System.out.println("currentDateFileName : " + reportGenerationTime);

        System.out.println("Before mimeMessage Initialization...");
        MimeMessage mimeMessage = getEmailSender().createMimeMessage();

        System.out.println("After mimeMessage Initialization...");
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        System.out.println("After mimeMessageHelper Initialization...");
        mimeMessageHelper.setFrom(getDbHealthCheckReportMessage().getFrom());
        mimeMessageHelper.setTo(getDbHealthCheckReportMessage().getTo());
        mimeMessageHelper.setSubject(String.format(getDbHealthCheckReportMessage().getSubject(),
                reportGenerationTime));
        mimeMessageHelper.setText(getDbHealthCheckReportMessage().getText());

        System.out.println("After mimeMessageHelper setup...");
        FileSystemResource reportFile = new FileSystemResource(outputFileName);
        System.out.println("After reportFile...");
        mimeMessageHelper.addAttachment(reportFile.getFilename(), reportFile);
        System.out.println("going to send mail : " + getDbHealthCheckReportMessage().getTo());

        getEmailSender().send(mimeMessage);
        System.out.println("Utility Report Report Delivered Sent Successfully...");
    }

}
