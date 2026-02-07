package com.parking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    List<Customer> getAllByFirstNameIn(Collection<String> firstNames);
    List<Customer> getAllByEmail(String email);
}
