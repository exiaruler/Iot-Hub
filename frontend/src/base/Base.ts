import CryptoJS from 'crypto-js';
// api domain and key
class Base{

  public originUrl=this.getOriginUrl();
  
  private apiURlBase=process.env.REACT_APP_API_URL||"http://localhost:8000/api";
  // dev key
  private apikey=process.env.REACT_APP_API_KEY||"S7fgxFOTKTK8aCjq";
  // encryption key
  public encryptKey:string=process.env.REACT_APP_API_ENCRYPTKEY||"";
  // tinymce API kye
  public tinyKey:string=process.env.REACT_APP_TINY_KEY||process.env.NEXT_PUBLIC_API_TINY_KEY||"";
  // app
  public app=process.env.REACT_APP_APPLICATION||process.env.NEXT_PUBLIC_API_APPLICATION||"";
  // js vanilla fetch
  public apiCallConfig(method:string,body:any=null,encrypt:boolean=false){
      let date=new Date();
      var encryptGen="";
      if(encrypt) encryptGen=this.genereateEncrypt(10);
      var config:any={
        method:method.toUpperCase(),
        credentials: 'include',
        headers:{
          'Content-Type': 'application/json',
          "apikey":this.getApiKey(),
          "datetime":date,
          "datestring":date.toDateString(),
          "timestring":date.toTimeString(),
          "encrypt":encryptGen
        },
      };
      if(body!=null){
        if(typeof body!='string'){
          body=JSON.stringify(body);
        }
        body=this.encryptValueByKey(body,encryptGen);
        if(encrypt){
          var data={data:body};
          body=JSON.stringify(data);
        }
        config={
          method:method.toUpperCase(),
          credentials: 'include',
          headers:{
            'Content-Type': 'application/json',
            "apikey":this.getApiKey(),
            "datetime":date,
            "date":date.toDateString(),
            "time":date.toTimeString(),
            "encrypt":encryptGen
          },
          body:body
        };
      }
      return config;
    }
    public async serviceRequest(service:string,path:string,route:string,method:string,params:any=[],body:any=null){
      var url=this.getApiUrl()+"/service/api-call/"+service+"/"+path+"/"+route;
      var config=this.apiCallConfig(method,body);
      if(params.length>0){

      }
      const request=await fetch(url,config);
      return request;
    }
    public getOriginUrl():string{
      var url='http://localhost:3000';
      if(typeof window !== 'undefined'){
        url=window.location.origin;
      }
      return url;
    }

    public checkEnv():boolean{
      var result=false;
      if(this.apiURlBase==="http://localhost:8000/api"&&this.apikey==="S7fgxFOTKTK8aCjq") result=true;
      return result;
    }
    
    public getApiKey(){
      var key="";
      if(this.apikey!=undefined){
        key=this.apikey;
      }
      return key;
    }
    public getApiUrl(){
      var url="";
      if(this.apiURlBase!=undefined){
        url=this.apiURlBase;
      }
      return url;
    }
    
    public encryptValue(value:string){
      var encryption=value;
      if(this.encryptKey!=undefined&&this.encryptKey!=""){
        encryption=CryptoJS.AES.encrypt(value,this.encryptKey).toString();
      }
      return encryption;
    }
    public decryptValueToString(value:string){
      var convert=value;
      if(this.encryptKey!=undefined&&this.encryptKey!=""){
        const bytes=CryptoJS.AES.decrypt(value,this.encryptKey);
        convert = bytes.toString(CryptoJS.enc.Utf8);
      }
      return convert;
    }
    public generateEncryptKey(){
      var length=20;
      const chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
      let result = "";
      for (let i = 0; i < length; i++) {
        result += chars.charAt(Math.floor(Math.random() * chars.length));
      }
      return result;
    }

    public genereateEncrypt(length:number){
      const chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
      let result = "";
      for (let i = 0; i < length; i++) {
        result += chars.charAt(Math.floor(Math.random() * chars.length));
      }
      return result;
    }
    public encryptValueByKey(value:Object|string,key:string){
      var encryption=value;
      if(key!=""){
        encryption=CryptoJS.AES.encrypt(JSON.stringify(value),key).toString();
      }
      return encryption;
    }
    public decryptValueToStringByKey(value:string,key:string){
      var convert=value;
      if(key!=""){
        const bytes=CryptoJS.AES.decrypt(value,key);
        convert = bytes.toString(CryptoJS.enc.Utf8);
      }
      return convert;
    }
      
}
export default Base;