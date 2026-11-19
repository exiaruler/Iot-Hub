package com.scheduler.app.backend.Log.Repo;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.scheduler.app.backend.Log.Model.Log;

public interface LogRepo extends JpaRepository<Log,UUID> {
    @Query(value = "SELECT * FROM log WHERE entity =?1 AND entity_id = ?2 order by created_date desc", nativeQuery = true)
    List<Log> findAllByEntityAndEntityId(String entity,String entityId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM log WHERE entity = ?1 AND entity_id = ?2", nativeQuery = true)
    int deleteByEntityAndEntityId(String entity, String entityId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM log WHERE log_expiry IS NOT NULL AND log_expiry <= now() AND non_expiry = false", nativeQuery = true)
    int deleteExpiredLogs();

}
