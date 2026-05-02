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
    functionRecord:null,
    boardHardware:null
  };
  const base=new NextBase();
  let fetchApis:any=await base.fetchGetApi([
    {api:'/board/get-board-id/'+boardId,key:'board'},
    {api:'/command/get-commands',key:'commands',result:[]},
    {api:'/command-parameter/get-electodes',key:'electrodes',result:[]},
    {api:'/route/new-record',key:'newFunction'},
    {api: "/mode/new-record",key: "newMode"},
    {api:'/route/get-route/'+route,key:'function'},
    {api:'/board/get-board-hardware/'+boardId,key:'boardHardware'}
  ]);
  dataResp.board=fetchApis.board;
  dataResp.commands=fetchApis.commands;
  dataResp.device=fetchApis.board.device.find((dev:any)=>dev.deviceId===deviceId);
  dataResp.electrodes=fetchApis.electrodes;
  dataResp.newFunction=fetchApis.newFunction;
  dataResp.newMode=fetchApis.newMode;
  dataResp.functionRecord=fetchApis.function;
  dataResp.boardHardware=fetchApis.boardHardware;
  return dataResp;
}
export default async function page({params}:any){
    const { board,device,route } = await params;
    const data=await getData(board,device,route);
    return(
        <Client boardHardware={data.boardHardware} electrodes={data.electrodes} device={data.device} record={data.functionRecord} board={data.board} commands={data.commands} newFunction={data.newFunction} newMode={data.newMode}/>
    );
}