package com.parking;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public record CustomerService(CustomerRepository customerRepository, FraudClient fraudClient, NotificationClient notificationClient) {

    public void registerCustomer(CustomerRegistrationRequest customerRegistrationRequest) {
        Customer customer = Customer.builder().firstName(customerRegistrationRequest.firstName())
                .lastName(customerRegistrationRequest.lastName())
                .email(customerRegistrationRequest.email())
                .build();
        customerRepository.save(customer); // Save customers in our DB

        FraudCheckResponse response = fraudClient.checkFraud(customer.getId());
        NotificationMessage res = notificationClient.sendNotifs(customer.getId());

        // 3. Check result
        if (response.isFraudster()) {
            throw new IllegalStateException("Customer est fraudster");
        }
    }
    public Customer getCustomerById(int id){
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("not found !"));
        return  customer;
    }
    public void deleteCustomerById(int id){
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("not found !"));
        customerRepository.deleteById(customer.getId());

    }
    public void modifyCustomerById(int id, CustomerRegistrationRequest customerRegistrationRequest){
        Customer customer = customerRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("customer n'existe pas frere"));
        if (customerRegistrationRequest.email() != null
                && !customerRegistrationRequest.email().isEmpty()
                && !customer.getEmail().equals(customerRegistrationRequest.email())) {
            customer.setEmail(customerRegistrationRequest.email());
        }
        if (customerRegistrationRequest.firstName() != null
                && !customerRegistrationRequest.firstName().isEmpty()
                && !customer.getFirstName().equals(customerRegistrationRequest.firstName())) {
            customer.setFirstName(customerRegistrationRequest.firstName());
        }
        if (customerRegistrationRequest.lastName() != null
                && !customerRegistrationRequest.lastName().isEmpty()
                && !customer.getLastName().equals(customerRegistrationRequest.lastName())) {
            customer.setLastName(customerRegistrationRequest.lastName());
        }
        customerRepository.save(customer); // Save customers in our DB
    }
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // search customers
    public  List<Customer> searchCustomerByEmailS(String email){
        return customerRepository.getAllByEmail(email);
    }


}
