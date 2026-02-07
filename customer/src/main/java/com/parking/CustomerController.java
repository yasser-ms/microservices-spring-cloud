package com.parking;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/customers")
public record CustomerController(CustomerService customerService) {
    /* POST Request*/
    @PostMapping
    public void registerCustomer(@Valid  @RequestBody CustomerRegistrationRequest customerRegistrationRequest){
        log.info("New customer registration {}", customerRegistrationRequest);
        customerService.registerCustomer(customerRegistrationRequest);
    }

    /* GET Request */
    @GetMapping
    public List<Customer> getAllCustomers() {
        log.info("FEtching all customers");
        return customerService.getAllCustomers();
    }
    /*GET customer by ID */
    @GetMapping("/{id}")
    public Customer getCUstomerById(@PathVariable("id") int id){
        log.info("Fetch customer by id");
        return customerService.getCustomerById(id);
    }
    /*DELETE customer by id*/
    @DeleteMapping("/{id}")
    public void deleteCustomerById(@PathVariable("id") int id){
        log.info("delete the user");
        customerService.deleteCustomerById(id);
    }
    /*MODIFY A CUSTOMER*/
    @PutMapping("/{id}")
    public void modifyCustomerById(@PathVariable("id") int id, @RequestBody CustomerRegistrationRequest customerRegistrationRequest){
        log.info("modify information of a customer {}", customerRegistrationRequest);
        customerService.modifyCustomerById(id, customerRegistrationRequest);

    }

    /* Search customer by email*/
     @GetMapping("/search")
    public List<Customer> searchCustomerByEmail(@RequestParam("email") String email){
         log.info("Search information of a customer ");
        return customerService.searchCustomerByEmailS(email);
     }

}
