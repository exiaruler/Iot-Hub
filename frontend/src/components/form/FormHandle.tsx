'use client'
import { Component, createRef, ReactNode } from "react";
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
}
interface State{
    // record
    record:Record<string,any>|null;
    // layout of record
    recordLayout:Record<string,any>;
    // record id
    id:string|number;
    // form submission response
    statusResponse:number;
    submissionResponse:any;
}
export default class FormHandle extends Component<Props,State>{
    constructor(props:Props) {
            super(props);
            this.state = {
                record:null,
                recordLayout:{},
                id:0,
                statusResponse:0,
                submissionResponse:null
            };
    }
    public inputFields:Array<any>=[];
    // static record, use for call back events only
    public record:Record<string,any>|null=null;
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
            this.setState({...this.state,recordLayout:this.props.recordLayout,record:rec});
            this.record=rec;
            if(this.props.record){
                this.setRecord(this.props.record);
            }
        }   
    }
   
    componentDidUpdate(prevProps: Readonly<Props>, prevState: Readonly<State>, snapshot?: any): void {
        if(prevState.recordLayout!==this.props.recordLayout){
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
    
    public addInput(component:Component):void{
        let exists=this.inputFields.filter(((input:any)=>input===component))
        if(exists.length===0){
            this.inputFields.push(component);
        }
    }
    // set prop warning in input field component
    public setWarning(name:string,error:string):void{
        let comp=this.inputFields.find((input:any)=>input.current.props.name==name)||null;
        if(comp!=null){
            let warn=comp.current;
            warn.setWarning(error);
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
        this.setState({...this.state,record:this.props.recordLayout,id:0,statusResponse:0,submissionResponse:null});
        this.record=this.props.recordLayout;
        this.clearWarnings();
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
                    const dataResp=await request.json();
                    this.statusResponse=status;
                    this.submissionResponse=dataResp;
                    this.setState({...this.state,statusResponse:status,submissionResponse:dataResp});
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
                    const dataResp=await request.json();
                    this.statusResponse=status;
                    this.submissionResponse=dataResp;
                    this.setState({...this.state,statusResponse:status,submissionResponse:dataResp});
                }else if(!this.props.streamOverride) this.setState({...this.state,statusResponse:400,submissionResponse:null});
            }
        }
        if(this.props.onSubmit){
            this.props.onSubmit();
        }
    }

    render(){
        return(
            <form onSubmit={(event:any)=>this.onsubmit(event)}>
            {
                this.props.children
            }
            </form>
        )
    }
}