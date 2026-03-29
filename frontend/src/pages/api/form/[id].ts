import { NextApiRequest, NextApiResponse } from "next";
import NextUtil from "@/NextUtil";
const base=new NextUtil();
// retrieve form
export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    var response=base.responseFormat();
    if(req.method=='GET'){
        var id=req.query.id;
        const request=await base.fetchRequest('/form/get-form/'+id,'GET');
        if(request.ok){
            response.data=await request.json();;
        }else response.status=404;
    }
    res.status(response.status).json(response.data);
}