'use client'

import { useRef, useState } from "react";
import { RegularButton } from "../../buttons/RegularButton";
import Modal from "../Modal";
import { ModalContent } from "@/component-base/modal/ModalBase";
import { NextBase } from "@/NextBase";
interface Props extends ModalContent{
    buttonCaption:string;
    disabled?:boolean;
    variant?:string;
    deleteApi?:string;
}
export default function ModalDelete(props:Props){
    const modalRef=useRef<Modal|null>(null);
    const util = new NextBase();
    const [message,setMessage]=useState<string>("");

    const openModal=async()=>{
        const mod=modalRef.current;
        if(props.deleteApi&&message==""){
            const request=await util.fetchClientQuery(props.deleteApi,'delete');
            if(request.ok){
                
            }
        }
        mod?.open();
    }
    return(
        <div>
        <RegularButton caption={props.buttonCaption} type={'button'} disabled={props.disabled} onClick={openModal} variant={'light'}/>
        <Modal buttonChildren={<RegularButton caption="Delete"/>}  ref={modalRef} title={props.title} submitCaption={props.submitCaption} showSubmit={props.showSubmit} submit={props.submit} onClose={props.onClose}>
           {
               props.children
           }
           <p>{message}</p>
        </Modal>
        </div>
           
           
    )
}