import Util from "./Util";
import UserAPI from "../api/UserAPI";
import { CommonAPI } from "../api/CommonAPI";
import { getPages } from "../redux/slice/pageSlice";
import { useSelector,useDispatch} from 'react-redux';
import {page} from './interfaces/page';
// base class for UI pages
export default class UiBase {
    // common util tools
    public util=new Util();
    
    // user api calls
    public userApi=new UserAPI();
    public commonApi=new CommonAPI();
    public pages:Array<page>=useSelector(getPages);
    public UiBase(){
        
    }
    // get pages by section
    public getPagesSection(section:string){
        return this.pages.filter((pa:page)=>pa.section===section&&pa.show===true);
    }
    // get page by url
    public getPageUrl(url:string){
        return this.pages.find((pa:page)=>pa.url===url&&pa.show===true);
    }
    public checkPageUrl(url:any){
        var res=false;
        if(this.getPageUrl(url)!=undefined) res=true;
        return res;
    }
    public createUrl(page:page){
        let url=page.url;
        if(page.loadParam!=""){
            url+=page.loadParam;
        }
        return url;
    }
    public pageSession(pages:Array<page>){
        this.util.pageSession(pages);
    }
    public getPagesSession(){
        return this.util.getPagesSession();
    }
    public booleanStatus(bool:boolean){
        var show="Inactive";
        if(bool) show="Active";
        return show;
    }
    // form on change handler
    public onChange(key:string,value:any,setForm:any,form:any){
        setForm({...form,[key]:value});
    }
    public onChangeObject(key:any,value:any,setForm:any,form:any){
        setForm({...form,[key]:JSON.parse(value)});
    }
    public isFormGen(object:Record<string,any>):boolean{
        return (typeof object.code==='number'&&typeof object.error==='string'&&typeof object.fields==='object');
    }
    public isResponse(object:Record<string,any>):boolean{
        return (typeof object.success==='boolean'&&typeof object.statusCode==='number'&&typeof object.messageResponse==='string'&&typeof object.object==='object');
    }
}