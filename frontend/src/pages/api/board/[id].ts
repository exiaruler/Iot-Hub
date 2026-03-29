import { NextApiRequest, NextApiResponse } from "next";
import NextUtil from "@/NextUtil";
const base=new NextUtil();
export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    var response=base.responseFormat();
    if(req.method=='GET'){
        var boardData={
            board:null,
            hardware:null
        };
        var id=req.query.id;
        var hardware=null;
        const request=await base.fetchRequest('/board/get-board/'+id,'GET',null,base.baseUrlIo);
        if(request.ok){
            response.data=await request.json();
        }else response.status=await request.status;
    }
    if(req.method=='DELETE'){
        var id=req.query.id;
        const request=await base.fetchRequest('/board/delete/'+id,'DELETE',null,base.baseUrlIo);
        if(request.ok){
            //response.data=await request.json();
        }else response.status=await request.status;
    }
    res.status(response.status).json(response.data);
}