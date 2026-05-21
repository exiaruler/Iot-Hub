import Client from "./client";
import { NextBase } from "@/NextBase";
async function getBoard(id:string) {
  const base=new NextBase();
  const requests=await base.fetchGetApi([{api:'/board/get-board-id/'+id,key:'board'},
    {api:'/device/new-record',key:'deviceForm'},
    {api:'/board/get-board-hardware/'+id,key:'boardHardware'}
  ]);
  return requests;
}
export default async function Page({params}:any){
    const { id } = await params;
    const query={};
    const data=await getBoard(id);
    return (
        <Client board={data?.board} boardHardware={data?.boardHardware} deviceForm={data?.deviceForm} query={query}/>
       
    )
}