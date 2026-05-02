import Client from "./client";
import { NextBase } from "@/NextBase";

async function getBoards() {
  let dataResp={
    boards:[],
    hardwares:[],
    boardForm:null
  };
  const base=new NextBase();
  // fetch user boards
  let boards=await base.fetchClientGet('/board/getboards',[]);
  const boardForm=await base.fetchClientGet('/board/new-record');
  dataResp.boards=boards;
  dataResp.boardForm=boardForm;
  // fetch hardware boards
  dataResp.hardwares=await base.fetchClientGet('/hardware/get-hardwares',[]);
  return dataResp;
}
export default async function Page(){
    const data=await getBoards();
    return(
    <Client boards={data.boards} hardwares={data.hardwares} boardForm={data.boardForm}/>
    
    
    );
}