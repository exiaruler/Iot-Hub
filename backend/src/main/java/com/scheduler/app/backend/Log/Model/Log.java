package com.scheduler.app.backend.Log.Model;

import java.time.Instant;
import java.time.Period;

import com.scheduler.Base.ModelBase.ModelBaseUni;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import java.util.Objects;
// Log entity for storing log information
@Entity
@Table(indexes = @Index(name = "idx_log_entity_entity_id", columnList = "entity, entity_id"))
public class Log extends ModelBaseUni {
    // log type
    @Column
    private String type="update";
    // entity/table
    @Column
    private String entity;
    // id in that entity/table
    @Column(name = "entity_id")
    private String entityId;
    // log message
    @Column
    private String message;
    // output/response
    @Lob
    @Column(columnDefinition = "MEDIUMTEXT")
    private String output;
    // log existence life time
    @Column
    private Period lifeTime = Period.ofDays(1);
    // log expiry date and time
    @Column
    private Instant logExpiry;
    // log expiry enabled
    @Column
    private boolean nonExpiry = false;
    
    protected void prePersist() {
        super.prePersist();
        if (logExpiry == null && !nonExpiry) {
            logExpiry = Instant.now().plus(lifeTime);
        }
    }
    public void setEntity(Object entity) {
        this.entity = entity.getClass().getSimpleName().toLowerCase();
    }
    public Log() {
    }

    public Log(String type, String entity, String entityId, String message, String output, Period lifeTime, Instant logExpiry, boolean nonExpiry) {
        this.type = type;
        this.entity = entity;
        this.entityId = entityId;
        this.message = message;
        this.output = output;
        this.lifeTime = lifeTime;
        this.logExpiry = logExpiry;
        this.nonExpiry = nonExpiry;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEntity() {
        return this.entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getEntityId() {
        return this.entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOutput() {
        return this.output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Period getLifeTime() {
        return this.lifeTime;
    }

    public void setLifeTime(Period lifeTime) {
        this.lifeTime = lifeTime;
    }

    public Instant getLogExpiry() {
        return this.logExpiry;
    }

    public void setLogExpiry(Instant logExpiry) {
        this.logExpiry = logExpiry;
    }

    public boolean isNonExpiry() {
        return this.nonExpiry;
    }

    public boolean getNonExpiry() {
        return this.nonExpiry;
    }

    public void setNonExpiry(boolean nonExpiry) {
        this.nonExpiry = nonExpiry;
    }

    public Log type(String type) {
        setType(type);
        return this;
    }

    public Log entity(String entity) {
        setEntity(entity);
        return this;
    }

    public Log entityId(String entityId) {
        setEntityId(entityId);
        return this;
    }

    public Log message(String message) {
        setMessage(message);
        return this;
    }

    public Log output(String output) {
        setOutput(output);
        return this;
    }

    public Log lifeTime(Period lifeTime) {
        setLifeTime(lifeTime);
        return this;
    }

    public Log logExpiry(Instant logExpiry) {
        setLogExpiry(logExpiry);
        return this;
    }

    public Log nonExpiry(boolean nonExpiry) {
        setNonExpiry(nonExpiry);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Log)) {
            return false;
        }
        Log log = (Log) o;
        return Objects.equals(type, log.type) && Objects.equals(entity, log.entity) && Objects.equals(entityId, log.entityId) && Objects.equals(message, log.message) && Objects.equals(output, log.output) && Objects.equals(lifeTime, log.lifeTime) && Objects.equals(logExpiry, log.logExpiry) && nonExpiry == log.nonExpiry;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, entity, entityId, message, output, lifeTime, logExpiry, nonExpiry);
    }

    @Override
    public String toString() {
        return "{" +
            " type='" + getType() + "'" +
            ", entity='" + getEntity() + "'" +
            ", entityId='" + getEntityId() + "'" +
            ", message='" + getMessage() + "'" +
            ", output='" + getOutput() + "'" +
            ", lifeTime='" + getLifeTime() + "'" +
            ", logExpiry='" + getLogExpiry() + "'" +
            ", nonExpiry='" + isNonExpiry() + "'" +
            "}";
    }
    
}
