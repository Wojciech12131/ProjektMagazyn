package pl.edu.pk.mag.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditRecord {

    private String oldObject;

    private String newObject;

    private String fieldName;
}
