import PageGroup from "@/app/next-components/pageGroup";
import Client from "./client";
import { NextBase } from "@/NextBase";
async function getData(boardId:string,deviceId:string,route:string) {
  let dataResp={
    board:null,
    device:null,
    commands:[],
    electrodes:[],
    newFunction:null,
    newMode:null,
    functionRecord:null
  };
  const base=new NextBase();
  let fetchApis:any=await base.fetchGetApi([
    {api:'/board/uni-board/'+boardId,key:'board'},
    {api:'/command/command',key:'commands',result:[]},
    {api:'/command/core/core',key:'electrodes',result:[]},
    {api:'/form-template/function',key:'newFunction'},
    {api: "/function/mode/new",key: "newMode"},
    {api:'/function/'+route,key:'function'}
  ]);
  dataResp.board=fetchApis.board;
  dataResp.commands=fetchApis.commands;
  dataResp.device=fetchApis.board.board.device.find((dev:any)=>dev.deviceId===deviceId);
  dataResp.electrodes=fetchApis.electrodes;
  dataResp.newFunction=fetchApis.newFunction;
  dataResp.newMode=fetchApis.newMode;
  dataResp.functionRecord=fetchApis.function;
  return dataResp;
}
export default async function page({params}:any){
    const { board,device,route } = await params;
    let data=await getData(board,device,route);
    return(
        <PageGroup url="/device/function/">
        <Client electrodes={data.electrodes} device={data.device} record={data.functionRecord} board={data.board} commands={data.commands} newFunction={data.newFunction} newMode={data.newMode}/>
        </PageGroup>
    );
}