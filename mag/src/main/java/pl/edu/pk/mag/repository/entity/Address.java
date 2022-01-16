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
public class Address extends BaseEntity implements Cloneable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Address address = (Address) o;

        if (email != null ? !email.equals(address.email) : address.email != null) return false;
        if (mobile != null ? !mobile.equals(address.mobile) : address.mobile != null) return false;
        if (city != null ? !city.equals(address.city) : address.city != null) return false;
        return street != null ? street.equals(address.street) : address.street == null;
    }

    @Override
    public Object clone() {
        Address address;
        try {
            address = (Address) super.clone();
        } catch (Exception e) {
            address = new Address(email, mobile, city, street);
        }
        return address;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (mobile != null ? mobile.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (street != null ? street.hashCode() : 0);
        return result;
    }
}
