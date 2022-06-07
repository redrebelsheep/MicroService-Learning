package de.bredex.lending.infrastructure.account;

import de.bredex.lending.LendingApplication8082;
import de.bredex.lending.application.api.config.RestTemplateConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = RestTemplateConfig.class)
@SpringBootTest(classes = LendingApplication8082.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AccountServiceProviderImplTest {

    @LocalServerPort
    Integer port;

    @Autowired
    RestTemplate testRestTemplate;

    @Autowired
    AccountServiceProviderImpl providerImpl;



}