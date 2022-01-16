package pl.edu.pk.mag.repository.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class AuditModification extends BaseEntity {
    private String entityName;

    private Long objectId;

    private boolean removed;

    private boolean modification;

    private Long UserId;

    @Column(length = 3000)
    private String changes;

}
