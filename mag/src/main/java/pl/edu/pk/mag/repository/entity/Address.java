package pl.edu.pk.mag.repository.entity;

import lombok.*;

import javax.persistence.Entity;
import java.io.Serial;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Address extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 11322512L;

    private String email;
    private String mobile;
    private String city;
    private String street;
    public Address(String email, String mobile) {
        this.email = email;
        this.mobile = mobile;
    }
}
