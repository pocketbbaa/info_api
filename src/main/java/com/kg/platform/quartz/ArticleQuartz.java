package com.kg.platform.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.kg.platform.service.QuartzService;

public class ArticleQuartz extends QuartzJobBean {

    private QuartzService quartzService;

    /**
     * 自动发布定时任务，每分钟执行一次
     */
    @Override
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
        quartzService.autoPublishArticle();
    }

    public void setQuartzService(QuartzService quartzService) {
        this.quartzService = quartzService;
    }
}
