import Util from "./base/Util";
const os = require('os');
const config=require("../config.json");
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
export type ObjectRecord = Record<string, any> | null;
export type ObjectArray = ObjectRecord[];
// override class
export class NextBase extends Util{
    
    // override encrypt key in next.js
    encryptKey=config["encrypt-key"]||'';

    // frontend URL
    public baseURL=this.nextAppUrl();
    // IoT URL
    public baseUrlIo=config["iot-url"]||"http://localhost:8080";
    // encryption
    encrypt=false;
    
    public nextAppUrl():string{
        let url="http://localhost"+":"+3000;
        if(config['iot-url']!=""){
            url="https://"+os.hostname();
        }
        return url;
    }
    public getOriginUrl():string{
        let url="http://localhost"+":"+3000;
        if(config['iot-url']!=""){
            url="https://"+os.hostname();
        }
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
    public async fetchClientGet(url:string,emptyResult?:null|[]){
        let response=null;
        if(emptyResult) response=emptyResult;
        let encrypt="";
        let config=this.apiCallConfig('GET',null,false);
        encrypt=config.headers.encrypt;
        try{
            const request=await fetch(this.baseUrlIo+url,config);
            if(request.ok){
                response=await request.json();
                //response=this.decryptValueToStringByKey(response,encrypt);
            }
        }catch(err){
            return response;
        }
        return response;
    }
    public async fetchClientGetQuery(url:string){
        let response=null;
        let encrypt="";
        let config=this.apiCallConfig('GET',null,false);
        encrypt=config.headers.encrypt;
        try{
            const request=await fetch(this.baseURL+'/api/app/iot?url='+url,config);
            if(request.ok){
                response=await request.json();
                //response=this.decryptValueToStringByKey(response,encrypt);
            }
        }catch(err){

        }
        return response;
    }
    public async fetchClientQuery(url:string,method:string,body:Record<string,any>|null=null):Promise<requestResponse>{
        let response:requestResponse=this.newResponse();
        let encrypt="";
        let data=null;
        const config=this.apiCallConfig(method,body,false);
        encrypt=config.headers.encrypt;
        response.encrypt=encrypt;
        try{
            const request=await fetch(this.baseURL+'/api/app/iot?url='+url,config);
            let stat=await request.status;
            response.status=stat;
            response.request=request;
            data=await request.json();
            if(data!=null){
                response.json=data;
            }
            response.json=JSON.parse(this.decryptValueToStringByKey(data,encrypt));
            response.ok=await request.ok;
        }catch(err){
            return response;
        }
        return response;
    }
    public async fetchClient(url:string,method:string,body:any=null):Promise<requestResponse>{
        let response:requestResponse=this.newResponse();
        let encrypt="";
        const config=this.apiCallConfig(method,body,false);
        encrypt=config.headers.encrypt;
        response.encrypt=encrypt;
        try{
            const request=await fetch(this.baseURL+'/api'+url,config);
            let stat=await request.status;
            response.status=stat;
            response.request=request;
            let data=await request.json();
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
        let result={};
        for(let i=0; i<input.length; i++){
            let req=input[i];
            try {
                const request=await this.fetchClientGet(req.api);
                if(request!=null){
                    result={...result,[req.key]:request};
                }else result={...result,[req.key]:req.result};
            } catch (error) {
                
            }
        }
        return result;
    }
    public async fetchGetApiQuery(input:Array<GetInput>){
        let result={};
        for(let i=0; i<input.length; i++){
            let req=input[i];
            const request=await this.fetchClientGetQuery(req.api);
            if(request!=null){
                result={...result,[req.key]:request};
            }else result={...result,[req.key]:req.result};
        }
        return result;
    }

}