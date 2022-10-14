package com.example.plantapp;

public class Check {
    public Check(){}

    public Check(String id, String name, String birth, String job) {
        this.id = id;
        this.name = name;
        this.birth = birth;
        this.job = job;
    }

    private String id;       // 번호
    private String name;     // 이름
    private String birth;     // 생년월일
    private String job;     // 직종

}
