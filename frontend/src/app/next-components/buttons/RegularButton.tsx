'use client'
import { ButtonComponent } from "@/component-base/ButtonComponent";
import { Button, ButtonGroup } from "react-bootstrap";
export class RegularButton extends ButtonComponent{
    
    render(){
        return(
        <ButtonGroup className="Button-Regular">
        <Button variant={this.props.variant||"light"} onClick={this.props.onClick} type={this.props.type} size={this.props.size} disabled={this.props.disabled}>
        {this.props.caption} 
        </Button> 
        </ButtonGroup>  
        );
    }
}