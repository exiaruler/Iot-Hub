'use client'

import { RegularButton } from "@/app/next-components/buttons/RegularButton";
import ModalBox from "../modal/ModalBox";
import ConfirmButton from "./ConfirmButton";
import { ModalInterface } from "../modal/ModalProperties";
import { ReactNode } from "react";
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
                <RegularButton caption={this.props.buttonCaption} size={undefined} onClick={this.openModal} disabled={this.props.disabled}/>
                
                <ModalBox hideSubmit={true} ref={this.modal} title={this.props.title} submitCaption={this.props.submitCaption} submit={this.submit} onClose={this.props.onClose}>
                {
                    this.props.children
                }
                </ModalBox>
                </>
            )
        }
}