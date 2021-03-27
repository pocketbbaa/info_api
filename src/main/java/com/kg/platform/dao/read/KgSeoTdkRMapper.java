package com.kg.platform.dao.read;

import com.kg.platform.model.in.KgSeoTdkInModel;
import com.kg.platform.model.out.KgSeoTdkOutModel;

public interface KgSeoTdkRMapper {


	KgSeoTdkOutModel selectByPrimaryKey(Integer tdkId);

    KgSeoTdkOutModel detailTdk(KgSeoTdkInModel inModel);
}