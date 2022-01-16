package pl.edu.pk.mag.service.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.pk.mag.repository.AuditModificationRepository;
import pl.edu.pk.mag.repository.UserRepository;
import pl.edu.pk.mag.repository.dto.AuditRecord;
import pl.edu.pk.mag.repository.entity.AuditModification;
import pl.edu.pk.mag.repository.entity.BaseEntity;
import pl.edu.pk.mag.repository.entity.StorageLocation;
import pl.edu.pk.mag.repository.entity.User;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuditModificationService {

    @Autowired
    private AuditModificationRepository auditModificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;


    public void beforeRemoveStorageLocation(StorageLocation storageLocation) {
        AuditModification auditModification = setRemoveObject(storageLocation);
        List<AuditRecord> auditRecordList = auditRemove(storageLocation);
        try {
            auditModification.setChanges(objectMapper.writeValueAsString(auditRecordList));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        auditModificationRepository.save(auditModification);
    }

    @SneakyThrows
    private List<AuditRecord> auditRemove(Object removedObject) {
        List<AuditRecord> auditRecordList = new ArrayList<>();
        for (Field field : removedObject.getClass().getDeclaredFields()
        ) {
            field.setAccessible(true);
            if (field.get(removedObject) != null) {
                auditRecordList.add(AuditRecord.builder().oldObject(String.valueOf(field.get(removedObject))).newObject(null).fieldName(field.getName()).build());
            }
        }
        return auditRecordList;
    }

    private <T extends BaseEntity> AuditModification setRemoveObject(T object) {
        AuditModification auditModification = new AuditModification();
        auditModification.setRemoved(true);
        auditModification.setModification(false);
        auditModification.setEntityName(object.getClass().getSimpleName());
        auditModification.setObjectId(object.getId());
        Optional<User> user = userRepository
                .getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        user.ifPresent(value -> auditModification.setUserId(value.getId()));
        return auditModification;
    }

    public <T extends BaseEntity> void beforeModification(T oldObject, T newObject) {
        AuditModification auditMod = setModification(oldObject);
        List<AuditRecord> auditRecordList = auditModification(oldObject, newObject);
        if (auditRecordList.size() != 0) {
            try {
                auditMod.setChanges(objectMapper.writeValueAsString(auditRecordList));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            auditModificationRepository.save(auditMod);
        }
    }

    private <T extends BaseEntity> List<AuditRecord> auditModification(T oldObject, T newObject) {
        List<AuditRecord> auditRecordList = new ArrayList<>();
        for (Field field : oldObject.getClass().getDeclaredFields()
        ) {
            field.setAccessible(true);
            try {
                if (!field.get(oldObject).equals(field.get(newObject))) {
                    auditRecordList.add(AuditRecord.builder()
                            .fieldName(field.getName())
                            .oldObject(String.valueOf(field.get(oldObject)))
                            .newObject(String.valueOf(field.get(newObject)))
                            .build());
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return auditRecordList;
    }

    private <T extends BaseEntity> AuditModification setModification(T oldObject) {
        AuditModification auditModification = new AuditModification();
        auditModification.setRemoved(false);
        auditModification.setModification(true);
        auditModification.setEntityName(oldObject.getClass().getSimpleName());
        auditModification.setObjectId(oldObject.getId());
        Optional<User> user = userRepository
                .getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        user.ifPresent(value -> auditModification.setUserId(value.getId()));
        SecurityContextHolder.getContext().getAuthentication().getName();
        return auditModification;
    }
}
