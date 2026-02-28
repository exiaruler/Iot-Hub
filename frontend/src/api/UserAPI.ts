import Util from "../base/Util";
export default class UserAPI extends Util{
    private base="/user/";
    private header={
        'Content-Type': 'application/json',
        "apikey":this.getApiKey()
    };
    public async login(json:any){
        try{
            //const request=await this.fetchRequest(this.base+"login","POST",json);
            const request=await fetch(this.getApiUrl()+this.base+"login",
            {   method:"POST",
                body:JSON.stringify(json),
                headers:this.header,
                credentials: 'include'
            });
            
            if(!request.ok){
                throw new Error('error happened');
            }
            const response=request.json();
            return response;
        }catch(err:any){
            this.throwError(err);
        }
    }

    public async checkLogin(){
        let login=false;
        const request=await this.fetchRequest(this.base+"auth","GET");
        const response=request.status;
        if(response===200){
            login=true;
        }
        return login;
    }
    public async userDetails(){
        let data=null;
        const request =await this.fetchRequest(this.base+"user-details","GET");
        if(request.ok){
            data= await request.json();
        }
        return data;
    }
    
    public async logout(){
        try{
            let res=false;
            const request=await fetch(this.getApiUrl()+this.base+"logout",{
                method:"POST",
                headers:this.header,
                credentials: 'include'
            });
            if(request.ok){
                res=true;
            }
            return res;
        }catch(err:any){
            this.throwError(err);
        }
    }
}