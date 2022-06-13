package de.bredex.lending.infrastructure.inventory;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.bredex.lending.domain.spi.InventoryServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class InventoryServiceProviderImpl implements InventoryServiceProvider {


    private final RestTemplate restTemplate;
    @Value("/api/v1/inventory/")
    private String apiInventory;
    private final String serviceId = "inventory-service";

    private final Set<String> isbns = new HashSet<>();
    @Autowired
    private DiscoveryClient discoveryClient;


    public InventoryServiceProviderImpl(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean bookExists(String isbn) {
        Optional<URI> uri = resolverInventoryUrlForInventory();
        if (uri.isPresent()){
            try {
                final ResponseEntity<String> response = restTemplate
                        .getForEntity(uri.get() + apiInventory + isbn, String.class);
                return response.getStatusCode() == HttpStatus.OK;
            } catch (final RestClientException exception) {
            }
        }
        return isbns.contains(isbn);
    }


    @Scheduled(fixedDelay = 100)
    public void updateInventory() throws JsonProcessingException {
        Optional<URI> uri = resolverInventoryUrlForInventory();
        if (uri.isPresent()) {
            try {
                final ResponseEntity<BookDto[]> response = restTemplate.getForEntity(
                        uri.get() + "apiInventory",
                        BookDto[].class);
                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    isbns.clear();
                    for (BookDto book : response.getBody()) {
                        isbns.add(book.getIsbn());
                    }
                }
            }catch (ResourceAccessException e) {
                // Do nothing.
            }
        }
    }

    private Optional<URI> resolverInventoryUrlForInventory() {
        List<ServiceInstance> instances = discoveryClient.getInstances("inventory-service");
        if (!instances.isEmpty()) {
            return Optional.of(instances.get(0).getUri());
        } else {
            return Optional.empty();
        }
    }

    private boolean isaBooleanHttp200(ResponseEntity<String> response) {
        return response.getStatusCode() == HttpStatus.OK;
    }

}