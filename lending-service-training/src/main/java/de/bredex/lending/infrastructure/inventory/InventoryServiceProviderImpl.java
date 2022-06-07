package de.bredex.lending.infrastructure.inventory;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.bredex.lending.domain.spi.InventoryServiceProvider;

@Component
public class InventoryServiceProviderImpl implements InventoryServiceProvider {

    @Value("http://localhost:8083/api/v1/inventory/")
    private String externalServiceBaseUri;
    private final RestTemplate restTemplate;

    public InventoryServiceProviderImpl(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean bookExists(String isbn) {

        try {
            final ResponseEntity<String> response = restTemplate
                    .getForEntity(externalServiceBaseUri + isbn, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (final RestClientException exception) {
            return false;
        }
    }
}