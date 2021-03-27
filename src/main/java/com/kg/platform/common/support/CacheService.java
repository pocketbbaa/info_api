package com.kg.platform.common.support;

import com.kg.platform.dao.read.UserRMapper;
import com.kg.platform.model.response.admin.ChannelResponse;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;

@Service
public class CacheService {

    @Inject
    private UserRMapper userRMapper;

    private static final HashMap<Integer,String> cacheChannelMap = new HashMap<>();

    public String getCacheChannelMapKey(Integer key) {
        if(cacheChannelMap.size()==0){
            initChannelCache();
        }
        return cacheChannelMap.get(key);
    }

    public CacheService() {
    }

    /**
     * 缓存数据库中的渠道来源描述
     */
    public void initChannelCache(){
        List<ChannelResponse> cacheChannel =  userRMapper.getChannel();
        if(cacheChannel == null || cacheChannel.size()==0){
            return;
        }
        for (ChannelResponse channelResponse:cacheChannel){
            cacheChannelMap.put(channelResponse.getId(),channelResponse.getName());
        }
    }
}
