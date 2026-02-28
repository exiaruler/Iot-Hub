import Util from "../base/Util";

export class FormAPI extends Util{
    private base="/form/"
    public async getAllForms(){
        const request=this.getApiUrl()+this.base+"get-all-forms";
        try{
            const projectRequest=await fetch(request,this.apiCallConfig("GET"));
            if(!projectRequest.ok){
                throw new Error('error happened');
            }
            let data=await projectRequest.json();
            return data;
        }catch(err){
            this.throwError(err);
        }
    }
    public async getFormById(id:any){
        const request=await this.fetchRequest(this.base+"get-form-id/"+id,"GET");
        return request;
    }
    // get record from api
    public async getRecord(api:any,id:any){
        try{
            const request=await fetch(api+id,this.apiCallConfig("GET"));
            if(!request.ok){
                throw new Error('error happened');
            }
            let data=await request.json();
            return data;
        }catch(err){
            this.throwError(err);
        }
    }
    // get record with id param
    public async getRecordSingle(api:any){
        try{
            const request=await fetch(api,this.apiCallConfig("GET"));
            if(!request.ok){
                throw new Error('error happened');
            }
            let data=await request.json();
            return data;
        }catch(err){
            this.throwError(err);
        }
    }
}