import { NextBase } from "@/NextBase";
import { NextApiRequest } from "next";
interface ResponseFormat{
    status:number;
    data:any;
}

export default class NextUtil extends NextBase{
    
    private jsonTransform(data:any){
        return { ...data, source: 'proxied-through-nextjs' };
    }
    public returnResponse(data:any){
        const transformedData=this.jsonTransform(data);
        return new Response(JSON.stringify(transformedData),
    {
        headers: { 'Content-Type': 'application/json' }
    });
    }
    public responseFormat():ResponseFormat{
        let response:ResponseFormat={
            status: 200,
            data: null
        }
        return response;
    }
    public getProperties(request: NextApiRequest):string{
        let encryptKey:string|any="";
        if(request.headers){
            let headers=request.headers;
            encryptKey=headers.encrypt;
        }
        return encryptKey;
    }
    public getRequest(request:NextApiRequest):NextApiRequest{
        let enc=this.getProperties(request);
        if(request.body&&enc!=""){
            let data=request.body?.data;
            if(data!==""){
                let convert=this.decryptValueToStringByKey(data,enc);
                request.body=convert;
            }
        }
        return request;
    }
}