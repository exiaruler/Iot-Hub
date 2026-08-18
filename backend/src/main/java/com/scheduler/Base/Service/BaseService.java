package com.scheduler.Base.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scheduler.Base.Base;
@Transactional
public abstract class BaseService<T,ID> extends Base{
    protected abstract JpaRepository<T, ID> repository();
    // callback method to be overridden by subclasses to perform additional actions before saving an entity. This can be useful for validation, logging, or any other pre-processing that needs to occur before the entity is persisted to the database.
    protected void beforeSave(T entity,Map<String, String> errors,Map<String, String> warnings) {
        // Default implementation
    }
    // callback method to be overridden by subclasses to perform additional actions after saving an entity. This can be useful for logging, auditing, or any other post-processing that needs to occur after the entity is persisted to the database.
    protected void afterSave(T entity) {
        // Default implementation
    }

    public T save(T entity) {
        Map<String,String> errors=new HashMap<>();
        Map<String,String> warnings=new HashMap<>();
        beforeSave(entity,errors,warnings);

        T saved = repository().save(entity);

        afterSave(saved);

        return saved;
    }
    // callback method to be overridden by subclasses to perform additional actions before deleting an entity. This can be useful for validation, logging, or any other pre-processing that needs to occur before the deletion of an entity.
    protected void beforeDelete(ID id) {}
    // callback method to be overridden by subclasses to perform additional actions after deleting an entity. This can be useful for logging, auditing, or any other post-processing that needs to occur after the deletion of an entity.
    protected void afterDelete(ID id) {}

    public final void delete(ID id) {
        beforeDelete(id);
        
        repository().deleteById(id);

        afterDelete(id);
    }
    // callback method to be overridden by subclasses to perform additional actions before deleting an entity. This can be useful for validation, logging, or any other pre-processing that needs to occur before the deletion of an entity.
    protected void verifyDelete(ID id, boolean verify) {}

    public final void deleteVerify(ID id, boolean verify) {
        verifyDelete(id, verify);
        this.delete(id);
    }
    // callback method to be overridden by subclasses to perform additional actions after finding an entity by ID. This can be useful for logging, auditing, or any other post-processing that needs to occur after retrieving an entity from the database.
    protected void afterFindById(ID id, T entity) {}
    
    public T findById(ID id){
        Optional<T> obj=repository().findById(id);
        if(obj.isPresent()){
            afterFindById(id, obj.get());
            return obj.get();
        }
        return null;
    }
}
