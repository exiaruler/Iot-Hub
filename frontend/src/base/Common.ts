import UserAPI from "../api/UserAPI";
// common methods for application
export class Common{
    //public user=new UserAPI();
    public removeLogCookie(){
        document.cookie = "id=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/; SameSite=Lax";
      }
      /*
      public checkLogCookie(){
        var login=false;
        const cookie=document.cookie;
        const cookieArr=cookie.split(";");
          if(cookieArr[0].includes("id=")){
            var id=cookieArr[0].split("=")[1];
            if(id!=""){
              
              login=true;
            }
          }
        return login
      }
        */
}
