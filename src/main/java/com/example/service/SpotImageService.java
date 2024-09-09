package com.example.service;

public class SpotImageService {

    private static final String IMAGE_DIRECTORY = "/web/static/images/스포츠시설사진/";

    public String getImagePath(String spot) {
        // 경로에 따라 이미지를 매핑
        switch (spot) {
            case "gateball":
                return IMAGE_DIRECTORY + "게이트볼장.jpg";
            case "golf":
                return IMAGE_DIRECTORY + "골프연습장.jpg";
            case "basketball":
                return IMAGE_DIRECTORY + "농구장.jpg";
            case "volleyball":
                return IMAGE_DIRECTORY + "배구장.jpg";
            case "badminton":
                return IMAGE_DIRECTORY + "배드민턴장.jpg";
            case "ice":
                return IMAGE_DIRECTORY + "빙상장.jpg";
            case "swimming":
                return IMAGE_DIRECTORY + "수영장.jpg";
            case "baseball":
                return IMAGE_DIRECTORY + "야구장.jpg";
            case "jokgu":
                return IMAGE_DIRECTORY + "족구장.jpg";
            case "soccer":
                return IMAGE_DIRECTORY + "축구장.png";
            case "tennis":
                return IMAGE_DIRECTORY + "테니스장.jpg";
            case "futsal":
                return IMAGE_DIRECTORY + "풋살장.jpg";
            default:
                return IMAGE_DIRECTORY + "default.jpg"; // 기본 이미지 또는 오류 이미지
        }
    }
}
