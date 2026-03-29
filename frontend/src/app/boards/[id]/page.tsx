import Client from "./client";
import { NextBase } from "@/NextBase";
async function getBoard(id:string) {
  let dataResp={
    board:null,
    deviceForm:null,
    boardHardware:null
  };
  const base=new NextBase();
  const boards=await base.fetchClientGet('/board/get-board-id/'+id);
  const form=await base.fetchClientGet('/device/new-record');
  const boardHarware=await base.fetchClientGet('/board/get-board-hardware/'+id);
  dataResp.board=boards;
  dataResp.deviceForm=form;
  dataResp.boardHardware=boardHarware;
  return dataResp;
}
export default async function Page({params}:any){
    const { id } = await params;
    const data=await getBoard(id);
    return (
        <Client board={data.board} boardHardware={data.boardHardware} deviceForm={data.deviceForm}/>
    )
}