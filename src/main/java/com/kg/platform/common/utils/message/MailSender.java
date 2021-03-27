package com.kg.platform.common.utils.message;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.kg.platform.dao.entity.User;

public class MailSender {

    @Inject
    private JavaMailSender javaMailSender;

    @Inject
    private SimpleMailMessage simpleMailMessage;

    @Inject
    private TaskExecutor taskExecutor;

    /**
     * 
     * 构建邮件内容，发送邮件。
     * 
     * @param user
     *            用户
     * 
     * @param url
     *            链接
     * 
     */

    public void send(User user, String url, String email) {

        String to = email;

        String text = "";

        Map<String, String> map = new HashMap<String, String>(1);

        map.put("url", url);

        this.taskExecutor.execute(new SendMailThread(to, null, text));

    }

    // 内部线程类，利用线程池异步发邮件。

    private class SendMailThread implements Runnable {

        private String to;

        private String subject;

        private String content;

        private SendMailThread(String to, String subject, String content) {

            super();

            this.to = to;

            this.subject = subject;

            this.content = content;

        }

        @Override

        public void run() {

            sendMail(to, subject, content);

        }

    }

    /**
     * 
     * 发送邮件
     * 
     * @param to
     *            收件人邮箱
     * 
     * @param subject
     *            邮件主题
     * 
     * @param content
     *            邮件内容
     * 
     */

    public void sendMail(String to, String subject, String content) {

        try {

            MimeMessage message = javaMailSender.createMimeMessage();

            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

            messageHelper.setFrom(simpleMailMessage.getFrom());

            if (subject != null) {

                messageHelper.setSubject(subject);

            } else {

                messageHelper.setSubject(simpleMailMessage.getSubject());

            }

            messageHelper.setTo(to);

            messageHelper.setText(content, true);

            javaMailSender.send(message);

        } catch (MessagingException e) {

            e.printStackTrace();

        }
    }
}
