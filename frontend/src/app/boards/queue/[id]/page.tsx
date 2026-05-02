import Client from "./client";
import { NextBase } from "@/NextBase";
async function getData(id:string){
    const base=new NextBase();
    const data=await base.fetchClientGet('/board/queue/get-queues/'+id,[]);
    return data;
}
export default async function Page({params}:any){
    const {id}=await params;
    const queues=await getData(id);
    return(
        <Client queue={queues} boardId={id}/>
    )
}