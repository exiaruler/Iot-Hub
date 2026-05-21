import Client from "./client";
import { NextBase } from "@/NextBase";
export const dynamic = 'force-dynamic';
async function getBoards() {
  const base=new NextBase();
  const requests=await base.fetchGetApi([{api:'/board/getboards',key:'boards',result:[]},
    {api:'/board/new-record',key:'boardForm'},
    {api:'/hardware/get-hardwares',key:'hardwares',result:[]}
  ]);
  return requests;
}
export default async function Page(){
    const data=await getBoards();
    return(
    <Client boards={data?.boards} hardwares={data?.hardwares} boardForm={data?.boardForm}/>
    
    
    );
}