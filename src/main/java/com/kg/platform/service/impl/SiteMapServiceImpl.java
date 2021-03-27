package com.kg.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.kg.platform.common.utils.*;
import com.kg.platform.dao.read.ArticleRMapper;
import com.kg.platform.dao.read.ColumnRMapper;
import com.kg.platform.dao.read.TagsRMapper;
import com.kg.platform.dao.read.UserRMapper;
import com.kg.platform.model.in.ArticleInModel;
import com.kg.platform.model.in.TagsInModel;
import com.kg.platform.model.in.UserInModel;
import com.kg.platform.model.out.ArticleOutModel;
import com.kg.platform.model.out.ColumnOutModel;
import com.kg.platform.model.out.TagsOutModel;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.service.SiteMapService;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Administrator on 2018/8/22.
 */
@Service
public class SiteMapServiceImpl implements SiteMapService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final Integer URLLIMIT = 30000;

    private static final Integer REQUEST_SIZE = 5000;

    private String INDEX_URL;

    private String ARTICLE_URL;

    private String VIDEO_URL;

    private String AUTHOR_URL;

    private String TAGS_URL;

    private String NOTFOUND_URL;

    @Autowired
    private JedisUtils jedisUtils;

    @Autowired
    private ArticleRMapper articleRMapper;

    @Autowired
    private UserRMapper userRMapper;

    @Autowired
    private TagsRMapper tagsRMapper;

    @Autowired
    private ColumnRMapper columnRMapper;

    @Resource(name = "propertyLoader")
    protected PropertyLoader propertyLoader;

    private List<ColumnOutModel> columnOutModels;


    private void initialUrl(String INDEX_URL){
        this.INDEX_URL = INDEX_URL;
        this.ARTICLE_URL = INDEX_URL+"article/";
        this.VIDEO_URL = INDEX_URL+"video/";
        this.AUTHOR_URL = INDEX_URL+"author/";
        this.TAGS_URL = INDEX_URL+"tag/";
        this.NOTFOUND_URL = INDEX_URL+"404";
    }


    @Override
    public void generatorSiteMapXmlFile() throws UnsupportedEncodingException {

            initialUrl(propertyLoader.getProperty("platform", "index.url"));
            String sLastXmlNum = jedisUtils.get(JedisKey.getSitemapXmlCode("count"));
            if(StringUtils.isNotBlank(sLastXmlNum)){
                logger.info("删除所有缓存XML，重新生成XML------");
                jedisUtils.delKeys(JedisKey.getSitemapXmlCode("*"),Lists.newArrayList());
            }
            columnOutModels = getColumnNameList();
            long count;
            //生成文章相关XML
            ArticleInModel articleInModel = new ArticleInModel();
            articleInModel.setPublishKind(1);
            count = articleRMapper.countArticleForSitemap(articleInModel);
            generatorSiteMapXmlFileUtil(count,1);

            //生成视频相关XML
            articleInModel.setPublishKind(2);
            count = articleRMapper.countArticleForSitemap(articleInModel);
            generatorSiteMapXmlFileUtil(count,2);

            //生成作者ID相关XML
            count = userRMapper.countAuthorListForSitemap();
            generatorSiteMapXmlFileUtil(count,3);

            //生成标签名相关XML
            count = tagsRMapper.countTagsForSitemap();
            generatorSiteMapXmlFileUtil(count,4);

    }

    @Override
    public void getSitemapXml(HttpServletResponse response, Integer number) {
        logger.info("收到获取网站地图XML请求------");
        long now = System.currentTimeMillis();
        try {
            OutputStream out = response.getOutputStream();
            byte[] bytes = jedisUtils.get(JedisKey.getSitemapXmlCode(number!=null?number.toString():""),byte[].class);

            if(bytes==null||bytes.length==0){
               response.sendRedirect(NOTFOUND_URL);
            }else {
                Document document = DocumentHelper.parseText(new String(bytes,"UTF-8"));
                //设置换行
                OutputFormat format = OutputFormat.createPrettyPrint();
                format.setEncoding("UTF-8");
                //5、生成XML文件
                XMLWriter writer;
                try {
                    writer = new XMLWriter(out, format);
                    //设置是否转义，默认是true进行转义
                    writer.setEscapeText(false);
                    writer.write(document);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                response.setContentType("text/xml;charset=UTF-8");
                out.flush();
                out.close();
                logger.info("生成网站地图完毕------耗时{}ms",(System.currentTimeMillis()-now));
            }
        } catch (IOException | DocumentException e) {
            try {
                response.sendRedirect(NOTFOUND_URL);
            } catch (IOException e1) {
                logger.error("返回网站地图XML失败：{}"+e1.getMessage());
            }
            logger.error("返回网站地图XML失败：{}"+e.getMessage());
        }
    }


    private void generatorSiteMapXmlFileUtil(long count,int type) throws UnsupportedEncodingException {

        //获取XML文件最新个数
        String sLastXmlNum = jedisUtils.get(JedisKey.getSitemapXmlCode("count"));
        int lastXmlNum=0;
        if(StringUtils.isNotBlank(sLastXmlNum)){
            lastXmlNum=Integer.parseInt(sLastXmlNum);
        }

        int publishKind=0;

        if(type==1){
            //文章
            publishKind=1;

        }else if(type==2){
            //视频
            publishKind=2;

        }else if(type==3){
            //作者

        }else if(type==4){
            //标签名

        }
        int result1 = 0;
        int result2 = 0;
        int pageCount = 0;
        int requestCount = 0;
        int pageSize = REQUEST_SIZE;

        //计算总的文件个数
        result1 = (int) count / URLLIMIT;
        result2 = count % URLLIMIT > 0 ? 1 : 0;
        pageCount = result1 + result2;//总的文件数
        for (int i = 1+lastXmlNum; i <= pageCount+lastXmlNum; i++) {
            List<Object> outModelList = Lists.newArrayList();
            if ((i-lastXmlNum) != pageCount) {
                //不是最后一个文件 说明都有2W条数据
                requestCount = URLLIMIT / REQUEST_SIZE;
                for (int j = 1; j <= requestCount; j++) {

                    if(type==1||type==2){
                        //文章或者视频
                        List<ArticleOutModel> outModels = getArticleList((i - 1-lastXmlNum) * requestCount + j, pageSize,publishKind);
                        outModelList.addAll(outModels);
                    }else if(type==3){
                        //作者

                    }else if(type==4){
                        //标签名

                    }

                }
            } else {
                //最后一个文件  没有2W条数据
                long lastCount = count - (URLLIMIT * (i - 1-lastXmlNum));
                result1 = (int) lastCount / pageSize;
                result2 = lastCount % pageSize > 0 ? 1 : 0;
                requestCount = result1 + result2;
                for (int j = 1; j <= requestCount; j++) {

                    if(type==1||type==2){
                        //文章或者视频
                        List<ArticleOutModel> outModels = getArticleList((i - 1-lastXmlNum) * (URLLIMIT / REQUEST_SIZE) + j, pageSize,publishKind);
                        outModelList.addAll(outModels);
                    }else if(type==3){
                        //作者
                        List<UserkgOutModel> outModels = getAuthorList((i - 1-lastXmlNum) * (URLLIMIT / REQUEST_SIZE) + j, pageSize);
                        outModelList.addAll(outModels);

                    }else if(type==4){
                        //标签名
                        List<TagsOutModel> outModels = getTagsList((i - 1-lastXmlNum) * (URLLIMIT / REQUEST_SIZE) + j, pageSize);
                        outModelList.addAll(outModels);
                    }

                }
            }
            //将每一页的数据生成XML 并放入缓存中
            byte[] bytes = createXML(outModelList, type);
            jedisUtils.set(JedisKey.getSitemapXmlCode(String.valueOf(i)), JSON.toJSONString(bytes));
        }
        jedisUtils.set(JedisKey.getSitemapXmlCode("count"), String.valueOf(pageCount+lastXmlNum));
    }

    private List<ArticleOutModel> getArticleList(int currentPage, int pageSize, int publishKind) {
        ArticleInModel inModel = new ArticleInModel();
        inModel.setStart((currentPage - 1) * pageSize);
        inModel.setLimit(pageSize);
        inModel.setPublishKind(publishKind);
        return articleRMapper.getArticleForSitemap(inModel);
    }

    private List<UserkgOutModel> getAuthorList(int currentPage, int pageSize) {
        UserInModel inModel = new UserInModel();
        inModel.setStart((currentPage - 1) * pageSize);
        inModel.setLimit(pageSize);
        return userRMapper.getAuthorListForSitemap(inModel);
    }

    private List<TagsOutModel> getTagsList(int currentPage, int pageSize) {
        TagsInModel inModel = new TagsInModel();
        inModel.setStart((currentPage - 1) * pageSize);
        inModel.setLimit(pageSize);
        return tagsRMapper.listTagsForSitemap(inModel);
    }

    private List<ColumnOutModel> getColumnNameList() {
        List<ColumnOutModel> firstColumnName = columnRMapper.appSelectFistColumnForSitemap();
        List<ColumnOutModel> secondColumnName = columnRMapper.appSelectSecondColumnForSitemap();
        List<ColumnOutModel> allColumnName = Lists.newArrayList();
        allColumnName.addAll(firstColumnName);
        allColumnName.addAll(secondColumnName);
        return allColumnName;
    }

    private byte[] createXML(List list, int type) throws UnsupportedEncodingException {
        logger.info("生成XML开始------");
        long now = System.currentTimeMillis();
        //1、创建Document对象，代表整个XML
        Document document = DocumentHelper.createDocument();
        //2、创建根节点
        Element urlset = document.addElement("urlset");
        //每一页放入栏目列表URL
        addColumnNameElement(urlset);
        //每一页放入首页URL
        addElement(urlset,INDEX_URL,null,"always","1.0");
        //4、生成子节点及内容
        if (type == 1||type==2) {
            //文章数据或者视频数据
            List<ArticleOutModel> outModels = list;
            for (ArticleOutModel articleOutModel : outModels) {
                String loc = "";
                if(type==1){
                    loc=ARTICLE_URL + articleOutModel.getArticleId();
                }else {
                    loc=VIDEO_URL + articleOutModel.getArticleId();
                }
                addElement(urlset,loc,DateUtils.formatDate(articleOutModel.getAuditDate(), DateUtils.DATE_FORMAT_YYYY_MM_DD),"always","1.0");
            }
        }
        if (type == 3) {
            //作者ID数据
            List<UserkgOutModel> outModels = list;
            for (UserkgOutModel userkgOutModel : outModels) {
                addElement(urlset,AUTHOR_URL + userkgOutModel.getUserId(),null,"always","1.0");
            }
        }
        if (type == 4) {
            //标签名数据
            List<TagsOutModel> outModels = list;
            for (TagsOutModel tagsOutModel : outModels) {
                addElement(urlset,TAGS_URL + URLEncoder.encode(tagsOutModel.getTagName(),"UTF-8"),null,"always","1.0");
            }
        }



        String s =  document.asXML();;
        logger.info("生成XML结束，耗时" + (System.currentTimeMillis() - now) + "ms------");
        return s.getBytes("UTF-8");
    }

    private void addElement(Element urlset,String sloc,String slastmod,String schangefreq,String spriority){
        Element url = urlset.addElement("url");
        Element loc = url.addElement("loc");
        if(StringUtils.isNotBlank(slastmod)){
            Element lastmod = url.addElement("lastmod");
            lastmod.addText(slastmod);
        }
        Element changefreq = url.addElement("changefreq");
        Element priority = url.addElement("priority");
        loc.addText(sloc);
        changefreq.addText(schangefreq);
        priority.addText(spriority);
    }

    private void addColumnNameElement(Element urlset){
        for (ColumnOutModel columnOutModel:columnOutModels) {
            Element url = urlset.addElement("url");
            Element loc = url.addElement("loc");
            Element changefreq = url.addElement("changefreq");
            Element priority = url.addElement("priority");
            loc.addText(INDEX_URL+columnOutModel.getColumnUrlName());
            changefreq.addText("always");
            priority.addText("0.8");
        }
    }


}
