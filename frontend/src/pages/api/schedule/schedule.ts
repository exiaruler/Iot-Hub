import { NextApiRequest, NextApiResponse } from "next";
import NextUtil from "@/NextUtil";
const base=new NextUtil();

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    var response=base.responseFormat();
    var encrypt=base.getProperties(req);
    req=base.getRequest(req);
    if(req.method=='GET'){
        const request=await base.fetchRequest('/schedule/get-schedules','GET',null,base.baseUrlIo);
        if(request.ok){
            response.status=200;
            response.data=await request.json();
        }
    }
    if(req.method=='POST'){
        const request=await base.fetchRequest('/schedule/add-schedule-socket','POST',req.body,base.baseUrlIo);
        var json=await request.json;
        response.status=await request.status;
        response.data=await base.encryptValueByKey(json,encrypt);
        
    }
    res.status(response.status).json(response.data);
}