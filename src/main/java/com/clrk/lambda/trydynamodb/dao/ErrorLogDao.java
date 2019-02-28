package com.clrk.lambda.trydynamodb.dao;

import com.clrk.lambda.trydynamodb.dto.ErrorLog;

public interface ErrorLogDao {
    void save(ErrorLog errorLog);
}
