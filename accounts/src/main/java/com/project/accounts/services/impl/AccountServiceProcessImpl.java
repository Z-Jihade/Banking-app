package com.project.accounts.services.impl;

import com.project.accounts.constants.AccountConstants;
import com.project.accounts.dtos.AccountDto;
import com.project.accounts.dtos.CustomerDto;
import com.project.accounts.entities.Account;
import com.project.accounts.entities.Customer;
import com.project.accounts.exceptions.CustomerAlreadyExistsException;
import com.project.accounts.exceptions.ResourceNotFoundException;
import com.project.accounts.mappers.AccountMapper;
import com.project.accounts.mappers.CustomerMapper;
import com.project.accounts.repositories.AccountRepository;
import com.project.accounts.repositories.CustomerRepository;
import com.project.accounts.services.IAccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        Customer createdCustomer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if(optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    +customerDto.getMobileNumber());
        }
        createdCustomer.setCreatedAt(LocalDateTime.now());
        createdCustomer.setCreatedBy("Anonymous");
        Customer savedCustomer = customerRepository.save(createdCustomer);
        accountRepository.save(createNewAccount(savedCustomer));
    }

    /**
     *
     * @param mobileNumber
     * @return
     */
    @Override
    public CustomerDto fetchAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        Account account = accountRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "id", customer.getCustomerId().toString())
        );
        CustomerDto fetchedCustomer = new CustomerDto();
        CustomerMapper.mapToCustomerDto(customer, fetchedCustomer);
        AccountMapper.mapToAccountDto(account, fetchedCustomer.getAccountsDto());
        return fetchedCustomer;
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

    @Override
    public boolean updateAccount(CustomerDto customerDto) {
        boolean isUpdated = false;
        AccountDto accountDto = customerDto.getAccountsDto();
        if (accountDto !=null ){
            Account account = accountRepository.findByAccountNumber(accountDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "Number", accountDto.getAccountNumber().toString())
            );
            AccountMapper.mapToAccount(accountDto, account);
            accountRepository.save(account);

            Customer customer = customerRepository.findByMobileNumber(customerDto.getMobileNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "Mobile Number", customerDto.getMobileNumber().toString())
            );
            CustomerMapper.mapToCustomer(customerDto, customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "MobileNumber", mobileNumber)
        );
        accountRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }

}
