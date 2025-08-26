import { ReactNode } from "react";

export default interface Props{
    title:any;
    eventKey:any;
    childen?:Array<any>;
    hidden?:boolean;
    disabled?:boolean;
}