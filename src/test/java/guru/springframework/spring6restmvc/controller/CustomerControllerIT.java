package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.entities.Customer;
import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CustomerControllerIT {
    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testPatchCustomerNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.patchCustomerById(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }

    @Test
    void testPatchCustomerById() {
        Customer testCustomer = customerRepository.findAll().get(0);

        final String patchedName = "patched customer name";
        CustomerDTO testDto = CustomerDTO.builder()
                .customerName(patchedName)
                .build();

        ResponseEntity responseEntity = customerController.patchCustomerById(testCustomer.getId(), testDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Customer patchedCustomer = customerRepository.findById(testCustomer.getId()).orElse(null);
        assertThat(patchedCustomer).isNotNull();
        assertThat(patchedCustomer.getCustomerName()).isEqualTo(patchedName);
    }

    @Test
    void testDeleteNotFound() {
        assertThrows(NotFoundException.class, () -> {
           customerController.deleteById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteCustomerById() {
        // get customer from repo and delete it using the controller
        Customer testCustomer = customerRepository.findAll().get(0);
        ResponseEntity responseEntity = customerController.deleteById(testCustomer.getId());

        // assert that status code is no content, and make sure when u fetch the id the repo returns null
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(customerRepository.findById(testCustomer.getId())).isEmpty();
    }

    @Test
    void testUpdateCustomerNotFound() {
        assertThrows(NotFoundException.class, () -> {
           customerController.updateCustomerById(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testUpdateCustomerById() {
        // get customer from repo in order for us to obtain the id
        Customer testCustomer = customerRepository.findAll().get(0);
        // create a new customerDto entity to emulate the object passed to the controller
        final String UPDATED_NAME = "test update customer";
        CustomerDTO testDto = CustomerDTO.builder()
                .customerName(UPDATED_NAME)
                .build();
        // set id and version to null because they are handled by Hibernate
        testDto.setId(null);
//        testDto.setVersion(null);

        // obtain response entity and make sure that it returns the 204 status code
        ResponseEntity responseEntity = customerController.updateCustomerById(testCustomer.getId(), testDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        // get the customer that has been updated through the controller again and make sure that the name was updated
        Customer testUpdatedCustomer = customerRepository.findById(testCustomer.getId()).orElse(null);
        assertThat(testUpdatedCustomer).isNotNull();
        assertThat(testUpdatedCustomer.getCustomerName()).isEqualTo(UPDATED_NAME);
    }

    @Rollback
    @Transactional
    @Test
    void testSaveNewCustomer() {
        // create new customer to pass to controller
        CustomerDTO testDto = CustomerDTO.builder()
                .customerName("test new customer")
                .build();

        // call controller function to be tested
        ResponseEntity responseEntity = customerController.handlePost(testDto);

        // test that the returned response entity has the correct code
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        // assert that the response entity is not equal to null, so we don't get an error when we call @getPath() function
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        // get the location header from the response entity and from it, we can extract the UUID to make sure it has been saved successfully
        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(locationUUID[4]);
        // verify that the item has been saved successfully in the database
        Customer testCustomer = customerRepository.findById(savedUUID).orElse(null);
        assertThat(testCustomer).isNotNull();
    }

    @Test
    void testGetCustomerById() {
        Customer testCustomer = customerRepository.findAll().get(0);
        CustomerDTO dto = customerController.getCustomerById(testCustomer.getId());
        assertThat(dto).isNotNull();
    }

    @Test
    void testCustomerIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
           customerController.getCustomerById(UUID.randomUUID());
        });
    }

    @Test
    void testListCustomers() {
        List<CustomerDTO> testDtos = customerController.listCustomers();
        assertThat(testDtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        customerRepository.deleteAll();
        List<CustomerDTO> testDtos = customerController.listCustomers();
        assertThat(testDtos.size()).isEqualTo(0);
    }
}