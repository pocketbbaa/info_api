package com.kg.platform.controller;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kg.platform.common.aop.BaseControllerNote;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.utils.message.SendSms;
import com.kg.platform.model.request.SendsmsemailRequest;
import com.kg.platform.model.response.SmsSendResponse;

@Controller
@RequestMapping("sendsmsemail")
public class SendsmsemailController extends ApiBaseController {
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(SendsmsemailController.class);

    @Inject
    private SendSms sendSms;

    @SuppressWarnings("unused")
    @ResponseBody
    @RequestMapping("sendEmail")
    @BaseControllerNote(beanClazz = SendsmsemailRequest.class)
    public JsonEntity sendEmail(@RequestAttribute SendsmsemailRequest request) {

        SmsSendResponse smsSingleResponse = sendSms.send(request.getUserMobile(),
                propertyLoader.getProperty("message", "sms.validatecount"));

        return null;

    }

}
