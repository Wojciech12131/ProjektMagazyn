package pl.edu.pk.mag.repository.entity;


import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class User extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 11322512L;

    @NotNull
    @Size(max = 50)
    private String username;

    @NotNull
    private String password;

    private boolean enabled;


    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Role> roles;

    @JoinColumn(name = "address_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.EAGER, targetEntity = Address.class, cascade = CascadeType.PERSIST)
    private Address address;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private Set<WarehouseGroup> warehouseGroups;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
