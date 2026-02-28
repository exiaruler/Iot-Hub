import { NextApiRequest, NextApiResponse } from "next";
import { NextUtil } from "@/pages/NextUtil";
const base=new NextUtil();
// application API
export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    let response=base.responseFormat();
    let encrypt=base.getProperties(req);
    let route="";
    let body=null;
    req=base.getRequest(req);
    if(req.query.url){
        route=req.query.url.toString();
    }
    if(req.body){
        body=req.body;
    }
    if(route!=""){
        const request=await base.fetchRequest(route,req.method,body,base.baseUrlIo);
        try{
            response.data=await request.json();
            response.status=await request.status;
        }catch(err){
            response.status=await request.status;
            response.data={error:"Server error"};
        }
    }else
    {
        response.status=404;
        response.data={error:"API does not exists"};
    }
    res.status(response.status).json(response.data);
}
