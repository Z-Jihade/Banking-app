package com.project.accounts.services.impl;

import com.project.accounts.constants.AccountConstants;
import com.project.accounts.dtos.CustomerDto;
import com.project.accounts.entities.Account;
import com.project.accounts.entities.Customer;
import com.project.accounts.exceptions.CustomerAlreadyExistsException;
import com.project.accounts.mappers.CustomerMapper;
import com.project.accounts.repositories.AccountRepository;
import com.project.accounts.repositories.CustomerRepository;
import com.project.accounts.services.IAccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
@Service
@AllArgsConstructor
public  class AccountServiceProcessImpl implements IAccountService {

    private AccountRepository accountRepository;
    private CustomerRepository customerRepository;

    /**
     *
     * @param customerDto customerDto object
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    +customerDto.getMobileNumber());
        }
        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("Anonymous");
        Customer savedCustomer = customerRepository.save(customer);
        accountRepository.save(createNewAccount(savedCustomer));
    }

    /**
     *
     * @param customer
     * @return
     */
    private Account createNewAccount(Customer customer){
        Account newAccount = new Account();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountConstants.SAVINGS);
        newAccount.setBranchAddress(AccountConstants.ADDRESS);
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setCreatedBy("Anonymous");
        return newAccount;
    }
}
