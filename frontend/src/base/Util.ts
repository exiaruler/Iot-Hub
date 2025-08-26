import { Url } from 'url';
import Base from './Base';
import { CommonAPI } from '../api/CommonAPI';
import { page } from './interfaces/page';
export class Util extends Base {

    // converts date to show month style
  public  dateConversionMonth(date:any){
        const months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
        const dateArr=date.split("/");
        const month = months[dateArr[0]];
        return dateArr[1]+" "+month+" "+dateArr[2]
  }
  public elementGet(elementId:any){
      const get=document.getElementById(elementId);
      if(get!=null){
        const ele=get+".__reactProps$0ywopfjtiphh";
        return ele;
      }
      return "does not exist";
    }

   public cutOffString(text:string,cut:number,endtag:string=""){
    var result="";
    result=text.substring(0,cut);
    if(result!=text){
      result=result+endtag;
    }
    return result;
   }
   public addMillsToCurrent(milliseconds:number):Date{
    // Get the current date and time
    let currentDate = new Date();
    // Add the specified milliseconds
    currentDate.setMilliseconds(currentDate.getMilliseconds() + milliseconds);
    return currentDate;
   }
   public checkAuthorise(code:number):boolean{
    var result=false;
    if(code===401||code===403) result=true;
    return result;
   }
   public setJsonValue(json:any,key:any,value:any){
      var currentJson=json;
      currentJson[key]=value;
      return currentJson;
    }
   public createRoute(base:string,route:string,param:string=""):string{
      var url=this.getApiUrl()+base+route;
      if(param!==""){
        url=url+"/"+param
      }
      return url;
   }
   public sendAlert(error:any){
      alert(error);
    }
    public setAttributeValue(element:any,attribute:any,value:any){
      document.getElementById(element)?.setAttribute(attribute,value);
    }
    public throwError(err:any){
      console.error('Oh shit something happened');
      throw err;
    }
    public unauthorisedAccess(){
      window.location.href='/';
    }
    public removeLogCookie(){
      let currentDate = new Date();
      let timeout=currentDate.setMilliseconds(currentDate.getMilliseconds() - 10000);
      document.cookie="id= ; expires="+timeout;
    }
    public checkLogCookie(){
      var login=false;
      const cookie=document.cookie;
      const cookiesArr=cookie.split(" ");
      for(var i=0; i<cookiesArr.length; i++){
        var cook=cookiesArr[i];
        const cookieArr=cook.split(";");
        if(cookieArr[0].includes("id=")){
          var id=cookieArr[0].split("=")[1];
          if(id!=""&&id.length==24){
            login=true;
            break;
          }
        }
      }
      return login
    }

    public pageSession(pages:Array<page>){
        let toString=this.encryptValue(JSON.stringify(pages));
        sessionStorage.setItem(this.originUrl,toString);
    }
    public getPagesSession(){
        let data:Array<page>=[];
        let storage=sessionStorage.getItem(this.originUrl);
        if(storage!=""&&typeof storage=='string'){
          let json=JSON.parse(this.decryptValueToString(storage));
          data=json;
        }
        return data;
    }

    public async fetchRequest(api:string,method:string="GET",body:any=null,externalUrl:string=""):Promise<Response>{
      var config=this.apiCallConfig(method.toUpperCase(),body);
      var request:any=null;
      var baseUrl=this.getApiUrl();
      if(externalUrl!=="") baseUrl=externalUrl;
      try{
        request=await fetch(baseUrl+api,config);
      }catch(err){
        this.throwError(err);
      }
      return request;
    }
    public async fetchRequestComplete(api:string,method:string="GET",body=null,redirect:boolean=true,jsonReturn:boolean=true){
      var config=this.apiCallConfig(method.toUpperCase(),body);
      var request:any;
      var statusCode=200;
      try{
        request=await fetch(this.getApiUrl()+api,config);
        var response={
          ok:false,
          status:200,
          json:{},
        }
        response.status=await request.status;
        statusCode=await request.status;
        response.ok=await request.ok;
        if(jsonReturn){
          response.json=await request.json(); 
        }
        return response;
      }catch(err){
        if(redirect&&(this.checkAuthorise(statusCode)||request==undefined)){
          this.unauthorisedAccess();
        }else this.throwError(err);
      }
      return request;
    }
    public async requestHandler(request:any){
      try{
        var response={
          ok:false,
          status:200,
          json:{},
        }
        response.status=await request.status;
        response.ok=await request.ok;
        response.json=await request.json(); 
        return response;
      }catch(err){
        this.throwError(err);
      }

    }
}
export default Util;