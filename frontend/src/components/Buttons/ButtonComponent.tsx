'use client'
import { Component, MouseEventHandler } from "react";
import { Button, ButtonGroup } from "react-bootstrap";
export type Props={
    id?:any;
    caption:string;
    variant?:string;
    onClick?:MouseEventHandler;
    size:any;
    active?:boolean;
    disabled?:boolean;
    type:any;
    tabIndex?:number;
}
export class ButtonComponent extends Component<Props>{
    constructor(props:Props) {
        super(props);
    }
    render(){
        return(
        <ButtonGroup className="Button-Regular">
        <Button variant={this.props.variant||"primary"} onClick={this.props.onClick} type={this.props.type} size={this.props.size} tabIndex={this.props.tabIndex} disabled={this.props.disabled}>
        {this.props.caption} 
        </Button> 
        </ButtonGroup>  
        );
    }
}