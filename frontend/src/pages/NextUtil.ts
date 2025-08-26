import { NextBase } from "@/NextBase";
import { NextApiRequest } from "next";
interface ResponseFormat{
    status:number;
    data:any;
}

export class NextUtil extends NextBase{
    
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
    public responseFormat(){
        var response:ResponseFormat={
            status: 200,
            data: null
        }
        return response;
    }
    public getProperties(request: NextApiRequest):string{
        var encryptKey:string|any="";
        if(request.headers){
            var headers=request.headers;
            encryptKey=headers.encrypt;
        }
        return encryptKey;
    }
    public getRequest(request:NextApiRequest):NextApiRequest{
        var enc=this.getProperties(request);
        if(request.body&&enc!=""){
            var data=request.body?.data;
            if(data!==""){
                var convert=this.decryptValueToStringByKey(data,enc);
                request.body=convert;
            }
        }
        return request;
    }
}