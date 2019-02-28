package com.clrk.lambda.trydynamodb.dao;

import com.clrk.lambda.trydynamodb.dto.TelephoneRecord;

import java.util.List;

public interface TelephoneBookDao {

    void save(List<TelephoneRecord> telephoneRecord);

    void save(TelephoneRecord telephoneRecord);
}
