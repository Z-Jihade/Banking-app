package com.project.accounts.services;

import com.project.accounts.dtos.CustomerDto;

public interface IAccountService {

    /**
     *
     * @param customerDto customerDto object
     */
    void createAccount(CustomerDto customerDto);
}
