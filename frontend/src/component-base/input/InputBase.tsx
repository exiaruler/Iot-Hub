'use client'
import { Component, createRef } from "react";
import { InputInterface,State } from "../interface/input";
import { FormContext, RecordContext } from "@/component-base/form/FormHandle";
// input base handlers
export default class InputBase extends Component<InputInterface,State>{
    public warningComponent:any;
    constructor(props:InputInterface) {
        super(props);
        this.warningComponent=createRef();
        this.state = {
            value:"",
            warning:""
        };
    }
    public value=this.props.value;
    public warning:string=this.props.warning||"";
    public name=this.props.name||"";
    public type=this.props.type;
    public static context=RecordContext;
    
    componentDidMount(): void {
        if(this.props.value){
            this.setState({...this.state,value:this.props.value});
            this.value=this.props.value;
        }
        this.formHandleValueSet();
    }
    
    componentDidUpdate(prevProps: Readonly<InputInterface>, prevState: Readonly<State>, snapshot?: any): void {
        this.formHandleValueSetUpdate(prevProps);
    }
    
    // return state value
    public getStateValue():any{
        if(this.props.formRef){
            const form=this.props.formRef.current;
            if(this.props.name&&form!=null&&JSON.stringify(form.state.record)!=='{}'&&form.state.record!=null){
                this.value=form.state.record[this.props.name];
                const context=this.context as FormContext;
                const rec=context.record;
                if(rec!=null){
                    return rec[this.props.name];
                }
            }
        }else if(this.props.value) this.value=this.props.value
        return this.value;
    }
    public getWarning():string{
        if(this.props.formRef&&this.props.name){
            const context=this.context as FormContext;
            const warnings=context.warnings;
            if(warnings!=null){
                const warn=warnings[this.props.name];
                return warn;
            } return "";
        } else if(this.state.warning!==""){
            return this.state.warning;
        }else return this.warning;
    }
    
    public setWarningState(warning:string):void{
        this.setState({...this.state,warning:warning});
    }
    public setWarning(warning:string):void{
        this.warning=warning;
    }
    // return error value
    public formHandleValueSetUpdate(prevProps: Readonly<InputInterface>):void{
        if(prevProps.formRef&&this.props.formRef&&this.props.name){
            const prevCurr=prevProps.formRef.current||this.props.formRef.current;
            const curr=this.props.formRef.current;
            if(JSON.stringify(curr.props.record)!=='{}'&&curr.props.record!=null){
                const pastId=prevCurr.state.id;
                const currId=curr.props.record[prevCurr.props.idKey];
                if(pastId!==currId){
                    const val=curr.props.record[this.props.name];
                    this.value=val;
                }

            }else{
                const val=curr.props.recordLayout[this.props.name];
                this.value=val;
            }
        }
    }
    // formRef value set upon component did mount
    public formHandleValueSet():void{
        if(this.props.formRef&&this.props.name){
            const curr=this.props.formRef.current;
            if(curr!=null&&this.warningComponent.current!=null) curr.addInput(this.warningComponent);
            if(curr!=null&&curr.props.record!=null&&Object.keys(curr.props.record).length>0){
                const val=curr.props.record[this.props.name];
                this.value=val;
            }
            else if(curr!=null&&this.props.name)
            {
                const val=curr.props.recordLayout[this.props.name];
                this.value=val;
            }
        }
    }
    
    public onChange(event:React.ChangeEvent<HTMLInputElement>):void{
        const value=event.target.value;
        this.value=event.target.value;
        this.setState({...this.state,value:value});
        if(this.props.formRef){
            const form=this.props.formRef.current;
            form.onChangeRecord(this.props.name,value);
        }
        if(this.props.onChange){
            this.props?.onChange(event);
        }
    }
    public onChangeValue(value:string):void{
        this.value=value;
        this.setState({...this.state,value:value});
        if(this.props.formRef){
            const form=this.props.formRef.current;
            form.onChangeRecord(this.props.name,value);
        }
        if(this.props.onChange){
            this.props?.onChange(value);
        }
    }

} 