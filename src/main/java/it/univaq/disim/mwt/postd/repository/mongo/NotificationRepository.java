package it.univaq.disim.mwt.postd.repository.mongo;

import it.univaq.disim.mwt.postd.domain.NotificationClass;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends CrudRepository<NotificationClass, String> {
    Optional<List<NotificationClass>> findByUserTargetId(Long userId);
}
