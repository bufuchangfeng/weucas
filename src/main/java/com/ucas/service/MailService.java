package com.ucas.service;

public interface MailService {
    boolean send(String sendTo, String subject, String content);
}
