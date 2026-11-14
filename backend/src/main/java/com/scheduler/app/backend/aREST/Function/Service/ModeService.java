package com.scheduler.app.backend.aREST.Function.Service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.scheduler.Base.Service.BaseService;
import com.scheduler.app.backend.aREST.Function.Models.Mode;
import com.scheduler.app.backend.aREST.Function.Repo.ModeRepo;

@Service
public class ModeService extends BaseService<Mode,Long>{

    private final ModeRepo modeRepo;

    ModeService(ModeRepo modeRepo) {
        this.modeRepo = modeRepo;
    }
    @Override
    protected JpaRepository<Mode, Long> repository() {
        return modeRepo;
    }

}
