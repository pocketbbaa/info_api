package com.kg.platform.controller;



import com.kg.platform.common.utils.JsonUtil;
import com.kg.platform.model.request.AccountRequest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountControllerTest extends BaseTest {

    @Autowired
    private AccountController accountController;

    @Test
    public void selectUserTxbBalance() {
        AccountRequest request=new AccountRequest();
        request.setUserId(433657149114163200L);
        System.out.println(">>>>>>>>>返回值>>>>>>>>>>>>"+JsonUtil.writeValueAsString(accountController.selectUserTxbBalance(request)));
    }


}
