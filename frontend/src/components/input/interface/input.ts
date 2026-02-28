import FormHandle from "../../form/FormHandle";

export interface InputInterface{
    label:string;
        type?:string|'text';
        name?:string;
        rows:number;
        required?:boolean;
        readOnly?:boolean;
        disable?:boolean;
        hidden?:boolean;
        onChange?:CallableFunction;
        warning?:string;
        value?:any;
        size?:any;
        md?:number;
        xs?:number;
        formRef?:FormHandle|any;
}
export interface State{
    value:any;
    warning:string;
}