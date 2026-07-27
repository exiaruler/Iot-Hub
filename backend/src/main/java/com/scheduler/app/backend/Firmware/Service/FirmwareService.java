package com.scheduler.app.backend.Firmware.Service;

import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.scheduler.Base.Exception.ValidationException;
import com.scheduler.Base.Service.BaseService;
import com.scheduler.app.backend.Firmware.Model.Firmware;

@Service
public class FirmwareService extends BaseService<Firmware, Long> {
    @Override
    protected JpaRepository<Firmware, Long> repository() {
        return null;
    }

    @Override
    protected void beforeSave(Firmware entity, Map<String, String> errors, Map<String, String> warnings) {
        if(!entity.validateVersion()){
            errors.put("version", "Invalid firmware version format");
        }
        if(errors.size()>0){
            throw new ValidationException(errors,null);
        }
        
    }

}
