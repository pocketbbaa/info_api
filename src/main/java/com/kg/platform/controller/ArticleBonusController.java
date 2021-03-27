// package com.kg.platform.controller;
//
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
// import com.kg.platform.model.request.KgArticleBonusRequest;
// import com.kg.platform.model.response.KgArticleBonusResponse;
// import com.kg.platform.service.KgArticleBonusService;
//
// @Controller
// @RequestMapping("articleBonus")
// public class ArticleBonusController extends ApiBaseController {
//
// @Inject
// KgArticleBonusService articleBonusService;
//
// /**
// * 查询当前文章下的阅读奖励
// *
// * @param request
// * @return
// */
// @ResponseBody
// @RequestMapping("getArticleBonus")
// @BaseControllerNote(needCheckParameter = true, beanClazz =
// KgArticleBonusRequest.class)
// public JsonEntity getArticleBonus(@RequestAttribute KgArticleBonusRequest
// request) {
// Result<List<KgArticleBonusResponse>> response =
// articleBonusService.getArticleBonus(request);
// if (response.getData() != null) {
// return JsonEntity.makeSuccessJsonEntity(response);
// }
// return
// JsonEntity.makeExceptionJsonEntity(ExceptionEnum.PARAMEMPTYERROR.getCode(),
// ExceptionEnum.PARAMEMPTYERROR.getMessage());
// }
//
// /**
// * 修改阅读奖励
// *
// * @param request
// * @return
// */
// @ResponseBody
// @RequestMapping("updateBonus")
// @BaseControllerNote(needCheckToken = false, needCheckParameter = true,
// beanClazz = KgArticleBonusRequest.class)
// public JsonEntity updateBonus(@RequestAttribute KgArticleBonusRequest
// request) {
//
// boolean success = articleBonusService.updateBonus(request);
// if (success) {
// return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
// ExceptionEnum.SUCCESS.getMessage());
// }
// return JsonEntity.makeExceptionJsonEntity("", "修改失败");
// }
//
// @ResponseBody
// @RequestMapping("updateStatus")
// @BaseControllerNote(needCheckParameter = true, beanClazz =
// KgArticleBonusRequest.class)
// public JsonEntity updateStatus(@RequestAttribute KgArticleBonusRequest
// request) {
//
// boolean success = articleBonusService.updateStatus(request);
// if (success) {
// return JsonEntity.makeExceptionJsonEntity(ExceptionEnum.SUCCESS.getCode(),
// ExceptionEnum.SUCCESS.getMessage());
// }
// return JsonEntity.makeExceptionJsonEntity("", "修改失败");
// }
//
// }
