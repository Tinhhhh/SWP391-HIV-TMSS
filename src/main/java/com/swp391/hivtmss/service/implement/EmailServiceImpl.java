package com.swp391.hivtmss.service.implement;

import com.swp391.hivtmss.model.entity.*;
import com.swp391.hivtmss.repository.TreatmentRegimenDrugRepository;
import com.swp391.hivtmss.service.EmailService;
import com.swp391.hivtmss.util.DateUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    public static final String UTF_8_CODING = "UTF-8";
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final TreatmentRegimenDrugRepository treatmentRegimenDrugRepository;

    @Value("${spring.mail.username}")
    private String sender;

    @Override
    @Async
    public void sendAccountInformation(String name, String email, String password, String to, String template, String subject) throws MessagingException {
        try {
            String senderNickName = "Customer Service Team at HIV Treatment and Medical Services System";
            Context context = new Context();
            context.setVariable("username", name);
            context.setVariable("email", email);
            context.setVariable("password", password);
            extractTemplate(to, template, subject, senderNickName, context);
        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            throw new RuntimeException("Error: " + exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendMimeMessageWithHtml(String name, String to, String content, String template, String subject) throws MessagingException {
        try {
            String senderNickName = "Customer Service Team at HIV Treatment and Medical Services System";
            Context context = new Context();
            context.setVariable("username", name);
            context.setVariable("content", content);
            extractTemplate(to, template, subject, senderNickName, context);
        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            throw new RuntimeException("Error: " + exception.getMessage());
        }
    }

    @Override
    @Async
    public void sendAppointmentFollowUpNotification(String name, String to, String appointmentDate, String patientName, String template, String subject) throws MessagingException {
        try {
            String senderNickName = "Customer Service Team at HIV Treatment and Medical Services System";
            Context context = new Context();
            context.setVariable("username", name);
            context.setVariable("appointmentDate", appointmentDate);
            context.setVariable("patientName", patientName);
            extractTemplate(to, template, subject, senderNickName, context);
        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            throw new RuntimeException("Error: " + exception.getMessage());
        }
    }

    @Override
    public void sendAppointmentFinishedNotification(String name, String to, Appointment appointment, String template, String subject) throws MessagingException {

        Diagnosis diagnosis = appointment.getDiagnosis();
        TestType testType = diagnosis.getTestType();
        Treatment treatment = appointment.getTreatment();
        List<TreatmentRegimenDrug> treatmentRegimenDrugs = treatmentRegimenDrugRepository.findByTreatmentRegimen_IdAndMethod(treatment.getId(), treatment.getMethod());
        String drug = treatmentRegimenDrugs.stream()
                .map(t -> t.getDrug().getName())
                .collect(Collectors.joining(", "));

        String clinicalStage = diagnosis.getClinicalStage().toString().split("_")[1];
        int result = romanToInt(clinicalStage);

        try {
            String senderNickName = "Customer Service Team at HIV Treatment and Medical Services System";
            Context context = new Context();
            context.setVariable("username", name);
            context.setVariable("appointmentDate", DateUtil.formatTimestamp(appointment.getStartTime(), DateUtil.DATE_FORMAT));
            context.setVariable("patientName", appointment.fullName());
            context.setVariable("doctorName", appointment.getDoctor().fullName());
            context.setVariable("testType", testType.getName());
            context.setVariable("sampleType", diagnosis.getSampleType());
            context.setVariable("result", diagnosis.getResult());
            context.setVariable("resultType", diagnosis.getResultType());
            context.setVariable("virusType", diagnosis.getVirusType());
            context.setVariable("agAbResult", diagnosis.getAgAbResult());
            context.setVariable("cd4", diagnosis.getCd4());
            context.setVariable("virusLoad", diagnosis.getViralLoad());
            context.setVariable("pcrType", diagnosis.getPcrType());
            context.setVariable("clinicalStage", result);
            context.setVariable("note", diagnosis.getNote());
            context.setVariable("drugs", drug);
            context.setVariable("doctorAdvice", treatment.getDosageInstruction());
            extractTemplate(to, template, subject, senderNickName, context);
        } catch (Exception exception) {
            System.out.println("Error: " + exception.getMessage());
            throw new RuntimeException("Error: " + exception.getMessage());
        }

    }

    private void extractTemplate(String sentTo, String template, String subject, String senderNickName, Context context) throws MessagingException, UnsupportedEncodingException {
        String text = templateEngine.process(template, context);
        MimeMessage message = getMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_CODING);
        helper.setSubject(subject);
        helper.setPriority(1);
        helper.setFrom(sender, senderNickName);
        helper.setTo(sentTo);
        helper.setText(text, true);
        mailSender.send(message);
    }

    public MimeMessage getMessage() {
        return mailSender.createMimeMessage();
    }


    public static int romanToInt(String roman) {
        Map<Character, Integer> map = new HashMap<>();
        map.put('I', 1);
        map.put('V', 5);
        map.put('X', 10);

        int total = 0;
        int prev = 0;

        for (int i = roman.length() - 1; i >= 0; i--) {
            int curr = map.get(roman.charAt(i));
            if (curr < prev) {
                total -= curr;
            } else {
                total += curr;
            }
            prev = curr;
        }
        return total;
    }

}
