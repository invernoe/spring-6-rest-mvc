package guru.springframework.spring6restmvc.services;

import guru.springframework.spring6restmvc.model.CustomerDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final Map<UUID, CustomerDTO> customerMap;

    public CustomerServiceImpl() {
        this.customerMap = new HashMap<>();

        CustomerDTO customer1 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("mahmoud")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        CustomerDTO customer2 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("khaled")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        CustomerDTO customer3 = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .customerName("ahmed")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customerMap.put(customer1.getId(), customer1);
        customerMap.put(customer2.getId(), customer2);
        customerMap.put(customer3.getId(), customer3);
    }

    @Override
    public List<CustomerDTO> listCustomers() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.of(customerMap.get(id));
    }

    @Override
    public CustomerDTO saveNewCustomer(CustomerDTO customer) {
        CustomerDTO savedCustomer = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .version(1)
                .customerName(customer.getCustomerName())
                .build();

        customerMap.put(savedCustomer.getId(), savedCustomer);
        return savedCustomer;
    }

    @Override
    public void updateCustomerById(UUID id, CustomerDTO customer) {
        CustomerDTO existing = customerMap.get(id);
        existing.setCustomerName(customer.getCustomerName());
        existing.setVersion(existing.getVersion() + 1);
        existing.setLastModifiedDate(LocalDateTime.now());

        customerMap.put(existing.getId(), existing);
    }

    @Override
    public void deleteById(UUID id) {
        customerMap.remove(id);
    }

    @Override
    public void patchCustomerById(UUID id, CustomerDTO customer) {
        CustomerDTO existing = customerMap.get(id);

        if(StringUtils.hasText(customer.getCustomerName()))
            existing.setCustomerName(customer.getCustomerName());

        existing.setVersion(existing.getVersion() + 1);
        existing.setLastModifiedDate(LocalDateTime.now());

        customerMap.put(existing.getId(), existing);
    }
}
