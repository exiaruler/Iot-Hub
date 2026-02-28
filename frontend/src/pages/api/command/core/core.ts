import { NextApiRequest, NextApiResponse } from "next";
import { NextUtil } from "@/pages/NextUtil";
const base=new NextUtil();
export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    var response=base.responseFormat();
    if(req.method=='GET'){
        const request=await base.fetchRequest('/command-parameter/get-electodes','GET',null,base.baseUrlIo);
        if(request.ok){
            var data=await request.json();
            response.data=data;
        }
    }
    res.status(response.status).json(response.data);
}