package com.kg.platform.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.kg.platform.dao.read.*;
import com.kg.platform.model.in.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.kg.platform.common.aop.Cacheable;
import com.kg.platform.common.context.Result;
import com.kg.platform.common.utils.BaseUtil;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.common.utils.HttpUtil;
import com.kg.platform.common.utils.IPUtil;
import com.kg.platform.common.utils.UserTagsUtil;
import com.kg.platform.dao.write.UserActiveWMapper;
import com.kg.platform.dao.write.UserProfileWMapper;
import com.kg.platform.dao.write.UserWMapper;
import com.kg.platform.enumeration.AuditStatusEnum;
import com.kg.platform.enumeration.DateEnum;
import com.kg.platform.enumeration.UserRoleEnum;
import com.kg.platform.model.IPDataModel;
import com.kg.platform.model.UserTagBuild;
import com.kg.platform.model.out.ArticleOutModel;
import com.kg.platform.model.out.UserActiveOutModel;
import com.kg.platform.model.out.UserProfileOutModel;
import com.kg.platform.model.out.UserkgOutModel;
import com.kg.platform.model.request.UserProfileRequest;
import com.kg.platform.model.response.ArticleResponse;
import com.kg.platform.model.response.UserProfileResponse;
import com.kg.platform.service.UserProfileService;

@Service
@Transactional
public class UserProfileServiceImpl implements UserProfileService {
    private static final Logger logger = LoggerFactory.getLogger(UserProfileServiceImpl.class);

    @Inject
    UserProfileRMapper userProfileRMapper;

    @Inject
    UserProfileWMapper userProfileWMapper;

    @Inject
    private UserWMapper userWMapper;

    @Inject
    UserActiveRMapper userActiveRMapper;

    @Inject
    UserActiveWMapper userActiveWMapper;

    @Inject
    KgArticleStatisticsRMapper kgArticleStatisticsRMapper;

    @Inject
    ArticleRMapper articleRMapper;

    @Inject
    ColumnRMapper columnRMapper;

    @Inject
    private UserRMapper userRMapper;

    @Autowired
    private UserTagsUtil userTagsUtil;

    @Autowired
    private IPUtil ipUtil;

    @Autowired
    private UserConcernRMapper userConcernRMapper;

    /**
     * 修改资料
     *
     * @param request
     * @return
     */
    public boolean updateProfile(UserProfileRequest request) {
        logger.info("修改资料入参：UserProfileRequest={}", JSON.toJSONString(request));

        boolean success = false;
        UserProfileInModel inModel = new UserProfileInModel();
        inModel.setUserId(request.getUserId());
        inModel.setAvatar(request.getAvatar());
        inModel.setSex(request.getSex());
        inModel.setCountry(request.getCountry());
        inModel.setProvince(request.getProvince());
        inModel.setCity(request.getCity());
        inModel.setCounty(request.getCounty());
        inModel.setResume(request.getResume());

        UserProfileOutModel outModel = userProfileRMapper.getMedia(inModel);
        if (outModel == null) {
            return false;
        }
        UserkgOutModel userkgOutModel = userRMapper.selectByPrimaryKey(request.getUserId());
        // 如果是专栏用户
        if (1 != userkgOutModel.getUserRole()) {
            inModel.setColumnAvatar(
                    null == request.getColumnAvatar() ? request.getAvatar() : request.getColumnAvatar());
            inModel.setColumnName(request.getUsername());
            inModel.setColumnIntro(request.getResume());
            inModel.setColumnCity(request.getCity());
            inModel.setColumnProvince(request.getProvince());
            inModel.setColumnCountry(request.getCountry());
            inModel.setColumnCounty(request.getCounty());
        }

        if (request.getUsername() != null) {
            UserInModel userInModel = new UserInModel();
            userInModel.setUserId(request.getUserId());
            userInModel.setUserName(request.getUsername());
            success = userWMapper.updateByUserSelective(userInModel) > 0;
        }
        // 如果修改的其他参数都为空，则不需要更新profule表中中数据
        if (StringUtils.isEmpty(inModel.getAvatar()) && StringUtils.isEmpty(inModel.getSex())
                && StringUtils.isEmpty(inModel.getCountry()) && StringUtils.isEmpty(inModel.getProvince())
                && StringUtils.isEmpty(inModel.getCity()) && StringUtils.isEmpty(inModel.getCounty())
                && StringUtils.isEmpty(inModel.getResume())) {
            return success;
        }
        success = userProfileWMapper.updateProfile(inModel) > 0;
        return success;
    }

    /**
     * 详情页面右侧作者模块
     */
    @Cacheable(key = "#{request.articleId}#{request.userId}_#{request.port}", expire = 1, dateType = DateEnum.HOURS)
    @Override
    public Result<UserProfileResponse> selectByuserprofileId(UserProfileRequest request) {
		long now = System.currentTimeMillis();
		logger.info("详情页面右侧作者入参：UserProfileRequest={}", JSON.toJSONString(request));

		Long userId;
		if(request.getUserId()==null){
			ArticleOutModel article = articleRMapper.selectByPrimaryKey(request.getArticleId());
			userId = article.getCreateUser();
		}else {
			userId = request.getUserId();
		}

		UserProfileInModel inModel = new UserProfileInModel();
		inModel.setUserId(userId);
		List<UserProfileOutModel> outModels = Lists.newArrayList();
		long fansCount = 0;
		if("M".equals(request.getPort())){
			//统计粉丝数量
			UserConcernInModel userConcernInModel = new UserConcernInModel();
			userConcernInModel.setUserId(request.getUserId());
			fansCount = userConcernRMapper.getFansCount(userConcernInModel);

		}else {
			outModels = userProfileRMapper.selectByuserprofileId(inModel);
		}

		//        UserProfileOutModel outModel = userProfileRMapper.countCollect(inModel);
		UserProfileOutModel comments = userProfileRMapper.countComments(inModel);
		UserProfileOutModel outcount = userProfileRMapper.bowsenumCount(inModel);
		ArticleInModel articleInModel = new ArticleInModel();
		articleInModel.setCreateUser(userId);
		articleInModel.setPublishStatus("1");
		// 统计文章总数
		long countArt = articleRMapper.selectCountArticle(articleInModel);
		// 统计作者总点赞数
		KgArticleStatisticsInModel kaSinModel = new KgArticleStatisticsInModel();
		kaSinModel.setCreateUser(userId);
		List<ArticleResponse> atresponse = new ArrayList<>();
		// 存入统计个数
		UserProfileResponse response = new UserProfileResponse();
		// 存入作者信息
		UserProfileOutModel profileOutModel = userProfileRMapper.getMedia(inModel);
		if (profileOutModel != null) {
			response.setColumnName(profileOutModel.getColumnName());
			response.setColumnAvatar(profileOutModel.getColumnAvatar());
			response.setColumnIntro(profileOutModel.getColumnIntro());
           /* if (outModel != null) {
                response.setCollect(outModel.getCollect());
            }*/
			if (comments != null) {
				response.setComments(comments.getComments());
			}
			if (outcount != null) {
				response.setPbowsenum(outcount.getPbowsenum());
			}
			response.setArtsum(countArt);
			response.setCreateUser(profileOutModel.getUserId().toString());
           /* long count = kgArticleStatisticsRMapper.getThumbupNum(kaSinModel);
            response.setThumbupNum(count);*/
			response.setFansCount(fansCount);
		}

		//返回专栏标签
		UserTagBuild userTagBuild = userTagsUtil.buildTags(userId);
		response.setIdentityTag(userTagBuild.getIdentityTag());
		response.setVipTag(userTagBuild.getVipTag());
		response.setRealAuthedTag(userTagBuild.getRealAuthedTag());
		// 存入文章信息
		if(!"M".equals(request.getPort())){
			for (UserProfileOutModel userProfileOutModel : outModels) {
				ArticleResponse articleResponse = new ArticleResponse();
				articleResponse.setArticleId(userProfileOutModel.getArticle_id());
				articleResponse.setColumnname(userProfileOutModel.getKgcolumnname());
				articleResponse.setUpdateDate(DateUtils.calculateTime(userProfileOutModel.getUpdate_date()));
				articleResponse.setBowseNum(userProfileOutModel.getBowse_num());
				articleResponse.setArticleTitle(userProfileOutModel.getArticle_title());
				articleResponse.setCreateUser(userProfileOutModel.getCreateUser());
				if (userProfileOutModel.getAuditDate() != null) {
					articleResponse.setUpdateTimestamp(String.valueOf(userProfileOutModel.getAuditDate().getTime()));
				} else {
					articleResponse.setUpdateTimestamp(String.valueOf(userProfileOutModel.getUpdate_date().getTime()));
				}

				articleResponse.setArticleImgSize(userProfileOutModel.getArticleImgSize());
				articleResponse.setArticleImage(userProfileOutModel.getArticleImage());
				articleResponse.setPublishKind(userProfileOutModel.getPublishKind());
				kaSinModel.setArticleId(Long.parseLong(userProfileOutModel.getArticle_id()));
				atresponse.add(articleResponse);
			}
		}

		if(atresponse.size()>0){
			response.setList(atresponse);
		}

		logger.info("【selectByuserprofileId -> 作者数据】response:" + JSONObject.toJSONString(response));
		System.out.println("------专栏主页信息耗时："+(System.currentTimeMillis()-now)+"ms");
		return new Result<>(response);
    }

    /**
     * 首页热门作者
     */
    @Cacheable(key = "getUserproFile", expire = 1, dateType = DateEnum.HOURS)
    @Override
    public Result<List<UserProfileResponse>> getUserproFile() {
        List<UserProfileOutModel> list = userProfileRMapper.getUserproFile();
//        List<UserProfileOutModel> listnum = null;
//        if (list.size() < 10) {
//            UserProfileInModel inModel = new UserProfileInModel();
//            inModel.setLimit(10 - list.size());
//            listnum = userProfileRMapper.getBowsnum(inModel);
//        }
        List<UserProfileResponse> listresponse = new ArrayList<UserProfileResponse>();
        for (UserProfileOutModel userProfileOutModel : list) {
            UserProfileResponse response = new UserProfileResponse();
            response.setUserId(userProfileOutModel.getUserId().toString());
            response.setCreateUser(userProfileOutModel.getUserId().toString());
            response.setColumnName(userProfileOutModel.getColumnName());
            response.setColumnAvatar(userProfileOutModel.getColumnAvatar());
            response.setColumnIntro(userProfileOutModel.getColumnintro());
            response.setArtsum(userProfileOutModel.getArtsum());
            response.setCreateDate(DateUtils.calculateTime(userProfileOutModel.getCreateDate()));
            response.setBowse_num(userProfileOutModel.getBowse_num());
            UserProfileInModel inModel = new UserProfileInModel();
            inModel.setUserId(userProfileOutModel.getUserId());

            // 存入文章信息
            List<UserProfileOutModel> outModels = userProfileRMapper.selectByuserprofileIdWithHotAuthor(inModel);
            List<ArticleResponse> listresponses = new ArrayList<ArticleResponse>();
            for (UserProfileOutModel OutModel : outModels) {
                ArticleResponse aresponse = new ArticleResponse();
                aresponse.setArticleTitle(OutModel.getArticle_title());
                aresponse.setUpdateDate(DateUtils.calculateTime(OutModel.getUpdate_date()));
                if (OutModel.getAuditDate() != null) {
                    aresponse.setUpdateTimestamp(String.valueOf(OutModel.getAuditDate().getTime()));
                } else {
                    aresponse.setUpdateTimestamp(String.valueOf(OutModel.getUpdate_date().getTime()));
                }

                aresponse.setColumnId(OutModel.getKgcolumnId());
                aresponse.setColumnname(OutModel.getKgcolumnname());

                aresponse.setArticleId(OutModel.getArticle_id());
                listresponses.add(aresponse);
            }
            if (listresponses.size() == 0) {
                response.setList(null);
            } else {
                response.setList(listresponses);
            }

            listresponse.add(response);
        }

//        if (listnum != null) {
//            for (UserProfileOutModel OutModel : listnum) {
//                UserProfileResponse responseout = new UserProfileResponse();
//
//                responseout.setUserId(OutModel.getUserId().toString());
//                responseout.setUserName(OutModel.getUsername());
//                responseout.setCreateUser(OutModel.getUserId().toString());
//                responseout.setColumnName(OutModel.getColumnName());
//                responseout.setColumnAvatar(OutModel.getColumnAvatar());
//                responseout.setColumnIntro(OutModel.getColumnintro());
//                responseout.setArtsum(OutModel.getArtsum());
//                responseout.setBowse_num(OutModel.getBowse_num());
//                UserProfileInModel inModel = new UserProfileInModel();
//                inModel.setUserId(OutModel.getUserId());
//                List<UserProfileOutModel> outModels = userProfileRMapper.selectByuserprofileIdWithHotAuthor(inModel);
//                List<ArticleResponse> listresponses = new ArrayList<ArticleResponse>();
//                for (UserProfileOutModel pOutModel : outModels) {
//                    ArticleResponse aresponse = new ArticleResponse();
//                    aresponse.setArticleTitle(pOutModel.getArticle_title());
//                    aresponse.setUpdateDate(DateUtils.calculateTime(pOutModel.getUpdate_date()));
//                    aresponse.setUpdateTimestamp(String.valueOf(pOutModel.getAuditDate().getTime()));
//                    aresponse.setColumnId(pOutModel.getKgcolumnId());
//                    aresponse.setColumnname(pOutModel.getKgcolumnname());
//                    aresponse.setArticleId(pOutModel.getArticle_id());
//                    // 热门作者ArticleResponse出参用Profilename显示文章所在栏目
//                    listresponses.add(aresponse);
//
//                }
//                if (listresponses.size() == 0) {
//                    responseout.setList(null);
//                } else {
//                    responseout.setList(listresponses);
//                }
//
//                listresponse.add(responseout);
//            }
//        }

        return new Result<List<UserProfileResponse>>(listresponse);

    }

    /**
     * 申请专栏
     */
    @Override
    public boolean addUserProfile(UserProfileRequest request) {
        logger.info("申请专栏入参：UserProfileRequest={}", JSON.toJSONString(request));
        boolean success;
        UserProfileInModel inModel = new UserProfileInModel();
        inModel.setUserId(request.getUserId());
        inModel.setRoleDefine(BaseUtil.isNullReplace(request.getRoleDefine()));
        inModel.setColumnName(BaseUtil.isNullReplace(request.getColumnName()));
        inModel.setColumnIntro(BaseUtil.isNullReplace(request.getColumnIntro()));
        inModel.setColumnAvatar(BaseUtil.isNullReplace(request.getColumnAvatar()));
        inModel.setColumnCountry(BaseUtil.isNullReplace(request.getColumnCountry()));
        inModel.setColumnProvince(BaseUtil.isNullReplace(request.getColumnProvince()));
        inModel.setColumnCity(BaseUtil.isNullReplace(request.getColumnCity()));
        inModel.setColumnCounty(BaseUtil.isNullReplace(request.getColumnCounty()));

        inModel.setEmail(BaseUtil.isNullReplace(request.getEmail()));
        inModel.setMobile(BaseUtil.isNullReplace(request.getMobile()));

        inModel.setIdcard(BaseUtil.isNullReplace(request.getIdcard()));
        inModel.setIdcardPic(BaseUtil.isNullReplace(request.getIdcardPic()));
        inModel.setRealName(BaseUtil.isNullReplace(request.getRealName()));
        inModel.setRoleId(BaseUtil.isNullReplace(request.getApplyRole()));
        inModel.setIdcardFront(request.getIdcardFront());
        inModel.setIdcardBack(request.getIdcardBack());
        inModel.setOtherPic(request.getOtherPic());

        int applyRole = Integer.parseInt(request.getApplyRole());
        // 媒体 or企业 or 组织
        if (applyRole == UserRoleEnum.MEDIA.getRole() || applyRole == UserRoleEnum.ENTERPRICE.getRole()
                || applyRole == UserRoleEnum.OTHER.getRole()) {
            inModel.setCompanyName(BaseUtil.isNullReplace(request.getCompanyName()));
            inModel.setLicensePic(BaseUtil.isNullReplace(request.getLicensePic()));
            inModel.setSiteLink(request.getSiteLink());
            String a = request.getOtherPic().replace("[", "").replace("]", "");
            // a.replaceAll("\"", "\\\"");
            String newStr = a.replaceAll("\"", "");
            inModel.setOtherPic(newStr);
        }

        UserInModel model = new UserInModel();
        model.setUserId(request.getUserId());
        model.setApplyRole(Integer.parseInt(request.getApplyRole()));
        model.setUserLevel(Integer.parseInt(request.getApplyRole()));
        model.setAuditStatus(AuditStatusEnum.AUDITING.getStatus());

        UserProfileOutModel profileOutModel = userProfileRMapper.selectByPrimaryKey(request.getUserId());
        if (profileOutModel != null) {
            success = userProfileWMapper.updateProfile(inModel) > 0;
        } else {
            success = userProfileWMapper.insertSelective(inModel) > 0;
        }

        UserActiveInModel uAinModel = new UserActiveInModel();
        uAinModel.setUserId(request.getUserId());
        UserActiveOutModel activeOutModel = userActiveRMapper.selectByUserKey(uAinModel);
        uAinModel.setApplyColumnTime(new Date());
        if (activeOutModel != null) {
            success = userActiveWMapper.updateUserSelective(uAinModel) > 0;
        } else {
            success = userActiveWMapper.insertUserSelective(uAinModel) > 0;
        }

        success = userWMapper.UpdateRole(model) > 0;
        if (success == false) {
            return false;
        }
        return success;

    }

    /**
     * 获取用户资料
     */
    @Override
    public Result<UserProfileResponse> getProfile(UserProfileRequest request, HttpServletRequest req) {

        logger.info("获取用户资料入参：UserProfileRequest={}", JSON.toJSONString(request));

        IPDataModel ipData = ipUtil.getIpData(HttpUtil.getIpAddr(req));
        String defCountry = null;
        String defProvince = null;
        String defCity = null;
        String defCounty = null;
        if (ipData != null) {
            defCountry = (ipData.getCountry().equals("内网IP") || ipData.getCountry().equalsIgnoreCase("XX")) ? ""
                    : ipData.getCountry();
            defProvince = (ipData.getRegion().equals("内网IP") || ipData.getRegion().equalsIgnoreCase("XX")) ? ""
                    : ipData.getRegion();
            defCity = (ipData.getCity().equals("内网IP") || ipData.getCity().equalsIgnoreCase("XX")) ? ""
                    : ipData.getCity();
            defCounty = (ipData.getCounty().equals("内网IP") || ipData.getCounty().equalsIgnoreCase("XX")) ? ""
                    : ipData.getCounty();
            defProvince = (StringUtils.isNotBlank(defProvince) && (defProvince.indexOf("省") == -1)) ? defProvince + "省"
                    : defProvince;
            defCity = (StringUtils.isNotBlank(defCity) && (defCity.indexOf("市") == -1)) ? defCity + "市" : defCity;
            defCounty = (StringUtils.isNotBlank(defCounty) && (defCounty.indexOf("区") == -1)) ? defCounty + "区"
                    : defCounty;
        }

        UserProfileInModel inModel = new UserProfileInModel();
        UserProfileResponse response = new UserProfileResponse();
        inModel.setUserId(request.getUserId());
        UserProfileOutModel model = userProfileRMapper.getMedia(inModel);
        if (model != null) {
            response.setAvatar(model.getAvatar());
            response.setSex(model.getSex());

            response.setCountry(StringUtils.isBlank(model.getCountry()) ? defCountry : model.getCountry());
            response.setProvince(StringUtils.isBlank(model.getProvince()) ? defProvince : model.getProvince());
            response.setCity(StringUtils.isBlank(model.getCity()) ? defCity : model.getCity());
            response.setCounty(StringUtils.isBlank(model.getCounty()) ? defCounty : model.getCounty());

            response.setResume(model.getResume());
            response.setUserId(model.getUserId().toString());
            response.setUserName(model.getUsername());

            response.setRoleDefine(model.getRoleDefine());

            response.setColumnName(model.getColumnName());
            response.setColumnIntro(model.getColumnIntro());
            response.setColumnAvatar(model.getColumnAvatar());

            response.setColumnCountry(
                    StringUtils.isBlank(model.getColumnCountry()) ? defCountry : model.getColumnCountry());
            response.setColumnProvince(
                    StringUtils.isBlank(model.getColumnProvince()) ? defProvince : model.getColumnProvince());
            response.setColumnCity(StringUtils.isBlank(model.getColumnCity()) ? defCity : model.getColumnCity());
            response.setColumnCounty(
                    StringUtils.isBlank(model.getColumnCounty()) ? defCounty : model.getColumnCounty());

            String realName = model.getRealName();
            if (StringUtils.isNotEmpty(realName)) {
                response.setRealName(realName.substring(0, 1) + com.kg.platform.common.utils.StringUtils.getChar(realName.length()));
            }
            String idcard = model.getIdcard();
            if (StringUtils.isNotEmpty(idcard)) {
                response.setIdcard(model.getIdcard().replaceAll("(\\d{1})\\d{16}(\\d{1})", "$1****************$2"));
            }
            response.setIdcardPic(model.getIdcardPic());
            response.setEmail(model.getEmail());
            String mobile = model.getMobile();
            if (StringUtils.isNotEmpty(mobile)) {
                response.setMobile(model.getMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
            }
            response.setOtherPic(model.getOtherPic());
            response.setCompanyName(model.getCompanyName());
            response.setLicensePic(model.getLicensePic());
            response.setSiteLink(model.getSiteLink());
            response.setRealnameAuthed(model.getRealnameAuthed());
            response.setTradepasswordSet(model.getTradepasswordSet());
            response.setUserRole(model.getUserRole());

            UserTagBuild userTagBuild = userTagsUtil.buildTags(request.getUserId());
            response.setVipTag(userTagBuild.getVipTag());
            response.setIdentityTag(userTagBuild.getIdentityTag());
            response.setRealAuthedTag(userTagBuild.getRealAuthedTag());

        }
        return new Result<>(response);
    }


    /**
     * 检查ID 有没有申请专栏
     *
     * @param request
     * @return
     */
    public boolean checkProfile(UserProfileRequest request) {
        UserProfileInModel inModel = new UserProfileInModel();
        inModel.setUserId(request.getUserId());
        return userProfileRMapper.checkProfile(inModel) > 0;

    }

    /**
     * 他人主页
     */
    @Override
    public Result<UserProfileResponse> getByIdProfile(UserProfileRequest request) {
        logger.info("他人主页入参：UserProfileRequest={}", JSON.toJSONString(request));
        UserProfileInModel inModel = new UserProfileInModel();
        inModel.setUserId(request.getUserId());
        UserProfileOutModel model = userProfileRMapper.getByIdProfile(inModel);
        UserProfileResponse response = new UserProfileResponse();
        if (model != null) {
            response.setAvatar(model.getAvatar());
            response.setResume(model.getResume());
            response.setUserName(model.getUsername());
            response.setUserId(model.getUserId().toString());
            response.setProvince(model.getProvince());
            response.setCity(model.getCity());
            response.setUserRole(model.getUserRole());
            response.setColumnAvatar(model.getColumnAvatar());
            UserTagBuild userTagBuild = userTagsUtil.buildTags(request.getUserId());
            response.setIdentityTag(userTagBuild.getIdentityTag());
            response.setVipTag(userTagBuild.getVipTag());
            response.setRealAuthedTag(userTagBuild.getRealAuthedTag());
        }
        return new Result<UserProfileResponse>(response);
    }

    /**
     * 判断该作者下是否有文章
     */
    @Override
    public boolean countArticle(UserProfileRequest request) {
        UserProfileInModel inModel = new UserProfileInModel();
        inModel.setUserId(request.getUserId());
        return userProfileRMapper.countArticle(inModel) > 0;
    }

    @Override
    public boolean initUserProfile(UserProfileRequest request) {
        UserProfileInModel inModel = new UserProfileInModel();
        inModel.setUserId(request.getUserId());
        inModel.setAvatar(request.getAvatar());
        inModel.setRoleId(request.getRoleId() + "");
        return userProfileWMapper.insertSelective(inModel) > 0;
    }

    @Override
    public UserProfileOutModel getUserProfile(UserProfileRequest request) {
        UserProfileInModel userProfileInModel = new UserProfileInModel();
        userProfileInModel.setUserId(request.getUserId());
        return userProfileRMapper.getByIdProfile(userProfileInModel);
    }

}
