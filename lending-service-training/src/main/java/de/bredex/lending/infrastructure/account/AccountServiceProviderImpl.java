package de.bredex.lending.infrastructure.account;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.bredex.lending.domain.spi.AccountServiceProvider;

@Component
public class AccountServiceProviderImpl implements AccountServiceProvider {

    @Value("http://localhost:8081/api/v1/account/")
    private String externalServiceBaseUri;


    private final RestTemplate restTemplate;

    public AccountServiceProviderImpl(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean accountExists(String accountNumber) {
        try {
            final ResponseEntity<String> response = restTemplate
                    .getForEntity(externalServiceBaseUri + accountNumber, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (final RestClientException exception) {
            return false;
        }
    }
}
