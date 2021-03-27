package com.kg.platform.dao.read;

import java.util.List;

import com.kg.platform.dao.entity.Siteimage;
import com.kg.platform.model.in.SiteimageInModel;
import com.kg.platform.model.out.SiteimageOutModel;

public interface SiteimageRMapper {

    Siteimage selectByPrimaryKey(Integer imageId);

    List<SiteimageOutModel> selectAll(SiteimageInModel model);

    List<SiteimageOutModel> selectAllForApp(SiteimageInModel model);

}
