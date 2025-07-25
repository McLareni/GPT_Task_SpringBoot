package com.example.demo.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Model.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {
	Page<Contact> findAll(Pageable pageable);

	Page<Contact> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
