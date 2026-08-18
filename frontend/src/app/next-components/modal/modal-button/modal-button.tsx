'use client'

import { ModalContent } from "@/component-base/modal/ModalBase"
import Modal from "../Modal"
import { RegularButton } from "../../buttons/RegularButton"
import { useRef } from "react";
interface Props extends ModalContent{
    buttonCaption:string;
    disabled?:boolean;
    variant?:string;
}
export default function ModalButton(props:Props){
    const modalRef=useRef<Modal|null>(null);
    const openModal=()=>{
        const mod=modalRef.current;
        mod?.open();
    }
    return(
        <>
        <RegularButton  caption={props.buttonCaption} type={'button'} disabled={props.disabled} onClick={openModal} variant={'light'}/>
        <Modal ref={modalRef} title={props.title} buttonChildren={props.buttonChildren} submitCaption={props.submitCaption} showSubmit={props.showSubmit} submit={props.submit} onClose={props.onClose}>
        {
            props.children
        }
        </Modal>
        </>
    )
}