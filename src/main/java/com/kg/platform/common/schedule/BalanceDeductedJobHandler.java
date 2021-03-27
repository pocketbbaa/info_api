package com.kg.platform.common.schedule;

import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.dao.entity.Account;
import com.kg.platform.dao.entity.BalanceDeducted;
import com.kg.platform.dao.read.AccountRMapper;
import com.kg.platform.dao.read.BalanceDeductedRMapper;
import com.kg.platform.dao.write.AccountWMapper;
import com.kg.platform.dao.write.BalanceDeductedWMapper;
import com.kg.platform.model.in.AccountInModel;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 扣除平台账户余额定时
 */
@Component
@JobHander(value = "balanceDeductedJobHandler")
public class BalanceDeductedJobHandler extends IJobHandler {

    @Autowired
    private AccountRMapper accountRMapper;
    @Autowired
    private AccountWMapper accountWMapper;
    @Autowired
    private BalanceDeductedRMapper balanceDeductedRMapper;
    @Autowired
    private BalanceDeductedWMapper balanceDeductedWMapper;

    @Override
    public ReturnT<String> execute(String... strings) throws Exception {
        Date yestaday = DateUtils.getBeforeDay(new Date(), 1);
        String start = DateUtils.formatYMDHMS(DateUtils.getDateStart(yestaday));
        String end = DateUtils.formatYMDHMS(DateUtils.getDateEnd(yestaday));
        XxlJobLogger.log("扣除平台账户余额定时任务 -> start:" + start + "  end:" + end);
        BalanceDeducted balanceDeducted = balanceDeductedRMapper.getBalanceByTime(start, end);
        if (balanceDeducted == null) {
            XxlJobLogger.log("start:" + start + "  end:" + end + "  没有待扣除的余额");
            return ReturnT.SUCCESS;
        }

        BigDecimal amount = balanceDeducted.getAmount();
        Account account = accountRMapper.selectByUserId(balanceDeducted.getUserId());
        BigDecimal kgBalance = account.getTxbBalance();
        if (kgBalance.compareTo(amount) < 0) {
            XxlJobLogger.log("平台账户余额不足 - kgBalance：" + kgBalance + "  应扣除余额：" + amount);
            return ReturnT.SUCCESS;
        }

        AccountInModel inModel = new AccountInModel();
        inModel.setUserId(balanceDeducted.getUserId());
        inModel.setBalance(amount);
        XxlJobLogger.log("inModel:" + JSONObject.toJSONString(inModel));
        int success = accountWMapper.reduceTxbBalance(inModel);
        XxlJobLogger.log("start:" + start + "  end:" + end + "  inModel:" + JSONObject.toJSONString(inModel) + "  扣除余额 success：" + success);
        if (success > 0) {
            balanceDeductedWMapper.updateState(1, start, end);
        }
        return ReturnT.SUCCESS;
    }

}
