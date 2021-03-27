package com.kg.platform.common.exception;

/**
 * @author Mark
 * @version $Id: ExceptionEnum.java, v 0.1 2016年7月13日 下午9:53:42 pengliqing Exp $
 */
public enum ExceptionEnum {


    SUCCESS("10000", "成功"),
    SERVERERROR("10001", "服务器异常，请联系管理员"),
    PARAMEMPTYERROR("10002", "参数缺失"),
    DATAERROR("10003", "错误，请检查输入"),
    TOKENRERROR("10004", "请重新登录"),
    SERVICEERROR("10005", "远程服务异常"),
    TIMEOUTEERROR("10006", "服务超时"),
    SYS_EXCEPTION("10007", "系统错误，请联系管理员"),
    SIGNERROR("10008", "验签失败"),
    PARAM_VALID_ERROR("10009", "数据格式验证失败，请重新输入"),

    ADMINIPERROR("10010", "非法操作，请联系管理员"),
    LOCKERROR("10011", "账户已被锁定"),
    ACCOUNTERROR("10012", "用户账户没有初始化"),

    SLIDE_VALID_ERROR("10020", "验证失败，请重新进行滑块验证"),
    FETCH_USER_FAIL("10021", "获取用户信息失败"),
    MOBILE_ERROR("10022","手机号格式错误，请重新输入"),

    EMAIL_EMPTY("20001", "收件人邮箱不得为空！"),
    MOBILE_USED("20002", "账号重复，请重新输入"),
    MOBILE_FORMAT("20005", "号码格式有误"),
    EMAIL_USED("20003", "邮箱已注册"),
    EMAIL_FORMAT("20004", "邮箱格式有误"),
    PASSWORD_FORMAT("20006", "密码格式有误"),
    PASSWORD_CONFIRM("20007", "两次密码不一致"),
    USER_ADDFAILURE("20008", "注册失败"),
    USER_LONGFAILURE("20009", "用户名或密码错误"),
    USER_VALIDATE("20010", "手机或邮箱和验证码不匹配"),
    USER_PROFILE("20011", "请申请专栏"),
    USER_ARTICLE("20012", "请先发表文章"),
    USER_ARTICLECOUNT("20013", "有文章"),
    ARTICLE_BONUS("20018", "设置金额大于账户余额"),
    INVITE_WITHDRAW_ERROR("20019", "邀请数量不足，无法提取奖励"),
    VERIFY_TXPASS_ERROR("20020", "交易密码验证失败"),
    TRANS_OUT_FAIL("20021","转账数量超出余额"),


    INVITE_FREZEE("20021", "用户邀新已经被冻结"),
    ACCESS_ERROR("20022", "获取Access异常"),
    THIRD_Bind_ERROR("20023", "该账号已经被其他手机号绑定了"),
    ACCOUNT_BALANCE_ERROR("20024", "账户余额不足"),
    MAX_VOTE_ERROR("20025", "你的投票数已经超过了最大投票数"),


    NONE_INVITER("20026", "无效邀请码，请输入正确的邀请码"),
    INVITER_SELF_ERROR("20027", "您不能邀请自己为师傅"),
    BINGD_ERROR("20028", "绑定失败"),
    EXIST_TEACHER("20029", "您已绑定师傅"),
    REWARD_INPUT_ERROR("20030", "请输入正确的金额!"),
    REWARD_PERSON_ERROR("20031", "您要打赏的人不存在!"),
    VIDEO_URL_ERROR("20032", "视频链接格式不正确"),

    BINGD_THIRDPAT_ERROR("20101", "账号绑定失败"),
    UN_BINGD_ERROR("20102", "解绑失败,你尚未绑定账号!"),
    RE_BINGD_ERROR("20103", "你已经绑定了账号，请勿重复绑定!"),
    BINGD_SPCOLUMN_ERROR("20104", "无法成为普通用户的徒弟"),
    TEACHER_BIND_SUB_ERROR("20105", "您无法绑定您的徒弟"),
    REWARD_MAX_ERROR("20106", "最高能打赏50Tv"),
    WX_ACCESS_ERROR("20107", "微信信息获取失败"),
    WB_ACCESS_ERROR("20108", "微博信息获取失败"),
    REBIND("20109", "第三方账号已被绑定"),
    AUTHERROR("20110", "授权失败"),
    MAX_TV_BONUS_ERROR("20111", "文章额外钛值奖励超过最大值"),
    MAX_TXB_BONUS_ERROR("20112", "文章额外氪金奖励超过最大值"),
    ADDED_EX_BONUS_ERROR("20113", "转载、抓取、后台编辑无法获得额外奖励"),
    RE_ADDED_BONUS_ERROR("20114", "已经获得过过额外奖励"),
    PLATFOEM_BALANCE_ERROR("20115", "提取奖励功能维护中，请稍后再试"),
    MOBILE_RE_BINGD_ERROR("20116", "该手机号已绑定过其他第三方账号"),
    INVITE_WITHDRAW_HANDLING("20117", "申请提现处理中"),
    USERCERTREPEAT("20118", "该用户身份证号已被其他用户提交使用"),
    ADMIN_USER_BONUS_USERIDS_ERROR("20119", "发放用户不能为空"),
    ADMIN_USER_BONUS_NO_AUTH("20120", "没有权限访问"),
    ADMIN_USER_BONUS_KG_ERROR("20121", "金额必须大于零"),
    ADMIN_USER_BONUS_TV_ERROR("20122", "钛值金额必须大于零"),
    ADMIN_USER_BONUS_REPEAT_ERROR("20123", "不能重复发放同一个人奖励"),
    MAX_USER_BONUS_MOBILES("20124", "发放的用户过多"),
    REPEAT_ERROR("20125", "请勿重复提交！"),
    ACCOUNT_KG_BANLANCE_ERROR("20126", "账户KG余额不足！"),
    INVALID_ASSINT_CODE_ERROR("20127", "助力码无效"),
    ROB_STATUS_ERROR("20128", "该抢单已完成"),
    ROBED_MINNER_ERROR("20129", "已经抢购过该矿机"),
    MINER_SELF_ASSIT("20130", "您不能为你自己助力"),
    MAX_ASSIST_ROB_TIMES_ERROR("20131", "当天的助力次数已用完"),
    TODAY_ASSIST_ERROR("20132", "您今天已经助力过该抢单了"),
    ACCOUNT_BALANCE_RIT_ERROR("20133", "账户RIT余额不足"),
    ACCOUNT_BALANCE_KG_ERROR("20134", "账户氪金余额不足"),
    NONE_MINER_ERROR("20135", "矿机已经被抢完"),
    ACTIVITY_NO_MATCH("20136", "今天没有比赛进行"),
    ACTIVITY_INVITE_COUNT_ERROR("20137", "邀新人数不足"),
    MO_START_PAGE("20138", "无启动页图片"),
    PASSWORD_LENGTH_ERROR("20139", "密码位数必须在6到20位之间"),

    SENSITIVE_TITLE_ERROR("29001", "标题中包含敏感词"),
    SENSITIVE_CONTENT_ERROR("29002", "正文中包含敏感词"),
    SENSITIVE_TAG_ERROR("29003", "标签中包含敏感词"),
    SENSITIVE_SUMMARY_ERROR("29004", "摘要中包含敏感词"),
    SENSITIVE_LINK_ERROR("29005", "原文链接中包含敏感词"),
    SENSITIVE_SOURCE_ERROR("29006", "文章来源中包含敏感词"),
    SENSITIVE_COMMENT_ERROR("29007", "评论中包含敏感词"),


    ACTIVITY_END("29008", "活动已经结束"),
    ACTIVITY_START("29009", "活动还未开始"),
    PRIZEDRAWED("29010", "您已抽取了奖励"),
    BONUS_FREEZE_ERROR("29011", "您的邀新奖励已被冻结"),
    ACTIVITY_INVITE_ERROR("29012", "邀新人数不足10人"),

    PHONE_EXIST("30001", "该手机号已存在"),
    PHONE_CODE_FAILURE("30002", "手机验证码已失效"),

    NOT_CERTIFICATE("40001", "未实名认证"),
    NOT_SET_TRANPASSWORD("40002", "未设置交易密码"),
    CANCEL_FAIL_PASSED("40003", "撤销失败，工单已审核通过"),
    CANCEL_FAIL_NO_PASSED("40004", "撤销失败，工单已审核不通过"),
    NO_DATA("40005", "数据不存在"),


    CONVERT_OVER("60001", "今天兑换额度被抢完了，明天早点来抢吧！"),
    CONVERT_NOT_START("60002", "当前不能兑换"),
    CONVERT_COUNT_OVER("60003", "今日兑换次数已用完"),
    CONVERT_KG_NOT_ENOUGH("60004", "你还没有足够的KG兑换哦,快去赚KG吧"),
    CONVERT_ALL_NOT_ENOUGH("60005", "不能全部兑换"),
    CONVERT_SYSTEM_ERROR("60006", "当前兑换人数过多，请稍后再试"),
    USERIDCORDEXIST("60007", "该身份证号已被其他用户使用"),
    AUTOCODEERR("60008","验证码有误,请重新输入"),
    AMOUNTMINUS("60009","金额不能为负数或者0"),
    GOING_WORK_ORDER("60010","存在正在进行中的工单"),


    // code handler copy start
    FAILURE("-99999", "错误代码**【-99999】");

    private String code;

    private String message;

    ExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
