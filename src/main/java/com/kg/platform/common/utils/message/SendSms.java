package com.kg.platform.common.utils.message;

import java.io.Serializable;
import java.net.URLEncoder;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.exception.ParamException;
import com.kg.platform.common.utils.HttpUtil;
import com.kg.platform.model.request.SmsSendOutRequest;
import com.kg.platform.model.request.SmsSendRequest;
import com.kg.platform.model.response.SmsSendOutResponse;
import com.kg.platform.model.response.SmsSendResponse;

/**
 * ClassName: SendSms
 * 
 */

public class SendSms implements Serializable {

    private static final long serialVersionUID = -4588923720114655613L;

    private static final Logger logger = LoggerFactory.getLogger(SendSms.class);

    /**
     * 发送国内短信
     * 
     */
    public SmsSendResponse send(String phone, String msg) {
        String report = "true";

        SmsSendRequest smsSingleRequest = new SmsSendRequest(this.getAccount(), this.getPassword(), msg, phone, report);

        String requestJson = JSON.toJSONString(smsSingleRequest);

        logger.info("before request string is: " + requestJson);

        logger.info("account: {} password: {} url: {} ", this.getAccount(), this.getPassword(), this.getUrl());

        String response = SmsUtil.sendSmsByPost(url, requestJson);

        logger.info("response after request result is :" + response);

        SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);

        logger.info("response toString is :" + smsSingleResponse);

        return smsSingleResponse;

    }

    /**
     * 发送国际短信（创蓝旧接口有误，使用他们给的新接口）
     * 
     */
    public void sendInternationalMessage(String phone, String content) {
        CloseableHttpClient client = HttpClients.createDefault();
        String encodedContent = null;
        try {
            encodedContent = URLEncoder.encode(content, "utf-8");

            StringBuffer strBuf = new StringBuffer();

            if (StringUtils.isEmpty(phone)) {
                logger.error("发送国际短信时手机号入参为空");
                throw new ParamException(ExceptionEnum.PARAMEMPTYERROR);
            }

            if (phone.indexOf(",") != -1) {
                strBuf.append(this.getOuturlbatch());
            } else {
                strBuf.append(this.getOuturl());
            }

            strBuf.append("?un=").append(this.getOutaccount());
            strBuf.append("&pw=").append(this.getOutpassword());
            strBuf.append("&da=").append(phone);
            strBuf.append("&sm=").append(encodedContent);
            strBuf.append("&dc=15&rd=1&rf=2&tf=3");
            HttpGet get = new HttpGet(strBuf.toString());

            logger.info("发送国际短信入参： {}", JSON.toJSONString(get));

            CloseableHttpResponse response = client.execute(get);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                logger.info("发送国际短信返回值： {}", EntityUtils.toString(response.getEntity()));
            }
        } catch (Exception e) {
            logger.error("创蓝接口发送短信失败： {}", e.getMessage());
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (Exception e) {
                    logger.error("发送短信接口关闭失败： {}", e.getMessage());
                }
            }
        }
        return;
    }

    /**
     * 发送国际短信
     * 
     */
    @Deprecated
    public SmsSendOutResponse sendOut(String mobile, String msg) {

        SmsSendOutRequest smsSingleRequest = new SmsSendOutRequest(this.getOutaccount(), this.getOutpassword(), msg,
                mobile);

        String requestJson = JSON.toJSONString(smsSingleRequest);

        logger.info("请求参数为:" + requestJson);

        logger.info("account: {} password: {} url: {} ", this.getOutaccount(), this.getOutpassword(), this.getOuturl());

        String result = HttpUtil.post(this.getOuturl(), requestJson);

        logger.info("返回参数为:" + result);

        SmsSendOutResponse smsSingleResponse = JSON.parseObject(result, SmsSendOutResponse.class);

        logger.info("状态码:" + smsSingleResponse.getCode() + ",状态码说明:" + smsSingleResponse.getError() + ",消息id:"
                + smsSingleResponse.getMsgid());

        return smsSingleResponse;
    }

    private String url;

    private String account;

    private String password;

    private String outurl;

    private String outurlbatch;

    private String outaccount;

    private String outpassword;

    public SendSms() {
    }

    public SendSms(SendSms ss) {
        this.url = ss.url;
        this.account = ss.account;
        this.password = ss.password;

        this.outurl = ss.outurl;
        this.outurlbatch = ss.outurlbatch;
        this.outaccount = ss.outaccount;
        this.outpassword = ss.outpassword;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOuturl() {
        return outurl;
    }

    public void setOuturl(String outurl) {
        this.outurl = outurl;
    }

    public String getOuturlbatch() {
        return outurlbatch;
    }

    public void setOuturlbatch(String outurlbatch) {
        this.outurlbatch = outurlbatch;
    }

    public String getOutaccount() {
        return outaccount;
    }

    public void setOutaccount(String outaccount) {
        this.outaccount = outaccount;
    }

    public String getOutpassword() {
        return outpassword;
    }

    public void setOutpassword(String outpassword) {
        this.outpassword = outpassword;
    }

    public static void main(String[] args) {
        // SendSms.sendsms("18581359701,13183814258", "【KG财经】来自KG财经的测试短信");

        logger.error("eerrrr");
    }
}
