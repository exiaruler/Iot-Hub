package com.scheduler.app.backend.Log.Service;

import java.time.Period;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.scheduler.Base.Service.BaseService;
import com.scheduler.app.backend.Log.Model.Log;
import com.scheduler.app.backend.Log.Repo.LogRepo;
import jakarta.transaction.Transactional;

@Service 
public class LogService extends BaseService<Log, UUID> {
    private final LogRepo logRepo;

    public LogService(LogRepo logRepo) {
        this.logRepo = logRepo;
    }

    @Override
    protected JpaRepository<Log, UUID> repository() {
        return logRepo;
    }
    // get logs by entity and entityId
    public List<Log> getLogsByEntityAndEntityId(String entity, String entityId) {
        return logRepo.findAllByEntityAndEntityId(entity, entityId);
    }
    // delete logs by entity and id within that entity
    @Transactional
    public int deleteLogsByEntityAndEntityId(String entity, String entityId) {
        return logRepo.deleteByEntityAndEntityId(entity.toLowerCase(), entityId);
    }
    // delete expired logs
    @Transactional
    public int deleteExpiredLogs() {
        return logRepo.deleteExpiredLogs();
    }
  
    public Log addDataChangeLog(Object entity, String entityId, String message) {
        return addDataChangeLog(entity, entityId, message, null);
    }

    public Log addDataChangeLog(Object entity, String entityId, String message, Period lifeTime) {
        Log log = new Log();
        log.setType("update");
        log.setEntity(entity);
        log.setEntityId(entityId);
        log.setMessage(message);
        if (lifeTime != null) {
            log.setLifeTime(lifeTime);
        }
        return logRepo.save(log);
    }
    
    public Log addResponseLog(Object entity, String entityId, String message, String output) {
        return addResponseLog(entity, entityId, message, output, null);
    }

    public Log addResponseLogNoExpiry(Object entity, String entityId, String message, String output) {
        Log log = new Log();
        log.setType("response");
        log.setEntity(entity);
        log.setEntityId(entityId);
        log.setMessage(message);
        log.setOutput(output);
        log.setNonExpiry(true);
        return logRepo.save(log);
    }

    public Log addResponseLog(Object entity, String entityId, String message, String output, Period lifeTime) {
        Log log = new Log();
        log.setType("response");
        log.setEntity(entity);
        log.setEntityId(entityId);
        log.setMessage(message);
        log.setOutput(output);
        if (lifeTime != null) {
            log.setLifeTime(lifeTime);
        }
        return logRepo.save(log);
    }



}
