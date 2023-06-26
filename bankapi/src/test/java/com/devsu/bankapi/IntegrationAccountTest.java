package com.devsu.bankapi;

import com.devsu.bankapi.controller.AccountController;
import com.devsu.bankapi.entities.Account;
import com.devsu.bankapi.service.AccountService;
import com.devsu.bankapi.utils.dto.response.AccountResponse;
import com.devsu.bankapi.utils.generics.CommonError;
import com.devsu.bankapi.utils.generics.ErrorList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static com.devsu.bankapi.utils.functions.CommonErrors.entityNotFound;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebMvcTest(AccountController.class)
public class IntegrationAccountTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountService service;

    @Test
    public void givenAccountByIdentifierSuccessTrueAndFalse()
            throws Exception {

        Account account = new Account(
                1,
                1,
                1,
                "001-0000001-1",
                new BigDecimal(100),
                new BigDecimal(100),
                "A",
                LocalDateTime.now(),
                1,
                new BigDecimal(1000),
                "DOL",
                1L,
                1,
                LocalDateTime.now()
        );

        given(service.getByIdentifier("001-0000001-1"))
                .willReturn((new ModelMapper()).map(account, AccountResponse.class));

        ErrorList errors = new ErrorList();

        errors.add(entityNotFound("cuenta"));

        given(service.getByIdentifier("001-0000001-2"))
                .willReturn(new AccountResponse(errors));

        mvc.perform(get("/api/account/byAccountIdentifier?identifier=001-0000001-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.formatedIdentifier", is(account.getFormatedIdentifier())))
                .andExpect(jsonPath("$.accountStatus", is("A")));

        mvc.perform(get("/api/account/byAccountIdentifier?identifier=001-0000001-2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].errorCode", is(104)));

    }
}
