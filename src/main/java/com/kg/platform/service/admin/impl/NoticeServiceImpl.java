package com.kg.platform.service.admin.impl;

import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.common.utils.CommonService;
import com.kg.platform.dao.entity.KgAutherNotice;
import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.dao.read.admin.KgAutherNoticeRMapper;
import com.kg.platform.dao.write.admin.KgAutherNoticeWMapper;
import com.kg.platform.model.in.admin.NoticeListModel;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.admin.NoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {

    private static final Logger logger = LoggerFactory.getLogger(NoticeServiceImpl.class);

    @Autowired
    KgAutherNoticeWMapper kgAutherNoticeWMapper;
    @Autowired
    KgAutherNoticeRMapper kgAutherNoticeRMapper;
    @Autowired
    CommonService commonService;


    @Override
    public JsonEntity getNoticeInfoById(KgAutherNotice request) {
        if(request.getId() == null){
            return  new JsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }

        KgAutherNotice responseBody = kgAutherNoticeRMapper.selectByPrimaryKey(request.getId());
        if(responseBody == null){
            return new JsonEntity();
        }

        HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("title",responseBody.getTitle());
        result.put("info",responseBody.getInfo());
        result.put("id",responseBody.getId());

        return JsonEntity.makeSuccessJsonEntity(result);
    }

    @Override
    public JsonEntity addNoticeInfo(KgAutherNotice request, UserkgResponse kguser) {
        //参数检查
        ExceptionEnum exceptionEnum = doAfterCheckInfo(request);

        if(!("10000".equals(exceptionEnum.getCode()))){
            return new JsonEntity(exceptionEnum);
        }

        //获取用户名称
        SysUser sysUser = commonService.getTokenModelByUserId(Long.parseLong(request.getUserId()));
        if(sysUser == null){
            logger.error("user model is empty");
            return new JsonEntity(ExceptionEnum.FETCH_USER_FAIL);
        }

        //填充数据
        fillNoticeData(request,sysUser);
        //开始写入数据
        try {
            doSaveInfo(request);
        } catch (Exception e) {
            logger.error("insert notice info fail e:"+e.getMessage());
            return new JsonEntity(ExceptionEnum.SYS_EXCEPTION);
        }
        return new JsonEntity(ExceptionEnum.SUCCESS);
    }

    @Override
    public JsonEntity removeNotice(KgAutherNotice request) {
        if(request.getId()==null){
            return new JsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        kgAutherNoticeWMapper.deleteByPrimaryKey(request.getId());
        return new JsonEntity(ExceptionEnum.SUCCESS);
    }

    @Override
    public JsonEntity updateNotice(KgAutherNotice request) {
        if(request.getId() == null || request.getUserId() == null){
            return new JsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        if(request.getTitle() == null && request.getInfo() == null){
            return new JsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        SysUser sysUser = commonService.getTokenModelByUserId(Long.parseLong(request.getUserId()+""));
        if(sysUser == null){
            logger.error("user model is empty");
            return new JsonEntity(ExceptionEnum.FETCH_USER_FAIL);
        }
        request.setUpdateTime(CommonService.formatData(new Date(),"yyyy-MM-dd HH:mm"));
        request.setUpdateUser(sysUser.getSysUserName());

        kgAutherNoticeWMapper.updateByPrimaryKeySelective(request);
        return new JsonEntity(ExceptionEnum.SUCCESS);
    }

    @Override
    public JsonEntity getNoticeInfo(NoticeListModel noticeListModel) {
        HashMap<String,Object> itemMap = new HashMap<String,Object>();

        String sql = fetchSqlStr(noticeListModel);

        List<Object> result = kgAutherNoticeRMapper.execSqlList_sp(sql);
        itemMap.put("total",result.size());
        List<KgAutherNotice> kgAutherNotice = transModel(result);

        List datas = commonService.getPagingList(kgAutherNotice,noticeListModel.getCurrentPage(),noticeListModel.getPageSize());
        itemMap.put("data",datas);
        itemMap.put("currentPage",noticeListModel.getCurrentPage());
        itemMap.put("pageSize",noticeListModel.getPageSize());
        return JsonEntity.makeSuccessJsonEntity(itemMap);
    }

    private List<KgAutherNotice> transModel(List<Object> datas) {
        List<KgAutherNotice> result = new ArrayList<KgAutherNotice>();
        if(datas == null || datas.isEmpty()){
            return result;
        }

        HashMap dataMap =null;
        KgAutherNotice kgAutherNotice;
        for (Object obj : datas){
            if(obj instanceof HashMap){
                dataMap = (HashMap)obj;
            }
            if(dataMap ==null){
                continue;
            }
            kgAutherNotice = new KgAutherNotice();
            kgAutherNotice.setTitle(dataMap.get("title")+"");
            kgAutherNotice.setInfo(dataMap.get("info")+"");
            kgAutherNotice.setId(Long.parseLong(dataMap.get("id")+""));
            kgAutherNotice.setAddUser(dataMap.get("add_user")+"");
            kgAutherNotice.setAddTime(dataMap.get("add_time")+"");
            kgAutherNotice.setUpdateTime(commonService.strIsNull(dataMap.get("update_time")+"")? "":dataMap.get("update_time")+"");
            kgAutherNotice.setUpdateUser(commonService.strIsNull(dataMap.get("update_user")+"")? "":dataMap.get("update_user")+"");
            result.add(kgAutherNotice);
        }
        return result;
    }

    private String fetchSqlStr(NoticeListModel noticeListModel) {
        StringBuilder sql = new StringBuilder("SELECT * FROM kg_auther_notice");

        Boolean flag = false;
        if(noticeListModel.getAddUser() != null){
            sql.append(" WHERE add_user='")
                    .append(noticeListModel.getAddUser())
                    .append("'");
            flag = true;
        }
        if(noticeListModel.getUpdateUser() != null){
            if(flag){
                sql.append(" AND update_user='")
                        .append(noticeListModel.getUpdateUser())
                        .append("'");
            }else{
                sql.append(" WHERE update_user='")
                        .append(noticeListModel.getUpdateUser())
                        .append("'");
            }
        }
        sql.append(" ORDER BY add_time DESC");
        return sql.toString();
    }

    private void fillNoticeData(KgAutherNotice request,SysUser sysUser) {
        String time = CommonService.formatData(new Date(),"yyyy-MM-dd HH:mm");
        request.setAddTime(time);
        request.setTime(System.currentTimeMillis());
        request.setAddUser(sysUser.getSysUserName());
    }

    private ExceptionEnum doAfterCheckInfo(KgAutherNotice request) {
        if(request == null ||
                request.getInfo()==null||
                request.getTitle()==null ||
                request.getUserId() == null){
            return ExceptionEnum.PARAMEMPTYERROR;
        }
        return ExceptionEnum.SUCCESS;
    }

    private void doSaveInfo(KgAutherNotice request) {
        kgAutherNoticeWMapper.insert(request);
    }

}
