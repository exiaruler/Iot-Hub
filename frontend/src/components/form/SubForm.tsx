'use client'
import { Component, ReactNode } from "react";
import FormHandle from "./FormHandle";
type Props={
    children?:Array<ReactNode>;
    objectKey:string;
    array:boolean;
    onSubmit?:any;
    recordLayout:Object;
    record?:Object;
    post?:string;
    put?:string;
    idKey:string;
}
// represent objects of main form
export default class SubForm extends FormHandle{

    render(){
        return(
            <form>
            {
                //this.formRender()
            }
            </form>
        )
    }
}