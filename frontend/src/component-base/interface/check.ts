import FormHandle from "../../components/form/FormHandle";
import { InputInterface } from "./input";

export interface CheckInterface extends InputInterface{
    label:string;
    type:string;
    name?:string;
    required?:boolean;
    onChange?:any;
    warning?:string;
    value?:boolean;
    size:any;
    formRef?:FormHandle|any;
}
export interface State{
    value:boolean;
}