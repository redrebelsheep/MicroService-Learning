package de.bredex.lending.application.api;//package de.bredex.lending.application.api;
//

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LendingControllerTest {

    @RegisterExtension
    static WireMockExtension wiremock = WireMockExtension.newInstance()
            .options((WireMockConfiguration.wireMockConfig().port(8080))).build();
    @LocalServerPort
    Integer port;

    @Autowired
    private MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		wiremock.stubFor(WireMock.get(WireMock.urlMatching("/api/v1/account/.*")).willReturn(WireMock.ok()));
		wiremock.stubFor(WireMock.get(WireMock.urlMatching("/api/v1/iventory/.*")).willReturn(WireMock.ok()));
	}

	@Test
    public void POST_createLending_creates_new_lending() throws Exception {
        createLending("10001", "1-86092-049-7").andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.accountNumber", is("10001"))).andExpect(jsonPath("$.isbn", is("1-86092-038-1")));

        deleteLending("10001", "1-86092-049-7");
    }

    @Test
    public void GET_returns_all_lendings_of_account() throws Exception {
        createLending("10001", "1-86092-049-7");
        createLending("10001", "1-86092-029-5");

        mvc.perform(get("/api/v1/lending").queryParam("accountNumber", "10001"))
                .andExpect(status().is(HttpStatus.OK.value())).andExpect(jsonPath("$.accountNumber", is("10001")))
                .andExpect(jsonPath("$.items", hasSize(2)));

        deleteLending("10001", "1-86092-049-7");
        deleteLending("10001", "1-86092-029-5");
    }

    @Test
    public void DELETE_deletes_existing_lending() throws Exception {
        createLending("10001", "1-86092-049-7");

        deleteLending("10001", "1-86092-049-7").andExpect(status().is(HttpStatus.OK.value()));

        mvc.perform(get("/api/v1/lending").queryParam("accountNumber", "10001"))
                .andExpect(status().is(HttpStatus.OK.value())).andExpect(jsonPath("$.accountNumber", is("10001")))
                .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    public void DELETE_returns_400_on_non_existing_lending() throws Exception {
        deleteLending("10001", "1-86092-049-7").andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    private ResultActions createLending(final String accountNumber, final String isbn) throws Exception {
        final LendingRequest request = new LendingRequest(accountNumber, isbn);
        byte[] input = mapper.writeValueAsBytes(request);

        return mvc.perform(post("/api/v1/lending").contentType(MediaType.APPLICATION_JSON)
                                   .accept(MediaType.APPLICATION_JSON).content(input));
    }

    private ResultActions deleteLending(final String accountNumber, final String isbn) throws Exception {
        final LendingRequest request = new LendingRequest(accountNumber, isbn);
        byte[] input = mapper.writeValueAsBytes(request);

        return mvc.perform(delete("/api/v1/lending").contentType(MediaType.APPLICATION_JSON)
                                   .accept(MediaType.APPLICATION_JSON).content(input));
    }
}
