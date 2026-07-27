import { NextBase, ObjectRecord } from "@/NextBase";
import Client from "./client";
async function getData(id:string,deviceId?:string){
    const base=new NextBase();
    const requests=await base.fetchGetApi([{api:'/task/get-tasks/'+id+'/true',key:'tasks',result:[]}
  ]);
    return requests;
}
export default async function Page({params}:any){
    const {id}=await params;
    const data=await getData(id);
    return(
        <Client queue={data?.tasks} boardId={id}/>
    )
}