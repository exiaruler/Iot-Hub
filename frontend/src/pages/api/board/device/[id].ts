import { NextApiRequest, NextApiResponse } from "next";
import NextUtil from "@/NextUtil";
const base=new NextUtil();
export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    var response=base.responseFormat();
    if(req.method=='GET'){
        var id=req.query.id;
        if(id=='device-routes'){
            const request=await base.fetchRequest('/device/get-devices?query=device-routes','GET',null,base.baseUrlIo);
            if(request.ok){
                response.data=await request.json();
            }
        }
        if(id=='all'){
            const request=await base.fetchRequest('/device/get-devices','GET',null,base.baseUrlIo);
            if(request.ok){
                response.data=await request.json();
            }
        }
        
    }
    res.status(response.status).json(response.data);
}