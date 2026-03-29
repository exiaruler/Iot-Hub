import { NextBase } from "@/NextBase";
import Client from "./client";
const base=new NextBase();
async function getData(){
    
    const fetchApis:any=await base.fetchGetApi([
    {api: "/schedule/new-record",key: "newSchedule"},
    {api: "/schedule/get-schedules",key: "schedule",result:[]},
    {api: "/device/get-devices?query=device-routes",key: "devices",result:[]}
    
  ]);
  return fetchApis;
}
export default async function page(){
    const data=await getData();
    return(
        <Client form={data.newSchedule} schedule={data.schedule} devices={data.devices}/>
    );
}