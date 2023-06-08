package guru.springframework.spring6restmvc.repositories;

import guru.springframework.spring6restmvc.entities.Beer;
import guru.springframework.spring6restmvc.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeerNameTooLong() {
        assertThrows(ConstraintViolationException.class, () -> {
            Beer beerSaved = beerRepository.save(Beer.builder()
                    .beerName("new beer 42174982147892174 21478129478219442174982147892174 214781294782194")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("123132213")
                    .price(new BigDecimal("12.44"))
                    .build());

            beerRepository.flush();
        });
    }

    @Test
    void testSaveBeer() {
        Beer beerSaved = beerRepository.save(Beer.builder()
                .beerName("new beer")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("123132213")
                .price(new BigDecimal("12.44"))
                .build());

        // flush so this gets persisted to the database and the validation errors appear.
        // if we don't flush the save function doesn't persist to the database instantly (it sort of does a lazy save, hibernate flushes in batches)
        beerRepository.flush();

        assertThat(beerSaved.getId()).isNotNull();
        assertThat(beerSaved.getBeerName()).isNotNull();
    }

}