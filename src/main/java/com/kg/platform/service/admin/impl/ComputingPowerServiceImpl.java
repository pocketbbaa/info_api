package com.kg.platform.service.admin.impl;

import com.google.common.collect.Lists;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.common.entity.PageModel;
import com.kg.platform.common.utils.DateUtils;
import com.kg.platform.dao.cloud.CloudOrderMapper;
import com.kg.platform.dao.entity.CloudOrder;
import com.kg.platform.model.request.admin.CloudOrderRequest;
import com.kg.platform.model.response.admin.CloudOrderResponse;
import com.kg.platform.service.admin.ComputingPowerService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2019/2/22.
 */
@Service
public class ComputingPowerServiceImpl implements ComputingPowerService {

	@Autowired
	private CloudOrderMapper cloudOrderMapper;

	@Override
	public JsonEntity list(CloudOrderRequest request) {
		PageModel<CloudOrderResponse> pageModel = new PageModel<>();
		pageModel.setCurrentPage(request.getCurrentPage());
		pageModel.setPageSize(request.getPageSize());
		CloudOrder record = new CloudOrder();
		record.setStart((request.getCurrentPage()-1)*request.getPageSize());
		BeanUtils.copyProperties(request, record);
		List<CloudOrder> outModels = cloudOrderMapper.selectByCondition(record);
		if(CollectionUtils.isEmpty(outModels)){
			return JsonEntity.makeSuccessJsonEntity(pageModel);
		}
		List<CloudOrderResponse> responses = Lists.newArrayList();
		outModels.forEach(item->{
			CloudOrderResponse response = new CloudOrderResponse();
			response.setId(item.getId().toString());
			response.setPhone(item.getPhone());
			response.setPayState(item.getPayState());
			response.setPackageName(item.getPackageName());
			response.setNumber(item.getNumber());
			response.setPrice(BigDecimal.valueOf(item.getPrice()).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());
			response.setPerformance(BigDecimal.valueOf(item.getPerformance()).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());
			response.setChannelId(item.getChannelId().toString());
			response.setCreateTime(DateUtils.formatDate(item.getCreateTime(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
			response.setPayTime(DateUtils.formatDate(item.getPayTime(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
			response.setState(item.getState());
			response.setSysUserName(item.getSysUserName());
			responses.add(response);
		});
		pageModel.setData(responses);
		long count = cloudOrderMapper.selectCountByCondition(record);
		pageModel.setTotalNumber(count);
		return JsonEntity.makeSuccessJsonEntity(pageModel);
	}

	@Override
	public JsonEntity detail(CloudOrderRequest request) {
		CloudOrderResponse response = new CloudOrderResponse();
		CloudOrder outModel = cloudOrderMapper.selectByPrimaryKey(request.getId());
		if(outModel==null){
			return JsonEntity.makeSuccessJsonEntity(response);
		}
		response.setId(outModel.getId().toString());
		response.setPhone(outModel.getPhone());
		response.setPackageName(outModel.getPackageName());
		response.setNumber(outModel.getNumber());
		response.setPrice(BigDecimal.valueOf(outModel.getPrice()).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());
		response.setPerformance(BigDecimal.valueOf(outModel.getPerformance()).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());
		response.setChannelId(outModel.getChannelId().toString());
		response.setCreateTime(DateUtils.formatDate(outModel.getCreateTime(), DateUtils.DATE_FORMAT_YYYY_MM_DD_HH_MM_SS));
		response.setState(outModel.getState());
		response.setTotalRevenue(BigDecimal.valueOf(outModel.getTotalRevenue()).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());
		return JsonEntity.makeSuccessJsonEntity(response);
	}

	@Override
	public JsonEntity update(CloudOrderRequest request) {
		CloudOrder record = new CloudOrder();
		BeanUtils.copyProperties(request, record);
		if(request.getPayState()==1){
			record.setState(0);
			record.setPayTime(new Date());
		}
		cloudOrderMapper.updateByPrimaryKeySelective(record);
		return JsonEntity.makeSuccessJsonEntity();
	}
}
