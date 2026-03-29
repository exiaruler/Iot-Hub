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
        const request=await base.fetchRequest('/board/get-board-id/'+id,'GET',null,base.baseUrlIo);
        if(request.ok){
            var hardwareRequest=await await base.fetchRequest('/board/get-board-hardware/'+id,'GET',null,base.baseUrlIo);
            if(hardwareRequest.ok){
                boardData.hardware=await hardwareRequest.json();
            }
            boardData.board=await request.json();
            response.data=boardData;
        }else response.status=404;
    }
    res.status(response.status).json(response.data);
}