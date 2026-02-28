'use client'
import Form from "@/app/next-components/form/Form"
import { Component, createRef, ReactNode, Ref, RefObject, useEffect, useRef } from "react"
import TextInput from "@/app/next-components/input/TextInput";
import SaveButton from "@/app/next-components/buttons/SaveButton";
import CheckBoxInput from "@/app/next-components/input/CheckBoxInput";
import { Row } from "react-bootstrap";
import Content from "@/app/next-components/layout/Content";
import { NextUIBase } from "@/NextUIBase";
interface Props{
    submissionHandle:CallableFunction;
    record:Record<string,any>|null;
    modalRef:any;
}
export default function ConfigForm(props:Props){
    const formRef=useRef<Form>(null);
    const inputFields=useRef<Array<Component>>([]);
    const uiBase=new NextUIBase();

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
    const addElement=(element:Component|null)=>{
        const fields=uiBase.addInputRefComponent(inputFields,element);
        inputFields.current=fields.current;
    }
    useEffect(()=>{
       uiBase.forceUpdateRefComponents(inputFields);
    },[])
    return(
        <div>
        <Content>
        <Row>
        <Form record={props.record} ref={formRef} onSubmit={handleUpdate} recordLayout={{
                    "id": 0,
                    "createdDate": "2026-02-11T02:43:30.2290526",
                    "updatedDate": "2026-02-11T02:43:30.2290526",
                    "boardId": null,
                    "boardKey": null,
                    "name": null,
                    "ip": null,
                    "status": false,
                    "arest": false,
                    "arestCommand": false,
                    "socket": false,
                    "periodicCheck": 60000,
                    "ramUsage": 0,
                    "activated": false,
                    "websocketId": "",
                    "devMode": false,
                    "lastConnectDate": null,
                    "lastConnectTime": null,
                    "lastConnectDateTime": null,
                    "timeout": 0,
                    "restartTimeout": false,
                    "tasksExecuted": 0,
                    "device": [],
                    "hardwardId": 0
                }} idKey={"id"} put="/board/update-board/">
                <TextInput ref={(element)=>addElement(element)} formRef={formRef} name={"name"} label={"Board Name"} rows={0}/>
                <CheckBoxInput ref={(element)=>addElement(element)} formRef={formRef} name={"devMode"} label={"Dev Mode"} rows={0}/>
                <SaveButton caption={"Save"} size={undefined}/>
                </Form>
                </Row>
            </Content>
        </div>
    )
}