package com.hss.service.impl;

import com.hss.mapper.ContactMapper;
import com.hss.pojo.contactUs;
import com.hss.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactMapper contactMapper;
    @Override
    public void add(contactUs contact) {
        contactMapper.add(contact);
    }

    @Override
    public contactUs query() {
        return contactMapper.query();
    }
}
