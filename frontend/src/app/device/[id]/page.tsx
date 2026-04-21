import { NextBase } from "@/NextBase";

async function getDevice(id:string){
    const base=new NextBase();
    let fetchApis:any=await base.fetchGetApi([
    {api:'/device/get-device-unique-id/'+id,key:'device'},
  ]);
}
export default async function Page({params}:any){
    const {id}=await params;

    return(
        <>
        </>
    )
}