package com.kg.platform.service.admin.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.exception.ExceptionEnum;
import com.kg.platform.dao.entity.SysUser;
import com.kg.platform.dao.read.ArticleRMapper;
import com.kg.platform.model.request.admin.ProfileVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kg.platform.dao.entity.Column;
import com.kg.platform.dao.read.ColumnRMapper;
import com.kg.platform.dao.read.admin.AdminTreeRMapper;
import com.kg.platform.dao.write.ColumnWMapper;
import com.kg.platform.model.in.ColumnInModel;
import com.kg.platform.model.out.ColumnOutModel;
import com.kg.platform.model.request.admin.ColumnEditRequest;
import com.kg.platform.model.response.admin.TreeQueryResponse;
import com.kg.platform.service.admin.ColumnService;
import com.kg.platform.tree.TreeUtils;

@Service("AdminColumnService")
public class ColumnServiceImpl implements ColumnService {

    @Autowired
    private AdminTreeRMapper adminTreeRMapper;

    @Autowired
    private ColumnWMapper columnWMapper;

    @Autowired
    private ColumnRMapper columnRMapper;

    @Autowired
    private ArticleRMapper articleRMapper;

    @Override
    public List<TreeQueryResponse> getColumnList() {
        List<TreeQueryResponse> list = adminTreeRMapper.selectColumn();
        list = TreeUtils.convert2Tree(list, TreeQueryResponse.class, null);
        if (null != list && list.size() > 0) {
            list.stream().forEach(item -> {
                List<TreeQueryResponse> children = item.getChildren();
                if (null != children && children.size() > 0) {
                    for (TreeQueryResponse child : children) {
                        child.setArticleCount(child.getSecondArticleCount());
                    }
                    item.setChildren(children);
                }
            });
        }
        return list;
    }

    @Override
    public boolean addColumn(ColumnEditRequest request,SysUser sysUser) {
        if(sysUser == null || sysUser.getSysUserId() == null){
            return false;
        }
        if (null == request.getColumnId()) {
            ColumnInModel model = new ColumnInModel();
            model.setName(request.getName());
            List<ColumnOutModel> list = columnRMapper.selectColumnByName(model);
            if (null != list && list.size() > 0) {
                return false;
            }
            Column column = new Column();
            column.setPrevColumn(request.getParentId());
            column.setColumnName(request.getName());
            column.setNavigatorDisplay(request.getNavigatorDisplay());
            column.setDisplayStatus(request.getDisplayStatus());
            column.setColumnOrder(request.getOrder());
            column.setDisplayMode(request.getDisplayMode());
            column.setSeoTitle(request.getTitle());
            column.setSeoKeyword(request.getKeyword());
            column.setSeoDescription(request.getDescription());
            column.setCreateDate(new Date());
            column.setCreateUser(Integer.parseInt(sysUser.getSysUserId()+""));
            column.setUpdateUser(Integer.parseInt(sysUser.getSysUserId()+""));
            column.setColumnLevel(request.getParentId() == 0 ? 1 : 2);
            column.setColumnUrlname(request.getUrlname());
            columnWMapper.insertSelective(column);
            return true;
        } else {
            ColumnInModel model = new ColumnInModel();
            model.setName(request.getName());
            List<ColumnOutModel> list = columnRMapper.selectColumnByName(model);
            if (null != list && list.size() > 0) {
                Optional<ColumnOutModel> optional = list.stream()
                        .filter(item -> !item.getColumnName().equals(request.getName())).findFirst();
                if (optional.isPresent()) {
                    return false;
                }
            }
            Column column = new Column();
            column.setColumnId(request.getColumnId());
            column.setPrevColumn(request.getParentId());
            column.setColumnName(request.getName());
            column.setNavigatorDisplay(request.getNavigatorDisplay());
            column.setDisplayStatus(request.getDisplayStatus());
            column.setColumnOrder(request.getOrder());
            column.setDisplayMode(request.getDisplayMode());
            column.setSeoTitle(request.getTitle());
            column.setSeoKeyword(request.getKeyword());
            column.setSeoDescription(request.getDescription());
            column.setUpdateDate(new Date());
            column.setUpdateUser(Integer.parseInt(sysUser.getSysUserId()+""));
            column.setColumnLevel(request.getParentId() == 0 ? 1 : 2);
            column.setColumnUrlname(request.getUrlname());
            columnWMapper.updateByPrimaryKeySelective(column);
            return true;
        }
    }

    @Override
    public JsonEntity deleteColumn(ColumnEditRequest request) {
        //删除前 判断该栏目下吃是否存在文章 如果存在 则删除失败
        JsonEntity existJson = checkColumnIsExistArticle(request);
        if(!existJson.getCode().equals(ExceptionEnum.SUCCESS.getCode())){
            return existJson;
        }
        Boolean delSuccess = columnWMapper.deleteByPrimaryKey(request.getColumnId()) > 0;
        if(delSuccess){
            return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS);
        }else{
            return new JsonEntity("删除失败,不存在的栏目id","500");
        }
    }

    private JsonEntity checkColumnIsExistArticle(ColumnEditRequest request) {
        if(request == null || request.getColumnId() == null){
            return new JsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        Long cnt = articleRMapper.countColumnArticleCnt(request.getColumnId());
        if(cnt == 0){
            return new JsonEntity(ExceptionEnum.SUCCESS);
        }else{
            return new JsonEntity(ExceptionEnum.USER_ARTICLECOUNT);
        }
    }

    @Override
    public ColumnOutModel getColumnById(ColumnEditRequest request) {
        return columnRMapper.selectByColumnKey(request.getColumnId());
    }

    @Override
    public JsonEntity setProfileAttr(ProfileVO profileVO) {
        if(StringUtils.isEmpty(profileVO.getType())){
            return new JsonEntity(ExceptionEnum.PARAMEMPTYERROR);
        }
        columnRMapper.updateGlobalProfile(profileVO.getCnt(),profileVO.getType());
        return new JsonEntity(ExceptionEnum.SUCCESS);
    }

    @Override
    public JsonEntity getProfileAttr() {
        return JsonEntity.makeSuccessJsonEntity(columnRMapper.getProfile("personage_column"));
    }

}
