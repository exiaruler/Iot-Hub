'use client'
import Form from "@/app/next-components/form/Form"
import { Component, createRef, ReactNode, Ref, RefObject, useEffect, useRef, useState } from "react"
import TextInput from "@/app/next-components/input/TextInput";
import SaveButton from "@/app/next-components/buttons/SaveButton";
import CheckBoxInput from "@/app/next-components/input/CheckBoxInput";
import { Row } from "react-bootstrap";
import Content, { ObjectRecord } from "@/app/next-components/layout/Content";
import Dev from "@/app/next-components/user/dev";
interface Props{
    submissionHandle:CallableFunction;
    record:ObjectRecord;
    formLayout:ObjectRecord;
    modalRef:any;
}
export default function ConfigForm(props:Props){
    const formRef=useRef<Form>(null);
    const [devShow,setDevShow]=useState(false);
    const devCheckRef=useRef<CheckBoxInput>(null);

    const handleUpdate=()=>{
        const modal=props.modalRef.current;
        const form=formRef.current;
        if(form?.statusResponse==200){
            const rec=form.submissionResponse;
            form.setRecord(rec);
            props.submissionHandle(rec);
            modal?.closeModal();
        }
    }
    const showDev=()=>{
        const check=devCheckRef.current;
        const val=check?.value;
        setDevShow(val);
    }
    const showDevForm=()=>{
        setDevShow(props.record?.devMode);
    }
   
    
    useEffect(()=>{
       showDevForm();
    },[setDevShow])
    return(
        <div>
        <Content>
        <Row>
        <Form record={props.record} ref={formRef} onSubmit={handleUpdate} recordLayout={props.formLayout||{}} idKey={"id"} put="/board/update-board/">
                <TextInput  formRef={formRef} name={"name"} label={"Board Name"} rows={0}/>
                <CheckBoxInput onChange={showDev} ref={devCheckRef}  formRef={formRef} name={"devMode"} label={"Dev Mode"} rows={0}/>
                {devShow?
                <TextInput  formRef={formRef} name={"devUrl"} label={"URL"} rows={0}/>
                :null}
                {devShow?
                <TextInput  formRef={formRef} name={"devWebsocketHost"} label={"Websocket Host"} rows={0}/>
                :null}
                <CheckBoxInput  formRef={formRef} name={"restartTimeout"} label={"Restart Timeout"} rows={0}/>
                <SaveButton caption={"Save"} size={undefined}/>
                </Form>
                </Row>
            </Content>
        </div>
    )
}