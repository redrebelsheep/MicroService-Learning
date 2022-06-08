package de.bredex.lending.infrastructure.inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.bredex.lending.domain.spi.InventoryServiceProvider;

import java.net.URI;

@Component
public class InventoryServiceProviderImpl implements InventoryServiceProvider {

    @Value("/api/v1/inventory/")
    private String apiInventory;

    @Autowired
    private DiscoveryClient discoveryClient;

    private final RestTemplate restTemplate;

    public InventoryServiceProviderImpl(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean bookExists(String isbn) {

        try {
            final URI inventoryService = discoveryClient.getInstances("inventory-service").get(0).getUri();
            final ResponseEntity<String> response = restTemplate
                    .getForEntity(inventoryService+ apiInventory + isbn, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (final RestClientException exception) {
            return false;
        }
    }
}