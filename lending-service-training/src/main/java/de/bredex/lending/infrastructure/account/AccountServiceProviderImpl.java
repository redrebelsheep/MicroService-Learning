package de.bredex.lending.infrastructure.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.bredex.lending.domain.spi.AccountServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashSet;
import java.util.Set;

@Component
public class AccountServiceProviderImpl implements AccountServiceProvider {

    private final String serviceId = "account-service";
    private final RestTemplate restTemplate;
    @Value("/api/v1/account")
    private String apiAddress;
    private final Set<String> accountNumberSet = new HashSet<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private DiscoveryClient discoveryClient;

    public AccountServiceProviderImpl(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedDelay = 10)
    public void getAccountNumberScheduled() throws JsonProcessingException {
        if (isInstanceOfDiscoveryClientNotEmpty()) {
            final ResponseEntity<String> response = getStringResponseEntity();
            if (isaBooleanHttp200(response)) {
                Set<AccountResponse> AccountResponseSet = objectMapper.readValue(response.getBody(),
                                                                                 objectMapper.getTypeFactory()
                                                                                         .constructCollectionType(
                                                                                                 Set.class,
                                                                                                 AccountResponse.class));
                AccountResponseSet.forEach(element -> accountNumberSet.add(element.getNumber()));
            }
        }
    }

    @Override
    public boolean accountExists(String accountNumber) {
        try {
            if (isInstanceOfDiscoveryClientNotEmpty()) {
                final URI accountService = discoveryClient.getInstances(serviceId).get(0).getUri();
                final ResponseEntity<String> response = getStringResponseEntity();
                return isaBooleanHttp200(response);
            }
            return hasAccountNumberSetAccountNumber(accountNumber);
        } catch (final RestClientException exception) {
            return hasAccountNumberSetAccountNumber(accountNumber);
        }
    }

    private boolean hasAccountNumberSetAccountNumber(String accountNumber) {
        return accountNumberSet.contains(accountNumber);
    }

    private boolean isaBooleanHttp200(ResponseEntity<String> response) {
        return response.getStatusCode() == HttpStatus.OK;
    }

    private boolean isInstanceOfDiscoveryClientNotEmpty() {
        return !discoveryClient.getInstances(serviceId).isEmpty();
    }

    private ResponseEntity<String> getStringResponseEntity() {
        final URI accountService = discoveryClient.getInstances(serviceId).get(0).getUri();
        return restTemplate.getForEntity(accountService + apiAddress, String.class);
    }
}
