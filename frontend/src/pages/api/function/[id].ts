import { NextApiRequest, NextApiResponse } from "next";
import NextUtil from "@/NextUtil";
const base=new NextUtil();
// retrieve form
export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    var response=base.responseFormat();
    if(req.method=='GET'){
        var param=req.query.id;
        if(param=="new"){
            const request=await base.fetchRequest('/route/new-record','GET',null,base.baseUrlIo);
            if(request.ok){
                response.data=await request.json();
            }else response.status=404;
        }else
        {
            const request=await base.fetchRequest('/route/get-route/'+param,'GET',null,base.baseUrlIo);
            if(request.ok){
                response.data=await request.json();
            }else response.status=404;
        }
    }
    if(req.method=='POST'){
        const deviceId=req.query.id;
        const content=req.body;
        const com=content.commandId;
        if(com!=null){
            const request=await base.fetchRequest('/route/add-route-socket/'+deviceId+'/'+com,'POST',content,base.baseUrlIo);
            if(request.ok){
                response.data=await request.json();
            }else response.status=404;
        }
    }
    if(req.method=='PUT'){
        const routeId=req.query.id;
        const body=req.body;
        const com=body.commandId;
        if(com!=null){
            const request=await base.fetchRequest('/route/update-route-socket/'+routeId+'/'+com,'PUT',body,base.baseUrlIo);
            if(request.ok){
                response.data=await request.json();
            }else response.status=404;
        }
        
    }
    res.status(response.status).json(response.data);
}