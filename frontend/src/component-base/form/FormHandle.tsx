'use client'
import { Component, createContext, createRef, ReactNode } from "react";
import {Util} from "../../base/Util";
import React from "react";
export interface Props{
    children?:Array<ReactNode>|ReactNode|Array<Array<ReactNode>>;
    onSubmit?:any;
    // record layout
    recordLayout:Record<string,any>;
    // record
    record?:Record<string,any>|null;
    // post API
    post?:string;
    // put API
    put?:string;
    // id key of record
    idKey:string;
    // override using built in control
    formControlOverride?:boolean;
    // override of api response
    streamOverride?:boolean;
    debug?:boolean;
    disableValidation?:boolean;
}
interface State{
    // record
    record:Record<string,any>|null;
    // layout of record
    recordLayout:Record<string,any>|string;
    // field errors
    errors:Record<string,any>;
    // warning errors
    warnings:Record<string,any>;
    // record id
    id:string|number;
    // form submission response
    statusResponse:number;
    submissionResponse:any;
}
export interface FormContext{
    record:Record<string,any>|null;
    warnings:Record<string,any>|null;
    errors:Record<string,any>|null;

}
export const RecordContext=createContext<FormContext>({record:null,warnings:{},errors:{}});
export const ErrorContext=createContext<Record<string,any>|null>(null);
export const WarningContext=createContext<Record<string,any>|null>(null);

export default class FormHandle extends Component<Props,State>{
    constructor(props:Props) {
            super(props);
            this.state = {
                record:null,
                recordLayout:{},
                errors:{},
                warnings:{},
                id:0,
                statusResponse:0,
                submissionResponse:null
            };
    }
    public blankKeys:string="";
    public inputFields:Array<any>=[];
    // static record, use for call back events only
    public record:Record<string,any>|null=null;
    public recordLayoutString:string="";
    public inputRefs: Record<string, React.RefObject<Component>> = {};
    public statusResponse:number=0;
    overrideUrl:string="";
    public submissionResponse:any=null;
    public response:any|null=null;
    public ok:boolean=false;
    public util=new Util();

    componentDidMount(): void {
        if(this.props.recordLayout){
            let rec=this.props.recordLayout;
            const blankKeys=this.mapKeys(this.props.recordLayout);
            this.blankKeys=JSON.stringify(blankKeys);
            this.recordLayoutString=JSON.stringify(this.props.recordLayout);
            this.setState({...this.state,recordLayout:this.recordLayoutString,record:rec,errors:blankKeys,warnings:blankKeys});
            
            this.record=rec;
            if(this.props.record){
                this.setRecord(this.props.record);
            }
        }   
    }
   
    componentDidUpdate(prevProps: Readonly<Props>, prevState: Readonly<State>, snapshot?: any): void {
        if(JSON.stringify(prevState.recordLayout)!=JSON.stringify(this.props.recordLayout)){
            let rec=this.state.record;
            if(prevState.id==0){
                rec=this.props.recordLayout;
            }
            this.setState({...this.state,recordLayout:this.props.recordLayout,record:rec});
        }
        if(this.props.idKey&&this.props.record){
            let currId=this.props.record[this.props.idKey]||0;
            if(prevState.id!==currId&&currId!="0"){
                this.setRecord(this.props.record);
            }
        }    
    }
    public mapKeys(layout:Record<string,any>):Record<string,any>{
        const blankEntries = Object.keys(layout).map(key => [key, ""]);
        const blankObject = Object.fromEntries(blankEntries);
        return blankObject;
    }
    public addInput(component:Component):void{
        let exists=this.inputFields.filter(((input:any)=>input===component))
        if(exists.length===0){
            this.inputFields.push(component);
        }
    }
    // set prop warning in input field component
    public setWarning(name:string,error:string):void{
    
        if(Object.hasOwn(this.state.warnings,name)){
            let currWarn=this.state.warnings[name];
            //if(currWarn!="") currWarn+="\n";
            currWarn=error; 
            this.setState({...this.state,warnings:{...this.state.warnings,[name]:currWarn}});
        }
    }
   // update record value
    onChangeRecord(key:string,value:any):void{
        this.setState({...this.state,record:{...this.state.record,[key]:value}});
        if(this.record!=null)this.record[key]=value;
    }
    // update record value upon event
    onChangeHandlerValue(event:React.ChangeEvent<HTMLInputElement>):void{
        let value=event.target.value;
        let name=event.target.name;
        this.setState({...this.state,record:{...this.state.record,[name]:value}});
        if(this.record!=null)this.record[name]=value;
    }
    // get string layout to object
    public getRecordLayout():Record<string,any>|null{
        let obj=null;
        if(this.recordLayoutString!=""){
            obj=JSON.parse(this.recordLayoutString);
        }
        return obj;
    }
    // get record
    getRecord():Record<string,any>|null{
        return this.state.record;
    }
    // get record id
    getId():string{
        let idKey=this.props.idKey;
        let id="";
        if(this.state.record!=null){
            id=this.state.record[idKey];
        }
        return id;
    }
    // retrieve value from record
    getRecordValue(key:string):any|null{
        let value:any=null;
        if(this.state.record!=null){
            if(this.state.record.hasOwnProperty(key)){
                value=this.state.record[key];
            }
        }
        return value;
    }
    public setRecord(rec:Record<string, any>):void{
        if(rec){
            this.setState({...this.state,record:rec,id:rec[this.props.idKey]});
            this.record=rec;
        }
    }
    public newRecord():void{
        this.statusResponse=0;
        this.submissionResponse=null;
        this.response=null;
        this.ok=false;
        this.record=this.getRecordLayout();
        this.clearWarnings();
        this.setState({...this.state,record:this.getRecordLayout(),id:0,statusResponse:0,submissionResponse:null,errors:JSON.parse(this.blankKeys),warnings:JSON.parse(this.blankKeys)});
    }
    public clearWarnings():void{
        if(this.inputFields.length>0)this.inputFields.map((input)=>input.current.setWarning(""));
    }

    async onsubmit(event:any):Promise<void>{
        event.preventDefault();
        if(this.props.debug) debugger;
        if(this.state.id!=""){
            if(this.props.put){
                const request=await this.util.fetchRequest(this.props.put+this.state.id,'PUT',this.state.record,this.overrideUrl);
                this.response=request;
                if(request!=null&&!this.props.streamOverride){
                    const status=await request.status;
                    this.ok=request.ok;
                    const dataResp=await request.json;
                    this.statusResponse=status;
                    this.submissionResponse=dataResp;
                    this.setState({...this.state,statusResponse:status,submissionResponse:dataResp});
                    if(!request.ok){
                        this.renderValidation(dataResp);
                    }
                }else if(!this.props.streamOverride) this.setState({...this.state,statusResponse:400,submissionResponse:null});
            }
        }else
        {
            if(this.props.post){
                const request=await this.util.fetchRequest(this.props.post,'POST',this.state.record,this.overrideUrl);
                this.response=request;
                if(request!=null&&!this.props.streamOverride){
                    const status=await request.status;
                    this.ok=request.ok;
                    const dataResp=await request.json;
                    this.statusResponse=status;
                    this.submissionResponse=dataResp;
                    this.setState({...this.state,statusResponse:status,submissionResponse:dataResp});
                    if(!request.ok){
                        this.renderValidation(dataResp);
                    }
                }else if(!this.props.streamOverride) this.setState({...this.state,statusResponse:400,submissionResponse:null});
            }
        }
        if(this.props.onSubmit){
            this.props.onSubmit();
        }
    }
    public renderValidation(validations: Record<string, any>):void{
        if(this.props.disableValidation)return;
        if(validations.errors){
            const errors=validations.errors;
            const formError=this.state.warnings;
            for(let key in errors){
                formError[key]=errors[key];
                this.setState({...this.state,warnings:formError});
            }
        }
    }
    render(){
        return(
            <form onSubmit={(event:any)=>this.onsubmit(event)}>
            <RecordContext.Provider value={{record:this.state.record,warnings:this.state.warnings,errors:this.state.warnings}}>
            {
                this.props.children
            }
            </RecordContext.Provider>
            </form>
        )
    }
}