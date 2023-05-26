package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.CustomerDTO;
import guru.springframework.spring6restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
public class CustomerController {
    public static final String CUSTOMER_PATH = "/api/v1/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";
    private final CustomerService customerService;

    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity patchCustomerById(@PathVariable("customerId") UUID id, @RequestBody CustomerDTO customer){
        customerService.patchCustomerById(id, customer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    public ResponseEntity deleteById(@PathVariable("customerId") UUID id){
        customerService.deleteById(id);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity updateCustomerById(@PathVariable("customerId") UUID id, @RequestBody CustomerDTO customer){
        customerService.updateCustomerById(id, customer);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(CUSTOMER_PATH)
    public ResponseEntity handlePost(@RequestBody CustomerDTO customer){
        CustomerDTO savedCustomer = customerService.saveNewCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "api/v1/customer/" + savedCustomer.getId());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping(CUSTOMER_PATH)
    public List<CustomerDTO> listCustomers(){
        return customerService.listCustomers();
    }

    @GetMapping(CUSTOMER_PATH_ID)
    public CustomerDTO getCustomerById(@PathVariable("customerId") UUID id){
        return customerService.getCustomerById(id).orElseThrow(NotFoundException::new);
    }
}
