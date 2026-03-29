import { NextApiRequest, NextApiResponse } from "next";
import NextUtil from "@/NextUtil";
const base=new NextUtil();
export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    var response=base.responseFormat();
    if(req.method=='GET'){
        const request=await base.fetchRequest('/hardware/get-hardwares','GET',null,base.baseUrlIo);
        if(request.ok){
            response.data=await request.json();
        }
    }
    res.status(response.status).json(response.data);
}