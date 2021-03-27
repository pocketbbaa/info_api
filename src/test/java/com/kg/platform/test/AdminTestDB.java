package com.kg.platform.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.utils.IPUtil;
import com.kg.platform.model.IPDataModel;

/**
 * 后台接口单元测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:/spring-context*.xml" })
public class AdminTestDB {
    /*
     * @Autowired private UserService userService;
     * 
     * @Autowired private LoginService loginService;
     * 
     * @Autowired private ArticleService articleService;
     * 
     * @Autowired private ColumnService columnService;
     * 
     * @Autowired private AboutService aboutService;
     * 
     * @Autowired private AdminSiteimageService siteimageinService;
     * 
     * @Autowired private FeedbackService feedbackService;
     * 
     * @Autowired private CommentService commentService;
     * 
     * @Autowired private SysUserService sysUserService;
     * 
     * @Autowired private PostService postService;
     * 
     * @Autowired private KeywordService keywordService;
     * 
     * @Autowired private RoleService roleService;
     * 
     * @Autowired private DataStatService dataStatService;
     * 
     * @Autowired private AccountService accountService;
     * 
     * @Autowired private WeixinService weixinService;
     * 
     * @Autowired private LynnService lynnService;
     */

    @Autowired
    private IPUtil ipUtil;

    @Test
    public void test() {
        try {
            // System.out.println(weixinService.getJsapiTicket());
            // UserQueryRequest request = new UserQueryRequest();
            //// request.setCreateDateEnd(new Date());
            //// request.setAuditStatus(1);
            //// request.setLockStatus(1);
            //// request.setUserMobile("135");
            //// request.setUserRole(1);
            // request.setUserId(383631064624664576L);
            // System.out.println(userService.getUserList(request,new
            // PageModel<>()));
            // System.out.println(userService.setHotUser(1L,true));
            // System.out.println(userService.auditUser(1L,-999L,1,"xxx").getErrorMsg());
            // System.out.println(userService.lockUser(1L,-999L,1,1,1).getErrorMsg());
            // System.out.println(userService.getUserInfo(1L));
            // System.out.println(loginService.login("super_admin","1"));
            // ArticleQueryRequest request = new ArticleQueryRequest();
            // request.setArticleId("1");
            // request.setArticleTag("1");
            // request.setArticleTitle("1");
            // request.setColumnId(1L);
            // request.setSecondColumn(1L);
            // request.setDisplayStatus(1);
            // request.setPublishStatus(1);
            // System.out.println(articleService.getArticleList(request,new
            // PageModel<>()));
            // System.out.println(articleService.setDisplayOrder(1L,1));
            // System.out.println(articleService.auditArticle(5L,-999L,1,2,3,"xx"));
            // 发布文章
            // ArticleEditRequest request = new ArticleEditRequest();
            // request.setArticleTitle("测试标题");
            // request.setArticleText("测试的富文本内容...............");
            // request.setArticleTag("标签1,标签2");
            // request.setDescription("文章摘要");
            // request.setImage("http://www.baidu.com");
            // request.setType(1);
            // request.setColumnId(1);
            // request.setSecondColumn(2);
            // request.setDisplayStatus(1);
            // request.setDisplayOrder(0);
            // request.setCommentSet(true);
            // request.setPublishSet(true);
            // request.setPublishTime(new Date());
            // request.setCreateUser(1L);
            // request.setSysUser(-999);
            // request.setTagnames("标签1,标签2");
            // System.out.println(articleService.publishArticle(request));
            // System.out.println(columnService.getColumnList());
            // System.out.println(aboutService.getBaseinfoList());
            // System.out.println(aboutService.setInfoStatus(1,-999));
            // System.out.println(articleService.getArticleById(1L));
            // System.out.println(siteimageinService.listImage(new
            // AdminSiteimageQueryRequest()));
            // FeedbackQueryRequest request = new FeedbackQueryRequest();
            // request.setContent("123");
            // System.out.println(feedbackService.getFeedbackList(request));
            // FeedbackQueryRequest request = new FeedbackQueryRequest();
            // request.setFeedbackId(1L);
            // System.out.println(feedbackService.deleteFeedback(request));
            // CommentQueryRequest request = new CommentQueryRequest();
            // request.setCommentIds("1");
            // request.setDisplayStatus(true);
            // System.out.println(commentService.setDisplayStatus(request));
            // CommentQueryRequest request = new CommentQueryRequest();
            // request.setCommentSet(false);
            // request.setCommentId(1L);
            // request.setStatus(2);
            // request.setRefuseReason("哈哈");
            // System.out.println(commentService.auditComment(request));
            // System.out.println(commentService.deleteComment(request));
            // System.out.println(commentService.commentSet(request));
            // AdminLoginSetRequest request = new AdminLoginSetRequest();
            // request.setTime(10);
            // request.setUnit(1);
            // System.out.println(userService.loginSet(request));
            // System.out.println(sysUserService.getPostList());
            // System.out.println(sysUserService.getSysUserList(new
            // SysUserQueryRequest()));
            // SysUserQueryRequest request = new SysUserQueryRequest();
            // request.setUserId(-999L);
            // request.setKgUserId(1L);
            // request.setStatus(true);
            // System.out.println(sysUserService.resetPassword(request));
            // System.out.println(sysUserService.setStatus(request));
            // System.out.println(sysUserService.setKgUser(request));
            // System.out.println(sysUserService.getSysUserById(request));
            // System.out.println(postService.getAuthTree());
            // AdminPostEditRequest request = new AdminPostEditRequest();
            // request.setName("产品");
            // request.setUserId(-999);
            // request.setAuthIds("1,2,3,4,5,6,7");
            // System.out.println(postService.addPost(request));
            // System.out.println(postService.getPostList());
            // AdminPostEditRequest request = new AdminPostEditRequest();
            // request.setPostId(1);
            // request.setStatus(true);
            // System.out.println(postService.setStatus(request));
            // System.out.println(keywordService.getKeywordList(new
            // KeywordQueryRequest()));
            // KeywordEditRequest request = new KeywordEditRequest();
            // request.setIds("1");
            // request.setChannel(1);
            // System.out.println(keywordService.setStatus(request));
            // System.out.println(keywordService.setOrder(request));
            // System.out.println(keywordService.deleteKeyword(request));
            // System.out.println(keywordService.setChannel(request));
            // KeywordEditRequest request = new KeywordEditRequest();
            // request.setName("测试");
            // request.setLink("http://www.baidu.com");
            // request.setChannel(0);
            // request.setStatus(true);
            // request.setOrder(0);
            // request.setCreateUser(-999);
            // System.out.println(keywordService.addKeyword(request));
            // System.out.println(roleService.getRoleList());
            // RoleQueryRequest request = new RoleQueryRequest();
            // request.setId(1);
            // System.out.println(roleService.getRoleProfile(request));
            // System.out.println(columnService.getColumnList());
            // System.out.println(dataStatService.getNormalUserLi、st(new
            // DataStatQueryRequest()));
            // System.out.println(dataStatService.getColumnUserList(new
            // DataStatQueryRequest()));
            // System.out.println(columnService.getColumnList());
            // UserQueryRequest request = new UserQueryRequest();
            // request.setUserId(1L);
            // System.out.println(loginService.getSysMenu(request));
            // System.out.println(accountService.getAccountRecharge(new
            // AccountRechargeQueryRequest()));
            // System.out.println(accountService.getAccount(new
            // AccountQueryRequest()));
            // System.out.println(accountService.getAccountWithdraw(new
            // AccountWithdrawQueryRequest()));
            // AccountWithdrawEditRequest request = new
            // AccountWithdrawEditRequest();
            // request.setFlowId(1L);
            // request.setStatus(AccountWithdrawStatusEnum.WITHDRAWING.getStatus());
            // System.out.println(accountService.auditAccountWithdraw(request));
            // System.out.println(accountService.getAccountDiposit(new
            // AccountDipositQueryRequest()));
            // System.out.println(userService.getUserCert(new
            // UserCertQueryRequest()));
            // UserCertEditRequest request = new UserCertEditRequest();
            // request.setUserIds("1");
            // request.setAuditUser(1L);
            // request.setStatus(1);
            // System.out.println(userService.auditUserCert(request));
            // System.out.println(accountService.getBusinessType());
            // ArticleEditRequest request = new ArticleEditRequest();
            // request.setArticleId(390168851050012672L);
            // System.out.println(articleService.getBonus(request));
            // List<Long> userIdList = new ArrayList<>();
            // userIdList.add(385833349006106624L);
            // userService.auditUser(userIdList,-999L,1,null);
            // AdminSiteimageQueryRequest request = new
            // AdminSiteimageQueryRequest();
            //// request.setNavigatorPos(1);
            // System.out.println(siteimageinService.listImage(request));
            // TxPasswordEditRequest request = new TxPasswordEditRequest();
            // request.setPassword("1");
            // request.setUserId(1L);
            // request.setCode("234356");
            // System.out.println(lynnService.setTxPassword(request));

            for (int i = 0; i < 100; i++) {
                IPDataModel ipData = ipUtil.getIpData("219.236.114.114");

                System.out.println("json数据： " + JSON.toJSONString(ipData));

                String country = ipData.getCountry();
                String region = ipData.getRegion();
                String city = ipData.getCity();
                String county = ipData.getCounty();
                String isp = ipData.getIsp();
                String area = ipData.getArea();
                System.out.println("国家： " + country);
                System.out.println("地区： " + area);
                System.out.println("省份: " + region);
                System.out.println("城市： " + city);
                System.out.println("区/县： " + county);
                System.out.println("互联网服务提供商： " + isp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 销量：
        //
    }
}