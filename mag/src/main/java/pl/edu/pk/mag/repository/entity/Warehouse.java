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
public class Warehouse extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 11322512L;

    @Column(unique = true)
    private String code;


}
