import { NextBase } from "@/NextBase";
import Client from "./client";
const base=new NextBase();
export const dynamic = 'force-dynamic';
async function getData(){
    
    const fetchApis:any=await base.fetchGetApi([
    {api: "/firmware/new-record",key: "form"},
    {api: "/firmware/get-records",key: "records",result:[]}
    
  ]);
  return fetchApis;
}
export default async function page(){
    const data=await getData();
    return(
        <Client form={data.form} records={data.records}/>
    );
}