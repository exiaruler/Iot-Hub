import { NextApiRequest, NextApiResponse } from "next";
import { NextUtil } from "@/pages/NextUtil";
const base=new NextUtil();
// format for new records
export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    var response=base.responseFormat();
    if(req.method=='POST'){
        const boardId=req.query.boardId;
        const action=req.query.action;
        if(action=="restart"){
            //  /run-command/{board}/{action}/{commmand}/{system}
            const request=await base.fetchRequest('/task/run-command/'+boardId+'/action/reset/true','POST',null,base.baseUrlIo);
            if(request.ok){
                response.data=await request.json();
            }else response.status=404;
        }
        if(action=="restart-board-config"){
            const request=await base.fetchRequest('/task/run-command/'+boardId+'/action/resetboard/true','POST',null,base.baseUrlIo);
            if(request.ok){
                response.data=await request.json();
            }else response.status=404;
        }
        if(action=="update"){
            const request=await base.fetchRequest('/task/run-command/'+boardId+'/action/update/true','POST',null,base.baseUrlIo);
            if(request.ok){
                response.data=await request.json();
            }else response.status=404;
        }
    }
    res.status(response.status).json(response.data);
}