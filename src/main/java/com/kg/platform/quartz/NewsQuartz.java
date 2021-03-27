package com.kg.platform.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.kg.platform.service.SnacthService;

/**
 * Created by Administrator on 2018/1/8.
 */
public class NewsQuartz extends QuartzJobBean {
    @Autowired
    private SnacthService snacthService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        snacthService.snatchNews();
    }

    public void setSnacthService(SnacthService snacthService) {
        this.snacthService = snacthService;
    }
}
