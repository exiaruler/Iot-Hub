import Util from "../base/Util";

export class CommonAPI extends Util{
    public async getSetting(){
        try{
            const request=await fetch(this.getApiUrl()+"setting/get-setting",this.apiCallConfig("GET"));
            if(!request.ok){
                throw new Error('error happened');
            }
            let data=await request.json();
            return data;
        }catch(err){
            this.throwError(err);
        }
    }
    public async getPages(app:string){
        let data=[];
        const request=await this.fetchRequest("/page/get-app-pages/"+app);
        if(!request.ok){
            throw new Error('error happened');
        }
        data=await request.json();
        return data;
    }
    public getPageAsync(app:string){
        const request=this.fetchRequest("/page/get-app-pages/"+app);
        return request;
    }
}