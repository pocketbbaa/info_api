package com.kg.platform.service;

import java.util.List;

import com.kg.platform.common.context.Result;
import com.kg.platform.model.request.FriendlyLinkRequest;
import com.kg.platform.model.response.FriendlyLinkResponse;

public interface FriendlylinkService {

    Result<List<FriendlyLinkResponse>> getFriendlyAll(FriendlyLinkRequest request);

}
