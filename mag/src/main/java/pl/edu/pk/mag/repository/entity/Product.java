package pl.edu.pk.mag.repository.entity;

import lombok.*;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class Product extends BaseEntity {

    private String code;

    private String description;

}
