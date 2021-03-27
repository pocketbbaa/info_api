package com.kg.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.utils.JedisKey;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.common.utils.StringUtils;
import com.kg.platform.dao.read.KgColumnCountRMapper;
import com.kg.platform.model.in.ArticleInModel;
import com.kg.platform.model.out.KgColumnCountOutModel;
import com.kg.platform.model.request.ArticleRequest;
import com.kg.platform.service.ArticlekgService;
import com.kg.platform.service.RefreshColumnArticle;
import com.kg.platform.service.app.HomePageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


/**
 * Created by Administrator on 2018/8/24.
 */
@Service
public class RefreshColumnArticleImpl implements RefreshColumnArticle {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KgColumnCountRMapper kgColumnCountRMapper;

    @Autowired
    private ArticlekgService articlekgService;

    @Autowired
    private JedisUtils jedisUtils;

    @Autowired
    private HomePageService homePageService;

    @Override
    public void refreshColumnArticle(Integer types,List<Integer> columnIds ) {

        Integer iType=types;
        types=2;
      /*  KgColumnCountOutModel indexOutModel = kgColumnCountRMapper.selectByPrimaryKey("-1");
        KgColumnCountOutModel videoTabInfoOutModel = kgColumnCountRMapper.selectByPrimaryKey("getVideoTabInfo");*/

        List<String> sColumnIds = Lists.newArrayList();
        if(columnIds!=null&&columnIds.size()>0){
            for (Integer columnId: columnIds) {
                if(columnId!=null){
                    sColumnIds.add(columnId.toString());
                }
            }
        }
        logger.info("refresh的MQ进入，sColumnIds包含的栏目ID有:{}------", JSON.toJSONString(sColumnIds));
        //获取当前所有栏目的KEY和文章总量
        long now = System.currentTimeMillis();
        List<KgColumnCountOutModel> kgColumnCountOutModels =  kgColumnCountRMapper.getAll();
        for (KgColumnCountOutModel outModel:kgColumnCountOutModels) {
            String key = "";
            String key1 = "";
            String key2 = "";
            String firstPageContent = "";
            if("-1".equals(outModel.getColumnKey())){
                key1 = JedisKey.getArticleWithPage(outModel.getColumnKey(),"WEB",(long)outModel.getArticleNum(),1);
                key2 = JedisKey.getArticleWithPage(outModel.getColumnKey(),"APP",(long)outModel.getArticleNum(),1);
              /*  String firstPageContent1 = jedisUtils.get(key1);
                String firstPageContent2 = jedisUtils.get(key2);*/
               /* if(StringUtils.isBlank(firstPageContent1)||StringUtils.isBlank(firstPageContent2)){
                   firstPageContent="";
                }else {
                    firstPageContent = jedisUtils.get(key1);
                }*/
            }else if("getVideoTabInfo".equals(outModel.getColumnKey())){
                key = JedisKey.getArticleWithPage(null,"getVideoTabInfo",(long)outModel.getArticleNum(),1);
//                firstPageContent = jedisUtils.get(key);
            }else {
                key = JedisKey.getArticleWithPage(outModel.getColumnKey(),"",(long)outModel.getArticleNum(),1);
//                firstPageContent = jedisUtils.get(key);
            }

            PageModel pageModel = new PageModel();
            List<String> exclude = Lists.newArrayList();
            //根据KEY获取到第一页的内容为空时 则主动获取对应栏目的第一页数据 并放入Redis
            if("-1".equals(outModel.getColumnKey())){
                //首页第一页的数据（APP和WEB是两个接口）
                articlekgService.buildSelectArticleRecomm(pageModel,outModel.getArticleNum(),types);
                articlekgService.buildSelectArticleAllResponse(pageModel,outModel.getArticleNum(),types);

                exclude.add(key1);
                exclude.add(key2);
                jedisUtils.delKeys(JedisKey.getArticlePageKeyPattern("-1"),exclude);
                //内容放入Redis后再更新对应栏目的Redis文章总量
                jedisUtils.set(JedisKey.getCountArticleNum("-1"),outModel.getArticleNum().toString());
            }else if("getVideoTabInfo".equals(outModel.getColumnKey())){
                //视频tab列表第一页数据
                homePageService.buildGetVideoTabInfo(pageModel,outModel.getArticleNum(),types);
                exclude.clear();
                exclude.add(JedisKey.getArticleWithPage(null,"getVideoTabInfo",(long)outModel.getArticleNum(),1));
                jedisUtils.delKeys("articlePage__columnIdnull:getVideoTabInfo*",exclude);
                jedisUtils.set(JedisKey.getCountArticleNum("getVideoTabInfo"),outModel.getArticleNum().toString());
            }else {
                    //栏目页第一页的数据（APP和WEB同一个接口）
                    if(iType==1||sColumnIds.contains(outModel.getColumnKey())){
                        logger.info("refresh检查到sColumnIds包含栏目ID为{}------",outModel.getColumnKey());
                        ArticleRequest request = new ArticleRequest();
                        request.setColumnId(outModel.getColumnKey());
                        ArticleInModel inModel = new ArticleInModel();
                        inModel.setColumnId(outModel.getColumnKey());
                        articlekgService.buildGetChannelArt(pageModel,outModel.getArticleNum(),request,inModel,types);
                        exclude.clear();
                        exclude.add(key);
                        jedisUtils.delKeys(JedisKey.getArticlePageKeyPattern(outModel.getColumnKey()),exclude);
                        //内容放入Redis后再更新对应栏目的Redis文章总量
                        jedisUtils.set(JedisKey.getCountArticleNum(outModel.getColumnKey()),outModel.getArticleNum().toString());
                    }
            }

           /* if(StringUtils.isBlank(firstPageContent)){
                //根据KEY获取到第一页的内容为空时 则主动获取对应栏目的第一页数据 并放入Redis
                refresh(outModel,types);
            }else {
                List<String> exclude = Lists.newArrayList();

                //刷新对应栏目页
                if(types==2&&sColumnIds.contains(outModel.getColumnKey())){
                    //刷新首页
                    refresh(indexOutModel,types);
                    exclude.add(key1);
                    exclude.add(key2);
                    jedisUtils.delKeys(JedisKey.getArticlePageKeyPattern("-1"),exclude);

                    //刷新视频tab页
                    refresh(videoTabInfoOutModel,types);
                    exclude.clear();
                    exclude.add(JedisKey.getArticleWithPage(null,"getVideoTabInfo",(long)outModel.getArticleNum(),1));
                    jedisUtils.delKeys("articlePage__columnIdnull:getVideoTabInfo*",exclude);

                    //说明只是内容改变总量没有变 则需要清除对应栏目的缓存 然后重新生成第一页数据
                    refresh(outModel,types);
                    exclude.clear();
                    exclude.add(key);
                    jedisUtils.delKeys(JedisKey.getArticlePageKeyPattern(outModel.getColumnKey()),exclude);
                }
            }*/
        }
        String key = "articlePage_columnId379:encyclopediaList";
        jedisUtils.del(key);
        articlekgService.encyclopediaList();
        logger.info("所有列表第一页刷新完成，耗时{}ms------",(System.currentTimeMillis()-now));
    }
}
