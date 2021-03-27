package com.kg.platform.service.admin.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.exception.ParamException;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.dao.entity.Feedback;
import com.kg.platform.dao.read.admin.AdminFeedbackRMapper;
import com.kg.platform.dao.write.FeedbackWMapper;
import com.kg.platform.model.MailModel;
import com.kg.platform.model.request.admin.FeedbackQueryRequest;
import com.kg.platform.model.response.admin.FeedbackQueryResponse;
import com.kg.platform.service.admin.FeedbackService;

@Service("AdminFeedbackService")
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private AdminFeedbackRMapper adminFeedbackRMapper;

    @Autowired
    private FeedbackWMapper feedbackWMapper;

    @Inject
    private JavaMailSender javaMailSender;

    @Inject
    private SimpleMailMessage simpleMailMessage;

    @Inject
    private TaskExecutor taskExecutor;

    @Override
    public PageModel<FeedbackQueryResponse> getFeedbackList(FeedbackQueryRequest request) {
        request.setStart((request.getCurrentPage() - 1) * request.getPageSize());
        request.setLimit(request.getPageSize());
        Date end = request.getEndDate();
        if (null != end) {
            // 设置时分秒
            request.setEndDate(DateUtils.getDateEnd(end));
        }
        List<FeedbackQueryResponse> list = adminFeedbackRMapper.selectByCondition(request);
        if (null != list && list.size() > 0) {
            list.stream().forEach(item -> {
                if (item.getStatus()) {
                    item.setStatusDisplay("已读");
                } else {
                    item.setStatusDisplay("未读");
                }
            });
        }
        long count = adminFeedbackRMapper.selectCountByCondition(request);
        PageModel<FeedbackQueryResponse> model = new PageModel<>();
        model.setData(list);
        model.setTotalNumber(count);
        model.setCurrentPage(request.getCurrentPage());
        model.setPageSize(request.getPageSize());
        return model;
    }

    @Override
    public boolean deleteFeedback(FeedbackQueryRequest request) {
        List<Long> idList = StringUtils.convertString2LongList(request.getFeedbackId(), ",");
        idList.stream().forEach(id -> feedbackWMapper.deleteByPrimaryKey(id));
        return true;
    }

    @Override
    public boolean setStatus(FeedbackQueryRequest request) {
        List<Long> idList = StringUtils.convertString2LongList(request.getFeedbackId(), ",");
        idList.stream().forEach(id -> {
            Feedback feedback = new Feedback();
            feedback.setFeedbackId(id);
            feedback.setFeedbackStatus(true);
            feedbackWMapper.updateByPrimaryKeySelective(feedback);
        });
        return true;
    }

    @Override
    public boolean sendEmail(FeedbackQueryRequest request) {
        MailModel mail = new MailModel();
        // 主题
        // mail.setSubject("清单");
        mail.setSubject(request.getTitle());
        mail.setToEmails(request.getEmail());
        // 附件
        Map<String, String> attachments = new HashMap<String, String>();
        // attachments.put("清单.xlsx", excelPath + "清单.xlsx");
        mail.setAttachments(attachments);

        mail.setContent(request.getContent());

        // sendEmail(mail);

        this.taskExecutor.execute(new FeedbackServiceImpl.SendMailThread(mail));
        return true;
    }

    private class SendMailThread implements Runnable {

        private MailModel mail;

        private SendMailThread(MailModel email) {
            super();
            this.mail = email;

        }

        @Override
        public void run() {

            sendEmail(mail);

        }

    }

    /**
     * 发送邮件
     */
    public boolean sendEmail(MailModel mail) {
        // 建立邮件消息
        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper messageHelper;
        try {
            messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            // 设置发件人邮箱
            if (mail.getEmailFrom() != null) {
                messageHelper.setFrom(mail.getEmailFrom());
            } else {
                messageHelper.setFrom(simpleMailMessage.getFrom());
            }

            // 设置收件人邮箱
            if (mail.getToEmails() != null) {
                String[] toEmailArray = mail.getToEmails().split(";");
                List<String> toEmailList = new ArrayList<String>();
                if (null == toEmailArray || toEmailArray.length <= 0) {
                    throw new ParamException(ExceptionEnum.EMAIL_EMPTY);
                } else {
                    for (String s : toEmailArray) {
                        if (s != null && !s.equals("")) {
                            toEmailList.add(s);
                        }
                    }
                    if (null == toEmailList || toEmailList.size() <= 0) {
                        throw new ParamException(ExceptionEnum.EMAIL_EMPTY);
                    } else {
                        toEmailArray = new String[toEmailList.size()];
                        for (int i = 0; i < toEmailList.size(); i++) {
                            toEmailArray[i] = toEmailList.get(i);
                        }
                    }
                }
                messageHelper.setTo(toEmailArray);
            } else {
                messageHelper.setTo(simpleMailMessage.getTo());
            }

            // 邮件主题
            if (mail.getSubject() != null) {
                messageHelper.setSubject(mail.getSubject());
            } else {

                messageHelper.setSubject(simpleMailMessage.getSubject());
            }

            // true 表示启动HTML格式的邮件
            messageHelper.setText(mail.getContent(), true);

            // 添加图片
            if (null != mail.getPictures()) {
                for (Iterator<Map.Entry<String, String>> it = mail.getPictures().entrySet().iterator(); it.hasNext();) {
                    Map.Entry<String, String> entry = it.next();
                    String cid = entry.getKey();
                    String filePath = entry.getValue();
                    if (null == cid || null == filePath) {
                        throw new RuntimeException("请确认每张图片的ID和图片地址是否齐全！");
                    }

                    File file = new File(filePath);
                    if (!file.exists()) {
                        throw new RuntimeException("图片" + filePath + "不存在！");
                    }

                    FileSystemResource img = new FileSystemResource(file);
                    messageHelper.addInline(cid, img);
                }
            }

            // 添加附件
            if (null != mail.getAttachments()) {
                for (Iterator<Map.Entry<String, String>> it = mail.getAttachments().entrySet().iterator(); it
                        .hasNext();) {
                    Map.Entry<String, String> entry = it.next();
                    String cid = entry.getKey();
                    String filePath = entry.getValue();
                    if (null == cid || null == filePath) {
                        throw new RuntimeException("请确认每个附件的ID和地址是否齐全！");
                    }

                    File file = new File(filePath);
                    if (!file.exists()) {
                        throw new RuntimeException("附件" + filePath + "不存在！");
                    }

                    FileSystemResource fileResource = new FileSystemResource(file);
                    messageHelper.addAttachment(cid, fileResource);
                }
            }
            messageHelper.setSentDate(new Date());
            // 发送邮件
            javaMailSender.send(message);

            return true;
        } catch (MessagingException e) {
            return false;
        }
    }

    @Override
    public boolean replay(FeedbackQueryRequest request) {
        List<Long> idList = StringUtils.convertString2LongList(request.getFeedbackId(), ",");
        idList.stream().forEach(id -> {
            Feedback feedback = new Feedback();
            feedback.setFeedbackId(id);
            feedback.setReplayInfo(request.getReplayInfo());
            feedbackWMapper.updateByPrimaryKeySelective(feedback);
        });
        return true;

    }
}
