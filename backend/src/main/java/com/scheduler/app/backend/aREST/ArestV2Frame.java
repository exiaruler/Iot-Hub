package com.scheduler.app.backend.aREST;
import com.scheduler.Base.Base;
import com.scheduler.Base.JsonObject.JsonObject;
public class ArestV2Frame extends Base {
    // methods related to ArestV2

    // test board and device framework
    public boolean testBoardFrameWork(JsonObject obj,String ip){
        String [] keys={"id","name","hardware","connected"};
        boolean out=false;
        for(int i=0; i<keys.length; i++){
            if(obj.checkKey(keys[i])){
                out=true;
            }
            else
            {
                out=false;
                break;
            }
        }
        // test for changeDeviceRoute is implemented
        JsonObject json=jsonobj.jsonToObject(httpUtil.requestRoute(ip,"changeDevice", ""));
        if(!json.checkKey("return_value")) out=false;
        return out;
    }
    // test device framework if it follows arestv2
    public boolean testDeviceFramework(JsonObject obj,String ip){
        String [] keys={"Devices","Background","QueryData","SetDevice","Warning","Status"};
        boolean out=false;
        for(int i=0; i<keys.length; i++){
            if(obj.checkKey(keys[i])){
                out=true;
                // test if there any devices listed and it's string and character '|'
                if(keys[i].equals("Devices")){
                    String value=obj.findKeyValue("Devices");
                    String [] arr=value.split("\\|");
                    if(arr.length>1){
                        
                    }
                    if(value.length()<=0){
                        out=false;
                        break;
                    }
                }
                // test if background key is a boolean
                if(keys[i].equals("Background")){
                    String value=obj.findKeyValue("Background");
                    if(!value.equals("false")||!value.equals("true")){
                        out=false;
                        break;
                    }
                }
                // test for string
                if(keys[i].equals("QueryData")){
                    
                }
                // test for string
                if(keys[i].equals("SetDevice")){
                    
                }
                // test for string
                if(keys[i].equals("Warning")){
                    
                }
                // test for string
                if(keys[i].equals("Status")){
                    
                }
            }
            else
            {
                out=false;
                break;
            }
        }
        // validate query data commands
        String queryDataTest="routes|type|subtype|components|background";
        String query=requestQuery(ip,"1");
        if(!queryDataTest.equals(query)){
             out=false;
        }else
        // test commands
        {
            String arr[]=queryDataTest.split("\\|");
            for(int i=0; i<arr.length; i++){
                JsonObject json=jsonobj.jsonToObject(httpUtil.requestRoute(ip,"query", arr[i]));
                if(json.checkKey("return_value")){
                    if(json.findKeyValue("return_value").trim().equals("1")){
                        out=true;
                    }else
                    {
                     out=false;
                     break;   
                    }
                }else
                {
                    out=false;
                    break;
                }
            }

        } 
        return out;
    }
    public boolean testRoutes(String ip){
        boolean out=false;
        String rawRoutes=requestQuery(ip,"routes");
        String [] routeArr=rawRoutes.split("\\|\\|");
        String [] controlArr=routeArr[0].split("");
        if(controlArr[0].equals("\\|")){ out=true;}else out=false;
        if(controlArr[1].equals("~")){ out=true;}else out=false;
        return out;
    }
    public JsonObject changeDevice(String url,String param){
        JsonObject json=new JsonObject();
        String rawJson=httpUtil.requestRoute(url,"changeDevice", param);
        json=json.jsonToObject(rawJson);
        return json;
    }

}
