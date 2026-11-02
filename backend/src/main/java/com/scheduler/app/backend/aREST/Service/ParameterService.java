package com.scheduler.app.backend.aREST.Service;
import org.springframework.stereotype.Service;
import java.util.*;
import com.scheduler.Base.Base;
import com.scheduler.app.backend.aREST.Repo.ParamRepo;
import com.scheduler.app.backend.aREST.Function.Models.Mode;
import com.scheduler.app.backend.aREST.Models.Parameter;
@Service
public class ParameterService extends Base{
    
    private final ParamRepo paramService;
    public ParameterService(ParamRepo paramService){
        this.paramService=paramService;
    }
    public String [] getParamsArray(long id){
        return paramService.getAllParamValueArray(id);
    }
    public List<Parameter> addParameters(List<String> params,Mode mode){
        List<Parameter> paramList=new ArrayList<Parameter>();
        int order=1;
        for(int i=0; i<params.size(); i++){
            Parameter parameter=new Parameter(mode, order,params.get(i));
            paramList.add(parameter);
            order++;
        }
        return paramList;
    }
    
}
