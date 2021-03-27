package com.kg.platform.weixin;

public class WeChatConstant {
    /**
     * Token
     */
    public static final String TOKEN = "1qaz2wsx3edc";

    /**
     * EncodingAESKey
     */
    public static final String AES_KEY = "7Uvgr1zkmiNB8ARmP6dJw3vC3zyiF4vvm7NvRnBd02R";

    /**
     * 消息类型:文本消息
     */
    public static final String MESSAGE_TYPE_TEXT = "text";

    /**
     * 消息类型:音乐
     */
    public static final String MESSAGE_TYPE_MUSIC = "music";

    /**
     * 消息类型:图文
     */
    public static final String MESSAGE_TYPE_NEWS = "news";

    /**
     * 消息类型:图片
     */
    public static final String MESSAGE_TYPE_IMAGE = "image";

    /**
     * 消息类型:视频
     */
    public static final String MESSAGE_TYPE_VIDEO = "video";

    /**
     * 消息类型:小视频
     */
    public static final String MESSAGE_TYPE_SHORTVIDEO = "shortvideo";

    /**
     * 消息类型:链接
     */
    public static final String MESSAGE_TYPE_LINK = "link";

    /**
     * 消息类型:地理位置
     */
    public static final String MESSAGE_TYPE_LOCATION = "location";

    /**
     * 消息类型:音频
     */
    public static final String MESSAGE_TYPE_VOICE = "voice";

    /**
     * 消息类型:事件推送
     */
    public static final String MESSAGE_TYPE_EVENT = "event";

    /**
     * 事件类型:subscribe(订阅)
     */
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";

    /**
     * 事件类型：unsubscribe(取消订阅)
     */
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";

    /**
     * 事件类型：CLICK(自定义菜单点击事件)
     */
    public static final String EVENT_TYPE_CLICK = "CLICK";

    /**
     * 返回消息类型:转发客服
     */
    public static final String TRANSFER_CUSTOMER_SERVICE = "transfer_customer_service";

    /**
     * ACCESS_TOKEN
     */
    public static final String ACCESS_TOKEN_ENAME = "WEIXIN_PUBLIC_ACCESS_TOKEN";

    /**
     * 返回成功字符串
     */
    public static final String RETURN_SUCCESS = "SUCCESS";

    /**
     * 主动发送消息url
     */
    public static final String SEND_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

    /**
     * 通过code获取授权access_token的URL
     */
    public static final String GET_AUTHTOKEN_URL = " https://api.weixin.qq.com/sns/oauth2/access_token?";

    /**
     * 发起用户授权的url
     */
    public static final String GET_CODE_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

    public static final String AUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";

    /**
     * 公众号AppId
     */
    public static final String APP_ID = "wx8d1517db2381997e";

    /**
     * 公众号AppSecret
     */
    public static final String APP_SECRET = "3a60886a4a6030d58bf71194991e625f";

    /**
     * 微信支付商户号
     */
    public static final String MCH_ID = "1492093072";

    /**
     * 微信支付API秘钥
     */
    public static final String KEY = "807ac4c4c287ffb80120b95fd595b1bf";

    /**
     * 微信支付api证书路径
     */
    public static final String CERT_PATH = "https://www.pupu.link/wePay/apiclient_cert.p12";

    /**
     * 微信统一下单url
     */
    public static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 微信申请退款url
     */
    public static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    /**
     * 微信支付通知url
     */
    public static final String NOTIFY_URL = "https://www.pupu.link/api/v3/weixin/notify";

    /**
     * 微信交易类型:公众号支付
     */
    public static final String TRADE_TYPE_JSAPI = "JSAPI";

    /**
     * 微信交易类型:原生扫码支付
     */
    public static final String TRADE_TYPE_NATIVE = "NATIVE";

    /**
     * 微信甲乙类型:APP支付
     */
    public static final String TRADE_TYPE_APP = "APP";

    /**
     * 获取客服基本信息GET
     */
    public static final String GET_KEFU_INFO = "https://api.weixin.qq.com/cgi-bin/customservice/getkflist?access_token=ACCESS_TOKEN";

    /**
     * 添加客服帐号POST
     */
    public static final String ADD_KEFU = "https://api.weixin.qq.com/customservice/kfaccount/add?access_token=ACCESS_TOKEN";

    /**
     * 邀请绑定客服帐号POST
     */
    public static final String INVI_KEFU = "https://api.weixin.qq.com/customservice/kfaccount/inviteworker?access_token=ACCESS_TOKEN";

    /**
     * 设置客服信息POST
     */
    public static final String SET_KEFU = "https://api.weixin.qq.com/customservice/kfaccount/update?access_token=ACCESS_TOKEN";

    /**
     * 上传客服头像POST/FROM
     */
    public static final String UPLOAD_KEFU_ICO = "https://api.weixin.qq.com/customservice/kfaccount/uploadheadimg?access_token=ACCESS_TOKEN&kf_account=KFACCOUNT";

    /**
     * 删除客服帐号GET
     */
    public static final String DELETE_KEFU = "https://api.weixin.qq.com/customservice/kfaccount/del?access_token=ACCESS_TOKEN&kf_account=KFACCOUNT";

    /**
     * 发送模板消息POST
     */
    public static final String SEND_TEMPLATE_MESSAGE = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    /**
     * 网站名
     */
    public static final String INDEX_URL = "https://www.pupu.link/#/URL";

    /**
     * 获得jsapi_ticket（参与微信支付配置的签名算法）
     */
    public static final String JSAPI_TICKET = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

    /**
     * 微信二维码
     */
    public static final String WEIXIN_QRCODE = "https://api.weixin.qq.com/";
}