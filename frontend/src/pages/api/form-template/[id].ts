import { NextApiRequest, NextApiResponse } from "next";
import { NextUtil } from "@/pages/NextUtil";
const base=new NextUtil();
// format for new records
export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    var response=base.responseFormat();
    if(req.method=='GET'){
        var formId=req.query.id;
        if(formId=="schedule"){
            const request=await base.fetchRequest('/schedule/new-record','GET',null,base.baseUrlIo);
            if(request.ok){
                response.data=await request.json();
            }else response.status=404;
        }
        if(formId=="function"){
            const request=await base.fetchRequest('/route/new-record','GET',null,base.baseUrlIo);
            if(request.ok){
                response.data=await request.json();
            }else response.status=404;
        }
        if(formId=="mode"){
            const request=await base.fetchRequest('/mode/new-record','GET',null,base.baseUrlIo);
            if(request.ok){
                response.data=await request.json();
            }else response.status=404;
        }
    }
    res.status(response.status).json(response.data);
}