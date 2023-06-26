package com.devsu.bankapi;

import com.devsu.bankapi.entities.Customer;
import com.devsu.bankapi.entities.keys.PersonKey;
import com.devsu.bankapi.repositories.AccountRepository;
import com.devsu.bankapi.repositories.CustomerRepository;
import com.devsu.bankapi.repositories.EmployeeRepository;
import com.devsu.bankapi.service.CustomerService;
import com.devsu.bankapi.utils.dto.request.CreateCustomerRequest;
import com.devsu.bankapi.utils.dto.response.CustomerResponse;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class UnitCustomerTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    private final CustomerService customerService;

    public UnitCustomerTest() {
        MockitoAnnotations.openMocks(this);
        customerService = new CustomerService(customerRepository, employeeRepository,
                new ModelMapper(), accountRepository);
    }

    @Test
    public void tryCreateCustomerWithoutExistingEmployee(){

        CreateCustomerRequest customerRequest = new CreateCustomerRequest();
        customerRequest.setFunctionary(2);

        given(employeeRepository.existsByEmployeeCode(2))
                .willReturn(false);

        CustomerResponse customerResponse = customerService.createCustomer(
                customerRequest
        );

        assertThat(customerResponse.getSuccess())
                .isFalse();

        assertThat(customerResponse.getErrors().get(0).getErrorCode())
                .isEqualTo(101);

    }

    @Test
    public void tryCreateExistingCustomer(){

        PersonKey personKey = new PersonKey("D", "2070-65446-0116");

        Customer customer = new Customer();
        customer.setCustomerStatus("A");

        given(customerRepository.findById(personKey))
                .willReturn(Optional.of(customer));

        given(employeeRepository.existsByEmployeeCode(1))
                .willReturn(true);

        CreateCustomerRequest customerRequest = new CreateCustomerRequest();
        customerRequest.setDocumentType("D");
        customerRequest.setDocument("2070-65446-0116");
        customerRequest.setFunctionary(1);

        CustomerResponse customerResponse = customerService.createCustomer(
                customerRequest
        );

        assertThat(customerResponse.getSuccess())
                .isFalse();

        assertThat(customerResponse.getErrors().get(0).getErrorCode())
                .isEqualTo(102);

    }

}
