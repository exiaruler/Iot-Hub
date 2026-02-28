import PageGroup from "../next-components/pageGroup";
import { NextBase } from "@/NextBase";
import Client from "./client";
const base=new NextBase();
async function getData(){
    
    const fetchApis:any=await base.fetchGetApi([
    {api: "/form-template/schedule",key: "newSchedule"},
    {api: "/schedule/schedule",key: "schedule",result:[]},
    {api: "/board/device/device-routes",key: "devices",result:[]}
    
  ]);
  return fetchApis;
}
export default async function page(){
    const data=await getData();
    return(
        <PageGroup>
        <Client form={data.newSchedule} schedule={data.schedule} devices={data.devices}/>
        </PageGroup>
    );
}