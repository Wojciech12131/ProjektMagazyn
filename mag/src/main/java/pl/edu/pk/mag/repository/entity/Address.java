package pl.edu.pk.mag.repository.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
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

    @OneToOne(orphanRemoval = true)
    @JoinTable(name = "address_address_id",
            joinColumns = @JoinColumn(name = "address_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id_id"))
    private User addressId;

    public Address(String email, String mobile) {
        this.email = email;
        this.mobile = mobile;
    }
}
