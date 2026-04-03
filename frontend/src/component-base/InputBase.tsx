'use client'
import { Component, createRef, useContext } from "react";
//import { InputInterface,State } from "./interface/input";
import { InputInterface,State } from "./interface/input";
import { RecordContext } from "@/components/form/FormHandle";
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
            let form=this.props.formRef.current;
            if(this.props.name&&form!=null&&JSON.stringify(form.state.record)!=='{}'&&form.state.record!=null){
                this.value=form.state.record[this.props.name];
                const value=this.context as Record<string, any>;
                return value[this.props.name];
            }
        }else if(this.props.value) this.value=this.props.value
        return this.value;
    }
    public getWarning():string{
        if(this.state.warning!==""){
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
            let prevCurr=prevProps.formRef.current||this.props.formRef.current;
            let curr=this.props.formRef.current;
            if(JSON.stringify(curr.props.record)!=='{}'&&curr.props.record!=null){
                let pastId=prevCurr.state.id;
                let currId=curr.props.record[prevCurr.props.idKey];
                if(pastId!==currId){
                    let val=curr.props.record[this.props.name];
                    this.value=val;
                }

            }else{
                let val=curr.props.recordLayout[this.props.name];
                this.value=val;
            }
        }
    }
    // formRef value set upon component did mount
    public formHandleValueSet():void{
        if(this.props.formRef&&this.props.name){
            let curr=this.props.formRef.current;
            if(curr!=null&&this.warningComponent.current!=null) curr.addInput(this.warningComponent);
            if(curr!=null&&curr.props.record!=null&&Object.keys(curr.props.record).length>0){
                let val=curr.props.record[this.props.name];
                this.value=val;
            }
            else if(curr!=null&&this.props.name)
            {
                let val=curr.props.recordLayout[this.props.name];
                this.value=val;
            }
        }
    }
    
    public onChange(event:React.ChangeEvent<HTMLInputElement>):void{
        let value=event.target.value;
        this.value=event.target.value;
        this.setState({...this.state,value:value});
        if(this.props.formRef){
            let form=this.props.formRef.current;
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
            let form=this.props.formRef.current;
            form.onChangeRecord(this.props.name,value);
        }
        if(this.props.onChange){
            this.props?.onChange(value);
        }
    }

} 