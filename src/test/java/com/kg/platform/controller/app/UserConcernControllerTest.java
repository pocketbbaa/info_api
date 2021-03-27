package com.kg.platform.controller.app;

import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.utils.JsonUtil;
import com.kg.platform.controller.SiteimageController;
import com.kg.platform.model.request.SiteimageRequest;
import com.kg.platform.model.request.UserConcernRequest;
import com.kg.platform.model.response.HotAuthorsResponse;
import com.kg.platform.model.response.UserConcernListResponse;
import com.kg.platform.model.response.UserkgResponse;
import com.kg.platform.service.PushMessageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

/**
 * Created by Administrator on 2018/3/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:/spring-context*.xml"})
public class UserConcernControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(UserConcernControllerTest.class);

    @Inject
    private UserConcernController userConcernController;

    @Inject
    private PushMessageService pushMessageService;

    @Inject
    private SiteimageController siteimageController;

    @Test
    public void concernUser() {
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("428645491874603008");
        UserConcernRequest request = new UserConcernRequest();
        request.setConcernUserId("454213176792363008");
        System.out.println(userConcernController.concernUser(kguser, request));
    }

    @Test
    public void concernList() {
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("428645491874603008");
        PageModel<UserConcernListResponse> page = new PageModel<UserConcernListResponse>();
        page.setCurrentPage(1);
        System.out.println(userConcernController.concernList(kguser, page));
    }

    @Test
    public void fansList() {
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("420245869804265472");
        PageModel<UserConcernListResponse> page = new PageModel<UserConcernListResponse>();
        page.setCurrentPage(1);
        System.out.println(userConcernController.fansList(kguser, page));
    }

    @Test
    public void concernReminder() {
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("420245869804265472");
        System.out.println(userConcernController.concernReminder(kguser));
    }

    @Test
    public void testRecommendHotAuthors() {
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("420245869804265472");
        PageModel<HotAuthorsResponse> page = new PageModel<HotAuthorsResponse>();
        page.setCurrentPage(2);
        System.out.println(JsonUtil.writeValueAsString(userConcernController.recommendHotAuthors(kguser, page)));
    }

    @Test
    public void testHotAuthors() {
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("428645491874603008");
        PageModel<UserConcernListResponse> page = new PageModel<UserConcernListResponse>();
        page.setCurrentPage(1);
        System.out.println(JsonUtil.writeValueAsString(userConcernController.hotAuthorList(new UserConcernRequest(), page)));
    }

    @Test
    public void searchAuthorList() {
        UserkgResponse kguser = new UserkgResponse();
        kguser.setUserId("428645491874603008");
        PageModel<UserConcernListResponse> page = new PageModel<UserConcernListResponse>();
        page.setCurrentPage(1);
        UserConcernRequest userConcernRequest = new UserConcernRequest();
        userConcernRequest.setSearchStr("BTC");
        userConcernRequest.setCurrentPage(1);
        userConcernRequest.setPageSize(20);
        System.err.println(JsonUtil.writeValueAsString(userConcernController.searchAuthorList(userConcernRequest, page)));
    }

    @Test
    public void testHolistAdSiteimageAuthors() {
        SiteimageRequest request = new SiteimageRequest();
    }

}