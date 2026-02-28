package com.scheduler.app.backend.Hardware.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scheduler.Base.Base;
import com.scheduler.app.backend.Hardware.Models.Hardware;
import com.scheduler.app.backend.Hardware.Models.HardwarePins;
import com.scheduler.app.backend.Hardware.Repo.HardwarePinsRepo;
import com.scheduler.app.backend.Hardware.Repo.HardwareRepo;

@Configuration
@Service
public class HardwareService extends Base {
    public final HardwareRepo hardware;
    public final HardwarePinsRepo pins;
    private final ObjectMapper objectMapper;
    
    public HardwareService(HardwareRepo hardware,HardwarePinsRepo pins, ObjectMapper objectMapper){
        this.hardware=hardware;
        this.pins=pins;
        this.objectMapper = objectMapper;
    }

    public void initData(){
        List<Hardware> hardwares=new ArrayList<>();
        try {
            // Load the JSON file from resources
            ClassPathResource resource = new ClassPathResource("json/hardware.json");
            // Deserialize JSON array into a List
            hardwares=objectMapper.readValue(resource.getInputStream(),new TypeReference<List<Hardware>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(int i=0; i<hardwares.size(); i++){
            Hardware board=hardwares.get(i);
            Hardware existBoard=hardware.findHardwareByBoardName(board.getBoardName());
            //existBoard=hardware.getById(existBoard.getId());
            if(existBoard!=null){

            }else
            {
                List<HardwarePins> pins=board.getPins();
                for (HardwarePins pin : pins) {
                    pin.setId(0);
                    pin.setHardware(board);
                }
                board.setPins(pins);
                board.setId(0);
                hardware.save(board);
            }
        }
        System.out.println("Hardware init");
    }
    public List<HardwarePins> createPinList( HashMap<String,Integer> list,Hardware hard){
        List<HardwarePins> pinList=new ArrayList<>();
        for(String i:list.keySet()){
            HardwarePins newPin=new HardwarePins(hard,i,list.get(i));
            pinList.add(newPin);
        }
        return pinList;
    }
    public Hardware getBoard(long id){
        Hardware res=hardware.getReferenceById(id);
        return res;
    }
    public List<Hardware> getBoards(){
        return hardware.findAll();
    }
}
