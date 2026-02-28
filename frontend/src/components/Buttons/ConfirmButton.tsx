'use client'
import { RegularButton } from "@/app/next-components/buttons/RegularButton";
import { Component, createRef, RefObject } from "react";
import ModalBox from "../modal/ModalBox";
import { ModalInterface } from "../modal/ModalProperties";
interface Props extends ModalInterface{
    buttonCaption:string;
    disabled?:boolean
}

export default class ConfirmButton extends Component<Props>{
    modal: RefObject<ModalBox|null>;
    constructor(props:Props) {
        super(props);
        this.modal = createRef();
    }
    openModal=()=>{
        const mod=this.modal.current;
        mod?.open();
    }
    closeModal=()=>{
        const mod=this.modal.current;
        mod?.close();
    }
    submit=()=>{
        const mod=this.modal.current;
        if(this.props.submit){
            this.props.submit();
        }
        mod?.close();
    }
    render(){
        return(
            <>
            <RegularButton caption={this.props.buttonCaption} size={undefined} onClick={this.openModal} disabled={this.props.disabled}/>
            <ModalBox ref={this.modal} title={this.props.title} submitCaption={this.props.submitCaption} submit={this.submit} onClose={this.props.onClose}>
            {
                this.props.children
            }
            </ModalBox>
            </>
        )
    }
}