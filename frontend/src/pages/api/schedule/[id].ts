import { NextApiRequest, NextApiResponse } from "next";
import { NextUtil } from "@/pages/NextUtil";
const base=new NextUtil();

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    var response=base.responseFormat();
    var encrypt=base.getProperties(req);
    req=base.getRequest(req);
    if(req.method=='GET'){
        const id=req.query.id;
        const request=await base.fetchRequest('/schedule/get-schedule/'+id,'GET',null,base.baseUrlIo);
        if(request.ok){
            response.status=200;
            response.data=await request.json();
        }
    }
    if(req.method=='DELETE'){
        const id=req.query.id;
        const request=await base.fetchRequest('/schedule/delete-schedule/'+id,'DELETE',null,base.baseUrlIo);
        response.status=await request.status;
        response.data=await request.json();
    }
    if(req.method=='PUT'){
        const id=req.query.id;
        const request=await base.fetchRequest('/schedule/update-schedule/'+id,'PUT',req.body,base.baseUrlIo);
        var json=await request.json;
        response.status=await request.status;
        response.data=await base.encryptValueByKey(json,encrypt);
        
    }
    res.status(response.status).json(response.data);
}