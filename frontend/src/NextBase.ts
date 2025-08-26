import Util from "./base/Util";
interface GetInput{
    api:string,
    key:string,
    result?:null|Array<any>
}
interface requestResponse{
    request:Response;
    encrypt:string;
    json:any|Object|JSON;
    status:number;
    ok:boolean;
}
// override class
export class NextBase extends Util{
    // override encrypt key in next.js
    encryptKey=process.env.NEXT_PUBLIC_API_ENCRYPTKEY||'';

    // frontend URL
    baseURL="http://localhost:3000";
    // IoT URL
    baseUrlIo="http://localhost:8080";
    // encryption
    encrypt=false;
  
    public getOriginUrl():string{
      var url=process.env.NEXT_PUBLIC_API_DOMAIN||'http://localhost:3000';
      return url;
    }
    public newResponse():requestResponse{
        return {
            request: new Response,
            encrypt: "",
            json: null,
            status: 400,
            ok:false
        };
    }
    public async fetchClientGet(url:string){
        var response=null;
        var encrypt="";
        var config=this.apiCallConfig('GET',null,false);
        encrypt=config.headers.encrypt;
        try{
            const request=await fetch(this.baseURL+'/api'+url,config);
            if(request.ok){
                response=await request.json();
                //response=this.decryptValueToStringByKey(response,encrypt);

            }
        }catch(err){

        }
        return response;
    }
    public async fetchClient(url:string,method:string,body:any):Promise<requestResponse>{
        var response:requestResponse=this.newResponse();
        var encrypt="";
        const config=this.apiCallConfig(method,body,false);
        encrypt=config.headers.encrypt;
        response.encrypt=encrypt;
        try{
            const request=await fetch(this.baseURL+'/api'+url,config);
            var stat=await request.status;
            response.status=stat;
            response.request=request;
            var data=await request.json();
            if(data!=""){

            }
            response.json=JSON.parse(this.decryptValueToStringByKey(data,encrypt));
            if(request.ok){
                response.ok=true;
            }
        }catch(err){
            return response;
        }
        return response;
    }
    public async fetchGetApi(input:Array<GetInput>){
        var result={};
        for(var i=0; i<input.length; i++){
            var req=input[i];
            const request=await this.fetchClientGet(req.api);
            if(request!=null){
                result={...result,[req.key]:request};
            }else result={...result,[req.key]:req.result};
        }
        return result;
    }

}