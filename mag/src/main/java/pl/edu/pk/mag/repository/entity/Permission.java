package pl.edu.pk.mag.repository.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serial;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Permission extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1124235345L;

    @Column(unique = true, length = 100)
    private String name;
}
