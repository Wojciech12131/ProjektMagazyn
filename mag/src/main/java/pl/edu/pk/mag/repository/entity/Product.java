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
public class Product extends BaseEntity {
    @Column(length = 100)
    private String code;
    @Column(length = 1000)
    private String description;
}
