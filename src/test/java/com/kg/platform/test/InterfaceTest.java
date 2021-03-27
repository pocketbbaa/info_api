package com.kg.platform.test;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.common.utils.sensitivefilter.StringPointer;
import com.kg.platform.model.in.UserWithdrawInModel;
import com.kg.platform.model.request.UserChargeRequest;
import com.kg.platform.service.ColumnService;
import com.kg.platform.service.UserChargeService;
import com.kg.platform.service.UserWithdrawService;

/**
 * 后台接口单元测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/spring-context*.xml" })
public class InterfaceTest {
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(InterfaceTest.class);

    @Inject
    UserChargeService rechargeService;

    @Inject
    UserWithdrawService userWithdrawService;

    @Inject
    ColumnService columnService;

    @Inject
    protected JedisUtils jedisUtils;

    // @Inject
    // private IDGenerater idGenerater;
    //
    // @Inject
    // private SendSms sendSms;

    @Test
    public void test() {
        try {

            UserChargeRequest request = new UserChargeRequest();
            request.setAccountAmount(new BigDecimal(10));
            request.setFromAddress("TVBP8HSx8UmZ94sjkRAgCKCCEigCvWa4W5r"); // 第三方钱包地址
            request.setRechargeAmount(new BigDecimal(10));
            request.setStatus(0);
            request.setToAddress("TVPamVGAAfMUPba4jqabtkPmD3T9W2hdiGR"); // kg钱包地址
            request.setTxId("65d77b9b84f5243b4c8f7da3150334d584f88cdf");
            request.setUserId("388713531081629696");

            // boolean success = rechargeService.recharge(request);

            UserWithdrawInModel inModel = new UserWithdrawInModel();
            inModel.setWithdrawAmount(BigDecimal.valueOf(49.9));
            inModel.setTxAddress("TV6oztnz5iUiAtU9ToGz7rvJui4op9hotN4"); // 提现地址
            inModel.setWithdrawFlowId(390583291696259072L);

            // userWithdrawService.withdraw(inModel);

            // ColumnRequest crequest = new ColumnRequest();
            // crequest.setColumnId(2);
            // System.err.println("==============" +
            // JSON.toJSONString(columnService.getSecondColumn(crequest)));
            //
            // Set<String> userKeys = jedisUtils.keys("*userTokenKey*");
            // if (userKeys != null && userKeys.size() > 0) {
            // for (String s : userKeys) {
            // System.err.println(jedisUtils.get(s));
            // }
            // }
            // String mobiles =
            // "13183814258,18384211995,13551114465,18580533917,13994119778,13798914320,15924377744,15577920476,18353735587,15994126635,13146826789,13905693841,13465730501,15728048158,18566196635,15399420686,18957855325,13275757756,15250498451,13967446970,15549443186,18800202596,15228831220";

            // String mobiles =
            // "18581359701,13183814258,18384211995,13551114465";
            // for (int i = 0; i < mobiles.length; i++) {
            // sendSms.send(mobiles,
            // "【千氪财经】尊敬的用户，您在《央行条法司研究者吴云：中国不应放弃数字货币高边疆》中的评论获得了15TV奖励。请登录千氪，查看明细！");
            // }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        String fileName = "aaa";
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        System.err.println(suffix);

        List<StringPointer> words = Lists.newArrayList();
        words.add(new StringPointer("aaaa"));
        words.add(new StringPointer("bbb"));
        words.add(new StringPointer("aaaccca"));
        System.err.println("======" + Joiner.on(",").join(words));

        String phoneNum = "17112345678";
        System.err.println("======" + phoneNum.startsWith("170"));

        BigDecimal bd = new BigDecimal(12345.67890);
        bd = bd.setScale(3, BigDecimal.ROUND_FLOOR);
        System.err.println(bd.toString());

        String reg = "(mp4|flv|avi|rm|rmvb|wmv)";
        Pattern p = Pattern.compile(reg);
        boolean boo = p.matcher("abc.png").find();
        System.err.println("=======" + boo);
    }
}