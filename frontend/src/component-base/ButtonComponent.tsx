'use client'
import { Component, MouseEventHandler } from "react";
import { Button, ButtonGroup } from "react-bootstrap";
export type Props={
    id?:any;
    caption:string;
    variant?:string;
    onClick?:MouseEventHandler;
    size?:any;
    active?:boolean;
    disabled?:boolean;
    type?: "button" | "submit" | "reset" | undefined;
    hidden?:boolean;
    tabIndex?:number;
}
export interface ButtonProps{
    id?:any;
    caption:string;
    variant?:string;
    onClick?:(event: React.MouseEvent<HTMLButtonElement>) => void;
    size?:any;
    active?:boolean;
    disabled?:boolean;
    type?: "button" | "submit" | "reset" | undefined;
    hidden?:boolean;
    tabIndex?:number;
}
export class ButtonComponent extends Component<ButtonProps>{
    
    caption=this.props.caption;
    variant=this.props.variant||"primary";

   public onClick(event:React.MouseEvent<HTMLButtonElement>):void{
        if(this.props.onClick){
            this.props.onClick(event);
        }
   }
    
    render(){
        return(
        <ButtonGroup className="Button-Regular">
        <Button variant={this.variant} onClick={(event:React.MouseEvent<HTMLButtonElement>)=>this.onClick(event)} type={this.props.type||"button"} size={this.props.size} tabIndex={this.props.tabIndex} disabled={this.props.disabled} hidden={this.props.hidden}>
        {this.caption} 
        </Button> 
        </ButtonGroup>  
        );
    }
}