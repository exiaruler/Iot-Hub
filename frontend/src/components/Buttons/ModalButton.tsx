'use client'
import ModalBox from "../modal/ModalBox";
import ConfirmButton from "./ConfirmButton";
import { ModalInterface } from "../modal/ModalProperties";
import { ReactNode } from "react";
import { ButtonComponent } from "../../component-base/ButtonComponent";
interface Props extends ModalInterface{
    buttonCaption:string;
    disabled?:boolean;
    form?:ReactNode;
}
export default class ModalButton extends ConfirmButton{
    declare props:Props;
    render(){
            return(
                <>
                <ButtonComponent caption={this.props.buttonCaption} onClick={this.openModal} disabled={this.props.disabled} size={undefined}/>
                
                <ModalBox hideSubmit={true} ref={this.modal} title={this.props.title} submitCaption={this.props.submitCaption} submit={this.submit} onClose={this.props.onClose}>
                {
                    this.props.children
                }
                </ModalBox>
                </>
            )
        }
}