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
public class WPermission extends BaseEntity {

    private String code;
}
