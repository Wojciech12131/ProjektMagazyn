package pl.edu.pk.mag.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@MappedSuperclass
@NoArgsConstructor
public class BaseEntity implements Serializable, Cloneable {
    @Serial
    private static final long serialVersionUID = 12315234L;

    @Version
    protected Long version;

    @CreationTimestamp
    protected LocalDateTime createdOn;

    @UpdateTimestamp
    protected LocalDateTime updatedOn;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BaseEntity that = (BaseEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public Object clone() {
        BaseEntity baseEntity;
        try {
            baseEntity = (BaseEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            baseEntity = new BaseEntity();
            baseEntity.setId(id);
            baseEntity.setCreatedOn(createdOn);
            baseEntity.setUpdatedOn(updatedOn);
            baseEntity.setVersion(version);
        }
        return baseEntity;
    }
}
