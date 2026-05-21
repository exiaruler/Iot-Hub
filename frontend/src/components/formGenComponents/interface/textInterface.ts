import FormHandle from "../../../component-base/form/FormHandle";

export type Props={
    label:string;
    type:string;
    name?:string;
    rows:number;
    required?:boolean;
    readOnly?:boolean;
    disable?:boolean;
    onChange?:any;
    warning?:string;
    value?:any;
    size?:any;
    md?:number;
    xs?:number;
    formRef?:FormHandle|any;
}

export interface State{
    value:any
}