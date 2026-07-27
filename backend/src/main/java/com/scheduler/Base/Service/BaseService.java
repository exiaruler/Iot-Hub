package com.scheduler.Base.Service;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.scheduler.Base.Base;
@Transactional
public abstract class BaseService<T,ID> extends Base{
    protected abstract JpaRepository<T, ID> repository();

    protected void beforeSave(T entity,Map<String, String> errors,Map<String, String> warnings) {
        // Default implementation
    }

    protected void afterSave(T entity) {
        // Default implementation
    }

    public T save(T entity) {
        
        beforeSave(entity,new HashMap<>(),new HashMap<>());

        T saved = repository().save(entity);

        afterSave(saved);

        return saved;
    }
    protected void beforeDelete(ID id) {}
    protected void afterDelete(ID id) {}

    public final void delete(ID id) {
        beforeDelete(id);

        repository().deleteById(id);

        afterDelete(id);
    }
}
