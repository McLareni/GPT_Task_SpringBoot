package com.example.demo.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Model.Contact;
import com.example.demo.Model.ContactDTO;
import com.example.demo.Services.ContactService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/contacts")
public class ContactController {
	@Autowired
	private ContactService contactService;

	@GetMapping
	public List<Contact> getAll(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return contactService.getAllContact(page, size).getContent();
	}

	@PostMapping
	public ResponseEntity<Contact> create(@Valid @RequestBody ContactDTO contact) {
		String bodyName = contact.getName();
		String bodyPhone = contact.getPhone();
		String bodyEmail = contact.getEmail();

		Contact newContact = new Contact(bodyName, bodyPhone, bodyEmail);

		Contact savedContact = contactService.createContact(newContact);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedContact);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Contact> getUserById(@PathVariable Long id) {
		Contact contact = contactService.getContactById(id);

		return ResponseEntity.ok(contact);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Contact> updateContact(@PathVariable Long id, @Valid @RequestBody ContactDTO contact) {
		Contact oldContact = contactService.getContactById(id);

		oldContact.setEmail(contact.getEmail());
		oldContact.setName(contact.getName());
		oldContact.setPhone(contact.getPhone());

		Contact updatedContact = contactService.createContact(oldContact);
		return ResponseEntity.ok(updatedContact);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		contactService.getContactById(id);
		contactService.deleteContactById(id);

		return ResponseEntity.ok("Contact deleted");
	}

	@GetMapping("/search")
	public List<Contact> getContactByName(@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
		return contactService.findContactsByNameContainingIgnoreCase(name, page, size).getContent();
	}
}
