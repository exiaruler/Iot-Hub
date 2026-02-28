import { MouseEventHandler } from "react";

export interface ButtonInterface{
    id?:any;
    caption:string;
    variant?:string;
    onClick?:MouseEventHandler;
    size:any;
    active?:boolean;
    disabled?:boolean;
    type?: "button" | "submit" | "reset" | undefined;
    hidden?:boolean;
    tabIndex?:number;
}