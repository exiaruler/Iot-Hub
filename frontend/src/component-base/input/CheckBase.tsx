'use client'
import { CheckInterface as InputInterface,State } from "../interface/check";
import InputBase from "./InputBase";
import { FormContext } from "@/component-base/form/FormHandle";
// checkbox input
export default class CheckBase extends InputBase{
    //declare props:InputInterface;
    //declare state:State;
    constructor(props:InputInterface) {
        super(props);
        this.state = {
            value:false,
            warning:""
        };
    }

   // return state value
    public getStateValue():any{
        if(this.props.formRef){
            const form=this.props.formRef.current;
            if(this.props.name&&form!=null&&JSON.stringify(form.state.record)!=='{}'&&form.state.record!=null){
                const context=this.context as FormContext;
                const rec=context.record;
                if(rec!=null){
                    return rec[this.props.name];
                }
            }
        }else return this.value;
    }

    public onChange(event:React.ChangeEvent<HTMLInputElement>){
        const value=event.target.checked;
        this.setState({...this.state,value:value});
        this.value=value;
        if(this.props.formRef){
            var form=this.props.formRef.current;
            form.onChangeRecord(this.props.name,value);
        }
        if(this.props.onChange){
            this.props.onChange(event);
        }
    }
    

} 