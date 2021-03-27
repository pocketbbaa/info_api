package com.kg.platform.service;

import com.kg.platform.common.entity.AppJsonEntity;
import com.kg.platform.common.entity.JsonEntity;
import com.kg.platform.model.request.TxPasswordEditRequest;
import com.kg.platform.model.request.UserCertEditRequest;
import com.kg.platform.model.response.UserkgResponse;

public interface LynnService {

    JsonEntity setTxPassword(TxPasswordEditRequest request, UserkgResponse kguser);

    JsonEntity userCert(UserCertEditRequest request);

    AppJsonEntity getTxPasswdCode(TxPasswordEditRequest request, UserkgResponse kguser);

    JsonEntity getWebTxPasswdCode(TxPasswordEditRequest request, UserkgResponse kguser);

    JsonEntity updateTxPassword(TxPasswordEditRequest request, UserkgResponse kguser);
}
