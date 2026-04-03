'use client'
import { CheckInterface as InputInterface,State } from "./interface/check";
import InputBase from "./InputBase";
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
            let form=this.props.formRef.current;
            if(this.props.name&&form!=null&&JSON.stringify(form.state.record)!=='{}'&&form.state.record!=null){
                const value=this.context as Record<string, any>;
                return value[this.props.name];
            }
        }else return this.value;
    }

    public onChange(event:React.ChangeEvent<HTMLInputElement>){
        let value=event.target.checked;
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