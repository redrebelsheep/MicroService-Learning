package de.bredex.lending.infrastructure.account;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bredex.lending.domain.model.AccountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.bredex.lending.domain.spi.AccountServiceProvider;

@Component
public class AccountServiceProviderImpl implements AccountServiceProvider {

    @Value("/api/v1/account")
    private String apiAdress;

    private Set<String> accountNumberSet = new HashSet<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(fixedDelay = 10)
    public void getAccountNumberScheduled() throws JsonProcessingException {
            if(isInstanceOfDiscoveryClientNotEmpty()) {
                final ResponseEntity<String> response = getStringResponseEntity();
                if(isaBooleanHttp200(response)){
                    Set<AccountResponse> AccountResponseSet = objectMapper.readValue(response.getBody(), objectMapper.getTypeFactory().constructCollectionType(Set.class, AccountResponse.class));
                    AccountResponseSet.forEach(element -> accountNumberSet.add(element.getNumber()));
                }
            }
    }


    @Autowired
    private DiscoveryClient discoveryClient;

    private final RestTemplate restTemplate;

    public AccountServiceProviderImpl(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean accountExists(String accountNumber) {
        try {
            if(isInstanceOfDiscoveryClientNotEmpty()){
                final URI accountService = discoveryClient.getInstances("account-service").get(0).getUri();
                final ResponseEntity<String> response = getStringResponseEntity();
                return isaBooleanHttp200(response);
            }
                return hasAccountNumberSetAccountNumber(accountNumber);
        } catch (final RestClientException exception) {
            return  hasAccountNumberSetAccountNumber(accountNumber);
        }
    }

    private boolean hasAccountNumberSetAccountNumber(String accountNumber) {
        return accountNumberSet.contains(accountNumber);
    }

    private boolean isaBooleanHttp200(ResponseEntity<String> response) {
        return response.getStatusCode() == HttpStatus.OK;
    }

    private boolean isInstanceOfDiscoveryClientNotEmpty() {
        return !discoveryClient.getInstances("account-service").isEmpty();
    }

    private ResponseEntity<String> getStringResponseEntity() {
        final URI accountService = discoveryClient.getInstances("account-service").get(0).getUri();
        final ResponseEntity<String> response = restTemplate
                .getForEntity(accountService + apiAdress, String.class);
        return response;
    }
}
