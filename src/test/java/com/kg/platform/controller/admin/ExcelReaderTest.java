package com.kg.platform.controller.admin;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.context.AnalysisContext;
import com.alibaba.excel.read.event.AnalysisEventListener;
import com.alibaba.fastjson.JSONObject;
import com.kg.platform.common.easyexcel.read.ExcelReaderFactory;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.controller.BaseTest;
import com.kg.platform.enumeration.RegistOriginEnum;
import com.kg.platform.enumeration.RegisterOriginEnum;
import com.kg.platform.enumeration.UserRoleEnum;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.UserkgRequest;
import com.kg.platform.service.UserkgService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelReaderTest extends BaseTest {

    @Autowired
    private UserkgService userkgService;

    @Test
    public void read() throws Exception {

        List<Map<String, String>> list = new ArrayList<>();
        try (InputStream in = new FileInputStream("/Users/pocketcoder/test.xlsx")) {
            AnalysisEventListener<List<String>> listener = new AnalysisEventListener<List<String>>() {

                @Override
                public void invoke(List<String> object, AnalysisContext context) {
                    Map<String, String> map = new HashMap<>();
                    System.err.println("Row:" + context.getCurrentRowNum() + " Data:" + object);
                    if (context.getCurrentRowNum() == 0) {
                        return;
                    }
                    String phone = object.get(0);
                    String amount = object.get(1);
                    String res = object.get(2);
                    UserkgRequest request = new UserkgRequest();
                    request.setUserMobile(phone);

                    UserkgOutModel outModel = userkgService.getUserInfo(request);
                    if (outModel == null) {
                        return;
                    }
                    map.put("userId", outModel.getUserId());
                    map.put("userName", outModel.getUserName());
                    map.put("phone", phone);
                    map.put("regist", RegistOriginEnum.getByCode(outModel.getRegisterOrigin()).getMessage());
                    map.put("createTime", DateUtils.formatDate(outModel.getCreateDate(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
                    map.put("role", UserRoleEnum.getUserRoleEnum(outModel.getUserRole()).getDisplay());
                    map.put("amount", amount);
                    map.put("res", res);
                    list.add(map);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                }
            };
            ExcelReader excelReader = ExcelReaderFactory.getExcelReader(in, null, listener);
            excelReader.read();

            System.err.println(JSONObject.toJSONString(list));
        }
    }

}
