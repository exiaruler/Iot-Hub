import PageGroup from "@/app/next-components/pageGroup";
import Client from "./client";
import { NextBase } from "@/NextBase";
async function getBoard(id:string) {
  let dataResp={
    board:null,
    deviceForm:null
  };
  const base=new NextBase();
  let boards=await base.fetchClientGet('/board/uni-board/'+id);
  let form=await base.fetchClientGet('/form/board');
  dataResp.board=boards;
  dataResp.deviceForm=form;
  return dataResp;
}
export default async function Page({params}:any){
    const { id } = await params;
    const data=await getBoard(id);
    return (
        <PageGroup url={'/board/'}>
        <Client data={data} board={data.board} deviceForm={data.deviceForm}/>
        </PageGroup>
    )
}