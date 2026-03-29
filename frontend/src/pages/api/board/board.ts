import { NextApiRequest, NextApiResponse } from "next";
import NextUtil from "@/NextUtil";
const base=new NextUtil();
export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    let response=base.responseFormat();
    let encrypt=base.getProperties(req);
    req=base.getRequest(req);
    //console.log('encrypt key '+encrypt);
    if(req.method=='GET'){
        const request=await base.fetchRequest('/board/getboards','GET',null,base.baseUrlIo);
        if(request.ok){
            let boards=await request.json();
            boards.push({id:0,name:'Add Board'});
            response.data=boards;
        }
    }
    if(req.method=='POST'){
        let body=req.body;
        const request=await base.fetchRequest('/board/add-board-socket','Post',body,base.baseUrlIo);
        response.status=await request.status;
        if(request.ok){
            response.data=await request.json();
        }
        
    }
    res.status(response.status).json(response.data);
}