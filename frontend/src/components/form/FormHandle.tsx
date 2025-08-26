'use client'
import { Component, ReactNode } from "react";
import {Util} from "../../base/Util";
import React from "react";
export type Props={
    children?:Array<ReactNode>;
    onSubmit?:any;
    recordLayout:Object;
    record?:Object;
    post?:string;
    put?:string;
    idKey:string;
    formControlOverride?:boolean;
}
interface State{
    // record
    record:Object|any;
    // layout of record
    recordLayout:Object;
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
                record:{},
                recordLayout:{},
                id:0,
                statusResponse:0,
                submissionResponse:null
            };
    }
    statusResponse=0;
    submissionResponse=null;
    util=new Util();
    public inputComponents:Array<string>=['FormGenSelect','FormGenText','FormGenCheckBox'];

    componentDidMount(): void {
        if(this.props.recordLayout){
            this.setState({...this.state,recordLayout:this.props.recordLayout,record:this.props.recordLayout});
        }   
    }

    checkComponent(element:any){
        var name=element?.type.name;
        var res=false;
        if(this.inputComponents.indexOf(name)>-1) res=true;
        return res;
    }
    onChangeRecord(key:string,value:any){
        this.setState({...this.state,record:{...this.state.record,[key]:value}});
    }
    onChangeHandlerValue(event:React.ChangeEvent<HTMLInputElement>){
        var value=event.target.value;
        var name=event.target.name;
        this.setState({...this.state,record:{...this.state.record,[name]:value}});
    }
    getRecord(){
        return this.state.record;
    }
    getId(){
        var idKey=this.props.idKey;
        var id=this.state.record[idKey];
        return id;
    }
    getRecordValue(key:string){
        var value:any=null;
        if(this.state.record.hasOwnProperty(key)){
            value=this.state.record[key];
        }
        return value;
    }
    setRecord(rec:Object){
        if(rec){
            this.setState({...this.state,record:rec,id:rec[this.props.idKey]});
        }
    }
    newRecord(){
        this.setState({...this.state,record:this.state.recordLayout,id:0,statusResponse:0,submissionResponse:null});
    }
    formRender(){
        if(this.props.children?.length>0&&!this.props.formControlOverride){
            return this.props.children?.map((child)=>{
                if(this.checkComponent(child)){
                    var name=child.props.name;
                    if(this.props.recordLayout.hasOwnProperty(name)){
                        var value=this.props.recordLayout[name];
                        var props=child.props;
                        //props={...props,value:value};
                        var ele=React.cloneElement(child,{...props,value:this.state.record[name]});
                        return ele;
                    }
                }
                return child;
            });
        }else return this.props.children;
        
    }
    async onsubmit(event:any){
        event.preventDefault();
        if(this.state.id!=""){
            if(this.props.put){
                const request=await this.util.fetchRequest(this.props.put+this.state.id,'PUT',this.state.record);
                if(request!=null){
                    const status=await request.status;
                    const dataResp=await request.json;
                    this.statusResponse=status;
                    this.submissionResponse=dataResp;
                    this.setState({...this.state,statusResponse:status,submissionResponse:dataResp});
                }else this.setState({...this.state,statusResponse:400,submissionResponse:null});
            }
        }else
        {
            if(this.props.post){
                const request=await this.util.fetchRequest(this.props.post,'POST',this.state.record);
                if(request!=null){
                    const status=await request.status;
                    const dataResp=await request.json;
                    this.statusResponse=status;
                    this.submissionResponse=dataResp;
                    this.setState({...this.state,statusResponse:status,submissionResponse:dataResp});
                }else this.setState({...this.state,statusResponse:400,submissionResponse:null});
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
                this.formRender()
            }
            </form>
        )
    }
}