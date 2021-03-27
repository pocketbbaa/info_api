package com.kg.platform.enumeration;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by Administrator on 2018/1/8.
 */
public enum NewsWebSiteEnum {

    hongguan(340, "hongguan"), guojijingji(351, "guojijingji"), guoneijingji(350, "guoneijingji"), shuzihuobi(319,
            "shuzihuobi"), gupiao(312, "gupiao"), qihuo(348, "qihuo"), waihui(346, "waihui"), shangye(349,
                    "shangye"), shangxueyuan(359, "shangxueyuan"), chuangtou(355, "chuangtou"), guanli(356,
                            "guanli"), yingxiao(358, "yingxiao"), chanjing(344, "chanjing"), nengyuan(352,
                                    "nengyuan"), fangdichan(354, "fangdichan"), shangmao(353, "shangmao"), jinrong(311,
                                            "jinrong"), licai(341, "licai"), jijin(343, "jijin"),
    zhengceyilan(361,"zhengceyilan"),hangyedongxiang(362,"hangyedongxiang"),hangqingfenxi(364,"hangqingfenxi"),
    baijiaguandian(366,"baijiaguandian"),jishudongtai(367,"jishudongtai"),xiangmuzhanshi(369,"xiangmuzhanshi"),
    qiankezhaowen(370,"qiankezhaowen"),qiankezhuanfang(371,"qiankezhuanfang"),qiankebaodao(372,"qiankebaodao"),
    qiankesudi(373,"qiankesudi"),qiankeguandian(374,"qiankeguandian"),kanke(360,"kanke"),

    pagenum(3, "pagenum"), cachetime(86400, "flushtime");

    private int status;

    private String display;

    NewsWebSiteEnum(int status, String display) {
        this.status = status;
        this.display = display;

    }

    public int getStatus() {
        return status;
    }

    public String getDisplay() {
        return display;
    }

    public static NewsWebSiteEnum getNewsWebSiteStatusEnum(int status) {
        NewsWebSiteEnum statuses[] = NewsWebSiteEnum.values();
        Optional<NewsWebSiteEnum> optional = Arrays.stream(statuses).filter(item -> item.status == status).findFirst();
        return optional.isPresent() ? optional.get() : NewsWebSiteEnum.hongguan;
    }
}
