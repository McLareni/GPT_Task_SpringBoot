package com.example.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.Exceptions.ResourceNotFoundException;
import com.example.demo.Model.Contact;
import com.example.demo.Repository.ContactRepository;

@Service
public class ContactService {
	@Autowired
	private ContactRepository contactRepository;

	public Page<Contact> getAllContact(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
		return contactRepository.findAll(pageable);
	}

	public Contact createContact(Contact contact) {
		return contactRepository.save(contact);
	}

	public Contact getContactById(Long id) {
		return contactRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Not found contact ID:" + id));
	}

	public void deleteContactById(Long id) {
		contactRepository.deleteById(id);
	}

	public Page<Contact> findContactsByNameContainingIgnoreCase(String name, int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
		return contactRepository.findByNameContainingIgnoreCase(name, pageable);
	}
}
