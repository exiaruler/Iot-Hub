'use client'
import { Button, ButtonGroup } from "react-bootstrap";
import { RegularButton } from "./RegularButton";

export default class DeleteButton extends RegularButton{
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