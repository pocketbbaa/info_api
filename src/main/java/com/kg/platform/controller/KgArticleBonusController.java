// package com.kg.platform.controller;
//
// import java.math.BigDecimal;
// import java.util.List;
//
// import javax.inject.Inject;
//
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.RequestAttribute;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.ResponseBody;
//
// import com.kg.platform.common.aop.BaseControllerNote;
// import com.kg.platform.common.context.Result;
// import com.kg.platform.common.entity.JsonEntity;
// import com.kg.platform.common.exception.ExceptionEnum;
// import com.kg.platform.common.utils.CheckUtils;
// import com.kg.platform.enumeration.ArticleAuditStatusEnum;
// import com.kg.platform.enumeration.BonusStatusEnum;
// import com.kg.platform.model.request.ArticleRequest;
// import com.kg.platform.model.request.KgArticleBonusRequest;
// import com.kg.platform.model.response.ArticleResponse;
// import com.kg.platform.model.response.KgArticleBonusResponse;
// import com.kg.platform.service.ArticlekgService;
// import com.kg.platform.service.KgArticleBonusService;
//
// @Controller
// @RequestMapping("kgArticleBonus")
// public class KgArticleBonusController extends ApiBaseController {
//
// @Inject
// KgArticleBonusService articleBonusService;
//
// @Inject
// ArticlekgService articlekgService;
//
// /**
// * 文章列表设置阅读奖励
// *
// * @return addArticleBonus
// */
// @ResponseBody
// @RequestMapping("addArticleBonus")
// @BaseControllerNote(needCheckToken = false, needCheckParameter = true,
// beanClazz = KgArticleBonusRequest.class)
// public JsonEntity addKgArticleBonus(@RequestAttribute KgArticleBonusRequest
// request) {
// if (null == request.getArticleId() || null == request.getCreateUser() || null
// == request.getList()) {
// return JsonEntity.makeExceptionJsonEntity("", "参数有误，请检查输入。");
// }
//
// /*
// * if (request.getCreateUser().longValue() != Constants.BIG_BOSS_ID) {
// * // 只有尚币哥账号才能设置随机奖励 for (KgArticleBonusRequest req :
// * request.getList()) { if (req.getBonusKind().intValue() == 2) { return
// * JsonEntity.makeExceptionJsonEntity("", "参数有误，请检查输入。"); } } }
// */
// for (KgArticleBonusRequest req : request.getList()) {
// if (req.getBonusValue().compareTo(BigDecimal.valueOf(0.001)) < 0) {
// return JsonEntity.makeExceptionJsonEntity("", "参数有误，请检查输入。");
// }
// if (req.getBonusKind().intValue() == 2) {
// if (req.getBonusValue()
// .divide(BigDecimal.valueOf(req.getMaxPeople().intValue()), 3,
// BigDecimal.ROUND_FLOOR)
// .compareTo(BigDecimal.valueOf(0.001)) < 0) {
// return JsonEntity.makeExceptionJsonEntity("", "参数有误，请检查输入。");
// }
// }
// }
//
// ArticleRequest req = new ArticleRequest();
// req.setArticleId(request.getArticleId());
// Result<ArticleResponse> result = articlekgService.getArticleContent(req);
// CheckUtils.checkRetInfo(result);
// int publishStatus = result.getData().getPublishStatus();
// if (publishStatus == ArticleAuditStatusEnum.AUDITING.getStatus()) {
// return JsonEntity.makeExceptionJsonEntity("", "当前文章状态不能编辑阅读奖励");
// }
//
// List<KgArticleBonusResponse> articleBonus =
// result.getData().getListAllBonus();
// if (null != articleBonus && articleBonus.size() > 0) {
// int bonusStatus = articleBonus.get(0).getBonusStatus().intValue();
// if (bonusStatus != BonusStatusEnum.NOTVALID.getStatus()
// && bonusStatus != BonusStatusEnum.TERMINATED.getStatus()) {
// return JsonEntity.makeExceptionJsonEntity("", "当前阅读奖励不能编辑");
// }
// }
//
// boolean success = articleBonusService.addArticleBonus(request);
// if (success) {
// return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
// ExceptionEnum.SUCCESS.getMessage());
// }
// return JsonEntity.makeExceptionJsonEntity("20033", "设置阅读奖励失败");
//
// }
//
// }
