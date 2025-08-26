'use client'
import { Button, ButtonGroup } from "react-bootstrap";
import { RegularButton } from "./RegularButton";
import { MouseEventHandler } from "react";
type props={
    id?:any,
    caption:string,
    variant?:string,
    onClick?:MouseEventHandler,
    size:any,
    active?:boolean,
    disabled?:boolean,
    tabIndex?:number
}
export default class SaveButton extends RegularButton{
    render(){
        return(
        <ButtonGroup className="Button-Regular">
        <Button variant={this.props.variant||"light"} onClick={this.props.onClick} type={"submit"} size={this.props.size} disabled={this.props.disabled}>
        {this.props.caption} 
        </Button> 
        </ButtonGroup>  
        );
    }
}