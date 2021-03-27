package com.kg.platform.service;

import com.kg.platform.model.MailModel;

public interface EmailService {

    /**
     * email配置
     * 
     * @return
     */
    public boolean emailManage();

    /**
     * 发送邮件
     * 
     * @param mail
     */
    public boolean sendEmail(MailModel mail);

}
