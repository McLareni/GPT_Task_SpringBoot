package com.example.demo.Controllers;

import com.example.demo.Model.Contact;
import com.example.demo.Repository.ContactRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class ContactControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private ObjectMapper objectMapper;

	private Contact savedContact;

	@BeforeEach
	void setup() {
		contactRepository.deleteAll(); // clean database before each test
		savedContact = contactRepository.save(new Contact("Test Name", "123456789", "test@example.com"));
	}

	@Test
	void testGetAllContacts() throws Exception {
		mockMvc.perform(get("/contacts").param("page", "0").param("size", "10")).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()", is(1))).andExpect(jsonPath("$[0].name", is("Test Name")));
	}

	@Test
	void testGetContactById() throws Exception {
		mockMvc.perform(get("/contacts/{id}", savedContact.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$.email", is("test@example.com")));
	}

	@Test
	void testCreateContact() throws Exception {
		Contact newContact = new Contact("New Name", "987654321", "new@example.com");

		mockMvc.perform(post("/contacts").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newContact))).andExpect(status().isCreated())
				.andExpect(jsonPath("$.name", is("New Name")));
	}

	@Test
	void testUpdateContact() throws Exception {
		savedContact.setName("Updated Name");

		mockMvc.perform(put("/contacts/{id}", savedContact.getId()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(savedContact))).andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is("Updated Name")));
	}

	@Test
	void testDeleteContact() throws Exception {
		mockMvc.perform(delete("/contacts/{id}", savedContact.getId())).andExpect(status().isOk())
				.andExpect(content().string("Contact deleted"));
	}

	@Test
	void testSearchContactByName() throws Exception {
		mockMvc.perform(get("/contacts/search").param("name", "Test")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name", containsString("Test")));
	}
}
