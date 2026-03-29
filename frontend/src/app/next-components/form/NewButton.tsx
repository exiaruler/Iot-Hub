'use client'
import { RegularButton } from "../buttons/RegularButton"
import { ButtonGroup, Button } from "react-bootstrap"
import Form from "./Form"
import { ButtonInterface } from "@/component-base/ButtonInterface"

interface Props extends ButtonInterface{
    formRef:Form|any;
}
export default class NewButton extends RegularButton{
    declare props:Props

    clearForm(event:React.MouseEvent<HTMLButtonElement>){
        if(this.props.formRef){
            const ref=this.props.formRef.current;
            ref.newRecord();
        }
        if(this.props.onClick){
            this.props.onClick(event);
        }
    }
    render(){
        return(
        <ButtonGroup className="Button-Regular">
        <Button variant={this.props.variant||"light"} onClick={(event:React.MouseEvent<HTMLButtonElement>)=>this.clearForm(event)} type={"button"} size={this.props.size} disabled={this.props.disabled}>
        {this.props.caption||"New"} 
        </Button> 
        </ButtonGroup>  
        );
    }
}