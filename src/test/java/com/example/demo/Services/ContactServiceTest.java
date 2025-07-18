package com.example.demo.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.demo.Model.Contact;
import com.example.demo.Repository.ContactRepository;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {
	@Mock
	private ContactRepository contactRepository;

	@InjectMocks
	private ContactService contactService;

	private Contact contact;

	@BeforeEach
	void setup() {
		contact = new Contact("John Doe", "123456789", "john@example.com");
		contact.setId(1L);
	}

	@Test
	void testGetAllContacts() {
		int page = 0;
		int size = 10;
		Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

		List<Contact> contacts = List.of(contact);
		Page<Contact> contactPage = new PageImpl<>(contacts, pageable, contacts.size());

		when(contactRepository.findAll(pageable)).thenReturn(contactPage);

		Page<Contact> result = contactService.getAllContact(page, size);

		assertEquals(1, result.getTotalElements());
		assertEquals("John Doe", result.getContent().get(0).getName());
	}

	@Test
	void testGetContactById_found() {
		when(contactRepository.findById(1L)).thenReturn(Optional.of(contact));

		Contact result = contactService.getContactById(1L);

		assertEquals("john@example.com", result.getEmail());
	}

	@Test
	void testGetContactById_notFound() {
		when(contactRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(RuntimeException.class, () -> contactService.getContactById(1L));
	}

	@Test
	void testCreateContact() {
		when(contactRepository.save(contact)).thenReturn(contact);

		Contact result = contactService.createContact(contact);

		assertNotNull(result);
		assertEquals("John Doe", result.getName());
	}

	@Test
	void testSearchContacts() {
		int page = 0;
		int size = 10;
		Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

		List<Contact> contacts = List.of(contact);
		Page<Contact> contactPage = new PageImpl<>(contacts, pageable, contacts.size());

		when(contactRepository.findByNameContainingIgnoreCase("john", pageable)).thenReturn(contactPage);

		Page<Contact> result = contactService.findContactsByNameContainingIgnoreCase("john", page, size);

		assertEquals(1, result.getTotalElements());
		assertEquals("John Doe", result.getContent().get(0).getName());
	}
}
