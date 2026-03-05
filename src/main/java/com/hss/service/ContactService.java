package com.hss.service;

import com.hss.pojo.contactUs;

public interface ContactService {
    void add(contactUs contact);

    contactUs query();
}
