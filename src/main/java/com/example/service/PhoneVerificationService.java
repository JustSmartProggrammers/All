package com.example.service;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Random;
import java.util.regex.Pattern;

public class PhoneVerificationService {
    private final String API_KEY = "NCSTAKTEQKHUPBZC";
    private final String API_SECRET = "4REZJUQPNK3YZJ6E7RJPGJRUJGN0AGWJ";
    private final String SENDER_NUMBER = "01047434904";

    private static final Pattern PHONE_PATTERN = Pattern.compile("^010\\d{8}$");

    private HashMap<String, String> verificationCodes = new HashMap<>();

    public String sendVerificationCode(String phoneNumber) throws CoolsmsException {
        if (!isValidPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number format. It should be 11 digits starting with 010.");
        }

        String verificationCode = generateVerificationCode();
        verificationCodes.put(phoneNumber, verificationCode);

        Message coolsms = new Message(API_KEY, API_SECRET);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phoneNumber);
        params.put("from", SENDER_NUMBER);
        params.put("type", "SMS");
        params.put("text", "[스포츠스팟] 인증번호는 " + verificationCode + " 입니다. 정확히 입력해주세요.");
        params.put("app_version", "test app 1.2");

        JSONObject obj = (JSONObject) coolsms.send(params);

        // 응답 로깅
        System.out.println("CoolSMS Response: " + obj);

        // 메시지 발송 성공 여부 확인
        if (obj != null && obj.containsKey("group_id")) {
            // group_id가 존재하면 메시지 발송이 성공한 것으로 간주
            return "Verification code sent successfully";
        } else {
            // 에러 메시지 확인
            String errorMessage = obj != null && obj.containsKey("error_message")
                    ? obj.get("error_message").toString()
                    : "Unknown error";
            throw new RuntimeException("Failed to send verification code. Error: " + errorMessage);
        }
    }

    public boolean verifyCode(String phoneNumber, String code) {
        if (!isValidPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number format. It should be 11 digits starting with 010.");
        }

        String storedCode = verificationCodes.get(phoneNumber);
        if (storedCode != null && storedCode.equals(code)) {
            verificationCodes.remove(phoneNumber);  // Remove the code after successful verification
            return true;
        }
        return false;
    }

    private String generateVerificationCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return PHONE_PATTERN.matcher(phoneNumber).matches();
    }
}