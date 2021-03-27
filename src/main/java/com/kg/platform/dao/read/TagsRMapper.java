package com.kg.platform.dao.read;

import java.util.List;

import com.kg.platform.dao.entity.Tags;
import com.kg.platform.model.in.TagsInModel;
import com.kg.platform.model.out.TagsOutModel;

public interface TagsRMapper {

    Tags selectByPrimaryKey(Long tagId);

    TagsOutModel getTags(TagsInModel inModel);

    List<TagsOutModel> listTags();

    List<TagsOutModel> listTagsForSitemap(TagsInModel inModel);

    long countTagsForSitemap();
}
