'use client'
import { Component, MouseEventHandler } from "react"
import { RegularButton } from "../buttons/RegularButton"
import { ButtonGroup, Button } from "react-bootstrap"
import { Props as OgProps } from "@/components/Buttons/ButtonComponent"
import { ButtonComponent } from "@/components/Buttons/ButtonComponent"
import Form from "./Form"
type Props={
    id?:any;
    caption:string;
    variant?:string;
    onClick?:MouseEventHandler;
    size:any;
    active?:boolean;
    disabled?:boolean;
    type:any;
    tabIndex?:number;
    formRef:Form;
}
export default class NewButton extends RegularButton{
    constructor(props:Props) {
        super(props);
    }
    clearForm(event:any){
        if(this.props.formRef){
            var ref=this.props.formRef.current;
            ref.newRecord();
        }
        if(this.props.onClick){
            this.props.onClick(event);
        }
    }
    render(){
        return(
        <ButtonGroup className="Button-Regular">
        <Button variant={this.props.variant||"light"} onClick={(event)=>this.clearForm(event)} type={"button"} size={this.props.size} disabled={this.props.disabled}>
        {this.props.caption||"New"} 
        </Button> 
        </ButtonGroup>  
        );
    }
}