package pl.edu.pk;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import pl.edu.pk.mag.repository.WarehouseRepository;

@Profile("test")
@Configuration
public class ConfigBean {

    @Bean
    @Primary
    public WarehouseRepository nameService() {
        return Mockito.mock(WarehouseRepository.class);
    }
}
