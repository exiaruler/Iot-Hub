import { NextBase, ObjectRecord } from "@/NextBase";

async function getDevice(id:string){
    const base=new NextBase();
    const fetchApis:ObjectRecord=await base.fetchGetApi([
    {api:'/device/get-device-unique-id/'+id,key:'device'},
  ]);
  return fetchApis;
}
export default async function Page({params}:any){
    const {boardId,id}=await params;

    return(
        <>
        </>
    )
}