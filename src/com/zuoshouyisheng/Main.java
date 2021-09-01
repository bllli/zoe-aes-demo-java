package com.zuoshouyisheng;


public class Main {

    public static void main(String[] args) {
        String access_secret = "7a47673e3e9149c89a10ad4561702186";
        String json = "[{\"event\":\"registration_created\",\"properties\":{\"user_id\":\"123\",\"patient_id\":\"123\"},\"happen_ts\":1630482503},{\"event\":\"bill_paid\",\"properties\":{\"user_id\":\"123\",\"patient_id\":\"123\",\"bill_id\":\"123\"},\"happen_ts\":1630482503}]";
        ZoeCrypt c = ZoeCrypt.Create(access_secret);
        String data = c.encrypt(json);
        System.out.println(data);

    }
}
