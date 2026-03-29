'use client'
import { forwardRef, use, useImperativeHandle, useRef, useState } from "react"
import { Modal, ModalBody, ModalFooter, ModalHeader } from "react-bootstrap";
import { RegularButton as ButtonComponent } from "../buttons/RegularButton";
//import UiBase from "../../base/UiBase";
import Content, { ContentRef } from "../layout/Content";
type modalContent={
    title:string,
    children?:any,
    buttonChildren?:any,
    submitCaption?:string,
    hideSubmit?:boolean,
    submit?:any,
    afterSubmit?:any,
    onClose?:any,
    deleteApi:string,
    baseUrl?:string,
    param:string|number
}
// modal box
const DeleteBox=forwardRef(function ModalBox(props:modalContent,ref){
    const [show,setShow]=useState(false);
    const [showSubmit,setShowSubmit]=useState(false);
    const contentRef=useRef<ContentRef>(null);
    //const base=new UiBase();
    var saveCaption='Delete';

    const open=()=>{
        setShow(true);
    }
    const close=()=>{
        if(props.onClose){
            props.onClose();
        }
        setShow(false);
    }
    const submitHandle=async(event:any)=>{
        event.preventDefault();
        var baseUrl="";
        if(props.baseUrl){
            baseUrl=props.baseUrl;
        }
        if(props.submit){
            props.submit();
        }else
        {
            const request=await contentRef.current!.util.fetchClient(props.deleteApi+props.param,'DELETE');
            if(props.afterSubmit){
                const res=request.json;
                props.afterSubmit(res);
            }

        }
    }
    if(props.submitCaption){
        saveCaption=props.submitCaption;
    }
    if(props.hideSubmit){
        setShowSubmit(props.hideSubmit);
    }
    useImperativeHandle(ref,()=>{
        return {
            open,
            close
        }
    },[]);
    return (
        <Modal show={show} onHide={close}>
        <Content ref={contentRef}>
        <form onSubmit={submitHandle}>
        <ModalHeader>
        {props.title}
        </ModalHeader>
        <ModalBody>
        {
            props.children
        }
        </ModalBody>
        <ModalFooter>
        <ButtonComponent caption={"Close"} size={undefined} type={'button'} onClick={close}/>
        {!showSubmit?
        <ButtonComponent variant="danger" caption={saveCaption} size={undefined} type={'submit'}/>
        :null}
        </ModalFooter>
        </form>
        </Content>
        </Modal>
    )
});
export default DeleteBox;