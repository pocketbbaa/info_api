package com.kg.platform.common.mq.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.google.common.collect.Lists;
import com.kg.platform.common.mq.MQConfig;
import com.kg.platform.common.mq.MQProduct;
import com.kg.platform.common.utils.JedisKey;
import com.kg.platform.common.utils.JedisUtils;
import com.kg.platform.dao.read.ArticleRMapper;
import com.kg.platform.dao.write.KgColumnCountWMapper;
import com.kg.platform.model.in.ArticleInModel;
import com.kg.platform.model.in.KgColumnCountInModel;
import com.kg.platform.model.out.CountArticleNumOutModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountArticleNumConsumer implements MessageListener {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private ArticleRMapper articleRMapper;
    @Autowired
    private KgColumnCountWMapper kgColumnCountWMapper;
    @Autowired
    private MQConfig refreshColumnArticleMqConfig;
    @Autowired
    private MQProduct mqProduct;

    @Override
    public Action consume(Message message, ConsumeContext consumeContext) {

        long now = System.currentTimeMillis();
        log.info("kg-CountArticleNumConsumer 启动 ......");
        log.info("mqUrl:" + message.getBornHost());
        log.info("topic:" + message.getTopic());
        log.info("tags:" + message.getTag());
        try {
            log.info("new message comming ....");
            log.info("msgId:" + message.getMsgID());
            String body = new String(message.getBody());
            log.info("message body : " + body);
            if (StringUtils.isEmpty(body)) {
                log.info("消息体为空 !!!");
                return Action.CommitMessage;
            }
            Map maps = (Map) JSONObject.parse(body);
            Integer types = Integer.parseInt(maps.get("types").toString());
            log.info("types : " + types);
            if(types==1||types==2){
                String key;
                List<KgColumnCountInModel> list = Lists.newArrayList();
                //获取首页推荐下的文章总量
                ArticleInModel inModel = new ArticleInModel();
                Long indexNum = articleRMapper.selectCountArticleRecomm(inModel);
                //将获取的首页推荐下的文章总量放入Redis
                key = "-1";
                addList(list,key,indexNum);
            /*    log.info("将key为：{}的文章总量统计数据放入Redis------",key);
                jedisUtils.set(key,indexNum.toString());*/

                //获取视频TAB的文章总量
                key = "getVideoTabInfo";
                indexNum = articleRMapper.countVideoArt();
                addList(list,key,indexNum);
                /*log.info("将key为：{}的文章总量统计数据放入Redis------",key);
                jedisUtils.set(key,indexNum.toString());*/

                //获取每个一级栏目的文章总量
                List<CountArticleNumOutModel> firstColumnForMQ =  articleRMapper.countFirstColumnForMQ();
                //获取每个一级栏目的文章总量
                List<CountArticleNumOutModel> secondColumnForMQ =  articleRMapper.countSecondColumnForMQ();


                if(firstColumnForMQ!=null&&firstColumnForMQ.size()>0){
                    //将统计的数据放入redis
                    for (CountArticleNumOutModel first:firstColumnForMQ) {
                        //没有二级栏目数据则直接将一级栏目数据放入Redis
                        if(first.getColumnId()!=null){
                            key = first.getColumnId().toString();
                            Long count = first.getArticleNum();
                            //将二级栏目中和一级栏目相同的ID相加
                            for (CountArticleNumOutModel second:secondColumnForMQ) {
                                if(first.getColumnId().equals(second.getColumnId())){
                                    count = count+second.getArticleNum();
                                    break;
                                }
                            }

                            addList(list,key,count);
                          /*  log.info("将key为：{}的一级栏目文章总量统计数据放入Redis------",key);
                            jedisUtils.set(key,count.toString());*/
                        }
                    }
                }

                //统计二级栏目的数量
                if(secondColumnForMQ!=null&&secondColumnForMQ.size()>0){

                    for (CountArticleNumOutModel second:secondColumnForMQ) {
                        boolean check = true;
                        if(second.getColumnId()!=null){
                            key = second.getColumnId().toString();
                            Long count = second.getArticleNum();
                            //将二级栏目和一级栏目的ID相同的排除掉
                            assert firstColumnForMQ != null;
                            for (CountArticleNumOutModel first:firstColumnForMQ) {
                                if(second.getColumnId().equals(first.getColumnId())){
                                   check = false;
                                   break;
                                }
                            }
                            if(check){
                                addList(list,key,count);
                              /*  log.info("将key为：{}的二级栏目文章总量统计数据放入Redis------",key);
                                jedisUtils.set(key,count.toString());*/
                            }
                        }
                    }
                }

                //删除栏目统计表的数据，然后重新写入新计算的数据
                int result = kgColumnCountWMapper.deleteAll();
                if(result>0){
                    int insertNum = kgColumnCountWMapper.insertList(list);
                    if(indexNum>0){
                        log.info("插入{}条栏目总量统计记录------",insertNum);
                        //插入记录后再发送一个MQ 此MQ消费者将根据统计总量将栏目列表第一页主动获取出来放入缓存
                        log.info("发送刷新栏目文章第一页数据的MQ------");
                        mqProduct.sendMessage(refreshColumnArticleMqConfig.getTopic(),refreshColumnArticleMqConfig.getTags(),null, JSON.toJSONString(maps));
                    }else {
                        return Action.ReconsumeLater;
                    }
                }else {
                    return Action.ReconsumeLater;
                }
            }
            log.info("统计栏目文章完成，并写入数据库，耗时{}ms------",(System.currentTimeMillis()-now));
            return Action.CommitMessage;
        } catch (Exception e) {
            log.error(e.getMessage());
            //消费失败
            return Action.ReconsumeLater;
        }
    }

    private void addList(List<KgColumnCountInModel> list, String key, Long num){
        KgColumnCountInModel inModel = new KgColumnCountInModel();
        inModel.setColumnKey(key);
        inModel.setArticleNum(num.intValue());
        list.add(inModel);
    }
}