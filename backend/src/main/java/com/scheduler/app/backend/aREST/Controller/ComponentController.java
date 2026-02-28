package com.scheduler.app.backend.aREST.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.scheduler.Base.ControllerBase;
import com.scheduler.app.backend.aREST.Models.Component;
import com.scheduler.app.backend.aREST.Models.FormModel.ComponentForm;
import com.scheduler.app.backend.aREST.Service.ComponentService;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping(value = "/component")
public class ComponentController extends ControllerBase{
    @Autowired
    private ComponentService componentService;

    @GetMapping(value="/new-component")
    public ComponentForm newComponent(){
        return new ComponentForm();
    }
    @GetMapping(value="/digital-pins")
    public String [] digitalPins(){
        return componentService.getDigitalPins();
    }
    @PostMapping(value = "/add-component/{id}",consumes = {"application/xml","application/json"})
    @ResponseBody
    public Component addComponent(@PathVariable long id,@RequestBody Component component){
        System.out.println(id);
        //return componentService.addComponent(id,component);
        return null;
    }
}
