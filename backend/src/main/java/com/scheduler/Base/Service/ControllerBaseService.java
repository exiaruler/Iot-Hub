package com.scheduler.Base.Service;

import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.scheduler.Base.ControllerBase;
// use in align with BaseService to create a generic controller for all entities that extends BaseService. This will allow for a more streamlined and consistent approach to handling CRUD operations across different entities in the application.
public class ControllerBaseService<ID,T> extends ControllerBase {
    @Autowired
    BaseService<T,ID> service;
    public boolean disableAdd=false;
    public boolean disableUpdate=false;
    public boolean disableDelete=false;
    
    @Override
    @GetMapping("/new-record")
    @SuppressWarnings("unchecked")
    public T getNewRecord() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        T obj = (T) super.getNewRecord();
        return obj;
    }

    @PostMapping(value="/add-record")
    public ResponseEntity<T> addRecord(@RequestBody T entity){
        if (disableAdd) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(service.save(entity));
    }
    @PutMapping(value="/update-record/{id}")
    public ResponseEntity<T> updateRecord(@PathVariable ID id,@RequestBody T entity){
        if (disableUpdate) {
            return ResponseEntity.status(403).build();
        }
        T existing=service.findById(id);
        if(existing==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(service.save(entity));
    }
    @DeleteMapping(value = "/delete-record/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable ID id){
        if (disableDelete) {
            return ResponseEntity.status(403).build();
        }
        T existing=service.findById(id);
        if(existing==null){
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.ok(null);
    }
    @DeleteMapping(value = "/delete-record-verify/{id}/{verify}")
    public ResponseEntity<Void> deleteRecordVerify(@PathVariable ID id, @PathVariable boolean verify){
        if (disableDelete) {
            return ResponseEntity.status(403).build();
        }
        T existing=service.findById(id);
        if(existing==null){
            return ResponseEntity.notFound().build();
        }
        service.deleteVerify(id, verify);
        return ResponseEntity.ok(null);
    }
    @GetMapping(value="/get-record/{id}")
    public ResponseEntity<T> getRecord(@PathVariable ID id) {
        T entity=service.findById(id);
        if(entity==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity);
    }

}
