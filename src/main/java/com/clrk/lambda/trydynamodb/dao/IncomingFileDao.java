package com.clrk.lambda.trydynamodb.dao;

import com.clrk.lambda.trydynamodb.dto.IncomingFile;

public interface IncomingFileDao {
    void save(IncomingFile incomingFiles);
    void update(IncomingFile incomingFile);
}
