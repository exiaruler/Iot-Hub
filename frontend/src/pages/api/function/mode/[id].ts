import { NextApiRequest, NextApiResponse } from "next";
import NextUtil from "@/NextUtil";
const base=new NextUtil();
// retrieve form
export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    var response=base.responseFormat();

    if(req.method=='GET'){
        var param=req.query.id;
        if(param=="new"){
            const request=await base.fetchRequest('/mode/new-record','GET',null,base.baseUrlIo);
            if(request.ok){
                response.data=await request.json();
            }else response.status=404;
        }
    }
    res.status(response.status).json(response.data);
}