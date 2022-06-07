package de.bredex.inventory.application.api;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class InventoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void GET_returns_inventory() throws Exception {
	mvc.perform(get("/api/v1/inventory").contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().is(HttpStatus.OK.value())).andExpect(jsonPath("$.*", hasSize(10)));
    }
    
    @Test
    public void GET_returns_single_book() throws Exception {
	mvc.perform(get("/api/v1/inventory/1-86092-049-7").contentType(MediaType.APPLICATION_JSON))
	.andExpect(status().is(HttpStatus.OK.value())).andExpect(jsonPath("$.isbn", is("1-86092-049-7")))
	.andExpect(jsonPath("$.genre", is("Modern Times")))
	.andExpect(jsonPath("$.title", is("The Grass is Always Greener")))
	.andExpect(jsonPath("$.author", is("Jeffrey Archer")));
    }
    
    @Test
    public void GET_returns_NOT_FOUND_for_non_existing_book() throws Exception {
	mvc.perform(get("/api/v1/inventory/1-00000-000-0").contentType(MediaType.APPLICATION_JSON))
	.andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }
}
