'use client'
import { Component } from "react";

interface WarningProps{
    warning?:string;
    name:string;
}
export default class WarningBase extends Component<WarningProps>{
    warning=this.props.warning||"";
    
    public setWarning(messsage:string):void{
        this.warning=messsage;
    }
    
}