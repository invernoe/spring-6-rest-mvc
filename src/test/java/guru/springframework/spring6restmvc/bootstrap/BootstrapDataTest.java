package guru.springframework.spring6restmvc.bootstrap;

import guru.springframework.spring6restmvc.repositories.BeerRepository;
import guru.springframework.spring6restmvc.repositories.CustomerRepository;
import guru.springframework.spring6restmvc.services.BeerCsvService;
import guru.springframework.spring6restmvc.services.BeerCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import(BeerCsvServiceImpl.class)
class BootstrapDataTest {
    @Autowired
    BeerRepository beerRepository;

    @Autowired
    CustomerRepository customerRepository;

    // Cannot be auto wired normally because this is not loaded in the spring context as this test file is annotated as a data jpa test
    // Therefore it only loads a splice of the context so we have to manually import the impl of the class
    @Autowired
    BeerCsvService beerCsvService;

    BootstrapData bootStrapData;

    @BeforeEach
    void setUp(){
        bootStrapData = new BootstrapData(beerRepository, customerRepository, beerCsvService);
    }

    @Test
    void testRun() throws Exception {
        bootStrapData.run();
        assertThat(beerRepository.count()).isEqualTo(2413);
        assertThat(customerRepository.count()).isEqualTo(3);
    }
}