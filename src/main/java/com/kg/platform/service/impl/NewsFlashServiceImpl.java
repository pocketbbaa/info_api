package com.kg.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.common.utils.HtmlUtil;
import com.kg.platform.common.utils.JedisKey;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.dao.read.KgNewsFlashRMapper;
import com.kg.platform.enumeration.NewsFlashTypeEnum;
import com.kg.platform.model.in.KgNewsFlashInModel;
import com.kg.platform.model.out.KgNewsFlashOutModel;
import com.kg.platform.model.request.KgNewsFlashRequest;
import com.kg.platform.model.response.KgNewsFlashResponse;
import com.kg.platform.service.ArticlekgService;
import com.kg.platform.service.NewsFlashService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Administrator on 2018/3/13.
 */
@Service
@Transactional
public class NewsFlashServiceImpl implements NewsFlashService {
    private static final Logger logger = LoggerFactory.getLogger(NewsFlashServiceImpl.class);

    @Inject
    KgNewsFlashRMapper kgNewsFlashRMapper;
    @Autowired
    protected JedisUtils jedisUtils;
    @Autowired
    private ArticlekgService articlekgService;

    /**
     * 24H快讯页
     *
     * @param request
     * @param page
     * @return
     */
    @Override

    public Map<String,Object> getNewsFlashListByType(KgNewsFlashRequest request, PageModel<KgNewsFlashResponse> page, HttpServletRequest servletRequest) {
        logger.info("24H快讯前端入参：{},分页入参：{}", JSON.toJSONString(request), JSON.toJSONString(page));



        Map<String,Object> map = new HashMap();
        KgNewsFlashInModel inModel = new KgNewsFlashInModel();
        inModel.setNewsflashType(request.getNewsflashType());
        long count = kgNewsFlashRMapper.selectCountNewsFlash(inModel);
        long totalCount = articlekgService.getArticleColumnTotalCount(servletRequest,page.getCurrentPage(),"getNewsFlashListByType"+request.getNewsflashType().toString(),count);

        page.setTotalNumber(totalCount);
        //判断是否超过总页数
        if(page.getCurrentPage()>page.getTotalPage()){
            page.setData(null);
            map.put("page",page);
            return map;
        }

        //计算limit下标
        int startIndex = articlekgService.getArticleRecommStartIndex(page.getCurrentPage(),Integer.parseInt(String.valueOf(totalCount)),page.getPageSize());
        inModel.setStart(startIndex);
        if(startIndex<0){
            inModel.setStart(0);
            inModel.setLimit((int)totalCount%page.getPageSize());
        }else {
            inModel.setLimit(page.getPageSize());
        }



        Date now = new Date();
        inModel.setCreateDate(now);

        List<KgNewsFlashOutModel> kgNewsFlashOutModelList = kgNewsFlashRMapper.selectByType(inModel);
        //将列表顺讯转换
        Collections.reverse(kgNewsFlashOutModelList);

        List<KgNewsFlashResponse> listrResponse = new ArrayList<KgNewsFlashResponse>();
        String noLink = "链向财经,金色财经,和讯网,界面新闻,财新一线,华尔街见闻,东方财富网";
        for (KgNewsFlashOutModel newsFlashOutModel : kgNewsFlashOutModelList) {
            KgNewsFlashResponse response = new KgNewsFlashResponse();
            response.setNewsflashId(newsFlashOutModel.getNewsflashId().toString());
            response.setNewsflashTitle(newsFlashOutModel.getNewsflashTitle());
            String newsflashText = newsFlashOutModel.getNewsflashText();
            if(newsFlashOutModel.getNewsflashOrigin()==0){
            	//系统抓取的快讯去掉HTML标签 人工添加的不需要去掉
				newsflashText = HtmlUtil.delHTMLTag(newsflashText);
			}
			response.setNewsflashText(newsflashText);
            if(StringUtils.isNotBlank(newsFlashOutModel.getRemark())){
                if(!noLink.contains(newsFlashOutModel.getRemark())){
                    response.setNewsflashLink(newsFlashOutModel.getNewsflashLink());
                }
            }else {
                response.setNewsflashLink(newsFlashOutModel.getNewsflashLink());
            }
            response.setCreateDate(DateUtils.calculateTime(newsFlashOutModel.getCreateDate()));
            response.setRemark(newsFlashOutModel.getRemark());
            response.setTimeStamp(String.valueOf(newsFlashOutModel.getCreateDate().getTime()));
            response.setNewsflashBottomImg(newsFlashOutModel.getNewsflashBottomImg());
            response.setLevel(newsFlashOutModel.getLevel());
            listrResponse.add(response);
        }

        page.setData(listrResponse);
        map.put("requestTime",String.valueOf(now.getTime()));
        map.put("page",page);
        return map;
    }

    /**
     * 首页24H快讯
     *
     * @return
     */
    @Override
    public Map<String,Object> getNewsFlashList() {
        KgNewsFlashInModel inModel = new KgNewsFlashInModel();
        Date now = new Date();
        inModel.setCreateDate(now);
        List<KgNewsFlashOutModel> kgNewsFlashOutModelList = kgNewsFlashRMapper.selectIndexFlash(inModel);
        List<KgNewsFlashResponse> response = new ArrayList<>();
        String noLink = "链向财经,金色财经,和讯网,界面新闻,财新一线,华尔街见闻,证券时报网·快讯,东方财富网";
        for (KgNewsFlashOutModel outModel : kgNewsFlashOutModelList) {
            KgNewsFlashResponse kgNewsFlashResponse = new KgNewsFlashResponse();
            kgNewsFlashResponse.setNewsflashId(outModel.getNewsflashId().toString());
            kgNewsFlashResponse.setNewsflashTitle(outModel.getNewsflashTitle());
            kgNewsFlashResponse.setNewsflashText(HtmlUtil.delHTMLTag(outModel.getNewsflashText()));
            if(StringUtils.isNotBlank(outModel.getRemark())){
                if(!noLink.contains(outModel.getRemark())){
                    kgNewsFlashResponse.setNewsflashLink(outModel.getNewsflashLink());
                }
            }else {
                kgNewsFlashResponse.setNewsflashLink(outModel.getNewsflashLink());
            }
            kgNewsFlashResponse.setCreateDate(DateUtils.calculateTime(outModel.getCreateDate()));
            kgNewsFlashResponse.setRemark(outModel.getRemark());
            kgNewsFlashResponse.setNewsflashBottomImg(outModel.getNewsflashBottomImg());
            kgNewsFlashResponse.setLevel(outModel.getLevel());
            response.add(kgNewsFlashResponse);
        }
        Map<String,Object> map = new HashMap();
        map.put("requestTime",String.valueOf(now.getTime()));
        map.put("newsFlashList",response);
        return map;
    }

    /**
     * 24h快讯详情
     *
     * @return
     */
    @Override
    public Result<KgNewsFlashResponse> getNewsFlashDetail(KgNewsFlashRequest request) {
        String noLink = "链向财经,金色财经,和讯网,界面新闻,财新一线,华尔街见闻,证券时报网·快讯,东方财富网";
        logger.info("24H快讯详情前端入参：{}", JSON.toJSONString(request));
        KgNewsFlashOutModel kgNewsFlashOutModel = kgNewsFlashRMapper.selectNewsFlashDetail(request.getNewsflashId());
        if(kgNewsFlashOutModel==null){
            return new Result<>();
        }
        KgNewsFlashResponse response = new KgNewsFlashResponse();
        response.setNewsflashId(kgNewsFlashOutModel.getNewsflashId().toString());
        response.setNewsflashTitle(kgNewsFlashOutModel.getNewsflashTitle());
        response.setNewsflashText(HtmlUtil.delHTMLTag(kgNewsFlashOutModel.getNewsflashText()));
        if(StringUtils.isNotBlank(kgNewsFlashOutModel.getRemark())){
            if(!noLink.contains(kgNewsFlashOutModel.getRemark())){
                response.setNewsflashLink(kgNewsFlashOutModel.getNewsflashLink());
            }
        }else{
            response.setNewsflashLink(kgNewsFlashOutModel.getNewsflashLink());
        }
        response.setRemark(kgNewsFlashOutModel.getRemark());
        response.setNewsflashType(kgNewsFlashOutModel.getNewsflashType());
        response.setCreateDate(DateUtils.calculateTime(kgNewsFlashOutModel.getCreateDate()));
        response.setNewsflashBottomImg(kgNewsFlashOutModel.getNewsflashBottomImg());
        response.setLevel(kgNewsFlashOutModel.getLevel());
        response.setTimeStamp(String.valueOf(kgNewsFlashOutModel.getCreateDate().getTime()));
        return new Result<>(response);
    }

    /**
     * 24h快讯未读数量
     *
     * @return
     */
    @Override
    public Result<Long> getNumberUnread(KgNewsFlashRequest request) {
        //从缓存中取出对应类型的24H快讯
        int flashType = request.getNewsflashType();
        long requetsTime = Long.valueOf(request.getCreateDate());
        long unreadNumber = 0;
        String key = "";
        String typeString ="";
        if(flashType==-1){
            //取出全部类型的快讯
            key = "allCount";
            typeString = jedisUtils.get(JedisKey.getNewsFlashKey("allCount"));
        }else {
            key = flashType==0?"qukuailianCount":(flashType==1?"jinrongCount":"gushiCount");
            typeString = jedisUtils.get(JedisKey.getNewsFlashKey(key));
        }
        if(StringUtils.isNotBlank(typeString)){
            String[] allString = typeString.split("_");
            long dateTime = new Date(Long.parseLong(allString[1])).getTime();
            unreadNumber = requetsTime<dateTime?Long.valueOf(allString[0]):0;
        }
        return new Result<Long>(unreadNumber);
    }

    @Override
    public AppJsonEntity getNewsFlashTopMenus() {
        List<KgNewsFlashResponse> response = new ArrayList<>();
        KgNewsFlashResponse qklkxresponse = new KgNewsFlashResponse();
        qklkxresponse.setNewsflashType(NewsFlashTypeEnum.QKLKX.getStatus());
        qklkxresponse.setRemark(NewsFlashTypeEnum.QKLKX.getDisplay());

        KgNewsFlashResponse jrkxresponse = new KgNewsFlashResponse();
        jrkxresponse.setNewsflashType(NewsFlashTypeEnum.JRKX.getStatus());
        jrkxresponse.setRemark(NewsFlashTypeEnum.JRKX.getDisplay());

        KgNewsFlashResponse gskxresponse = new KgNewsFlashResponse();
        gskxresponse.setNewsflashType(NewsFlashTypeEnum.GSKX.getStatus());
        gskxresponse.setRemark(NewsFlashTypeEnum.GSKX.getDisplay());

        KgNewsFlashResponse allresponse = new KgNewsFlashResponse();
        allresponse.setNewsflashType(NewsFlashTypeEnum.ALLKX.getStatus());
        allresponse.setRemark(NewsFlashTypeEnum.ALLKX.getDisplay());

        response.add(allresponse);
        response.add(qklkxresponse);
        response.add(jrkxresponse);
        response.add(gskxresponse);
        Map map = new HashMap();
        map.put("newsFlashColumns",response);
        return AppJsonEntity.makeSuccessJsonEntity(map);
    }

    @Override
    public Result<List<KgNewsFlashResponse>> websocketNewsFlash() {
        List<KgNewsFlashResponse> responses = new ArrayList<>();
        //从缓存中拿取websocket放入的快讯数据
        String result = jedisUtils.get(JedisKey.getIndexNewsFlashKey());
        if(StringUtils.isNotBlank(result)){
            responses = JSON.parseObject(result,List.class);
        }else {
            //如果为空，从数据库中查询最新3条快讯数据
            List<KgNewsFlashOutModel> list = kgNewsFlashRMapper.getThreeNewNewsFlash();
            for (KgNewsFlashOutModel outModel:list) {
                KgNewsFlashResponse response = new KgNewsFlashResponse();
                response.setNewsflashId(outModel.getNewsflashId().toString());
                response.setNewsflashTitle(outModel.getNewsflashTitle());
                response.setNewsflashText(HtmlUtil.delHTMLTag(outModel.getNewsflashText()));
                response.setLevel(outModel.getLevel());
                response.setNewsflashBottomImg(outModel.getNewsflashBottomImg());
                response.setCreateDate(DateUtils.calculateTime(outModel.getCreateDate()));
                responses.add(response);
            }
        }
        return new Result<>(responses);
    }
}
