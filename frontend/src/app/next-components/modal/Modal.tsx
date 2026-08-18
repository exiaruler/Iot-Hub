'use client'

import ModalBase from "@/component-base/modal/ModalBase";
import { Modal as BootstrapModal, ModalBody, ModalFooter, ModalHeader } from "react-bootstrap";
import { RegularButton } from "../buttons/RegularButton";

export default class Modal extends ModalBase{
    render(){
        return (
        <BootstrapModal show={this.state.show} onHide={()=>this.close()}>
        <ModalHeader>
        {this.props.title}
        </ModalHeader>
        <ModalBody>
        {
            this.props.children
        }
        </ModalBody>
        <ModalFooter>
        {
            this.props.buttonChildren
        }
        <RegularButton caption={"Close"} size={undefined} type={'button'} onClick={()=>this.close()}/>
        </ModalFooter>
        </BootstrapModal>
        )
    }
}