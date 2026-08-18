'use client'
import SaveButton from "@/app/next-components/buttons/SaveButton";
import Form from "@/app/next-components/form/Form"
import NewButton from "@/app/next-components/form/NewButton";
import TextInput from "@/app/next-components/input/TextInput"
import Content, { ObjectRecord } from "@/app/next-components/layout/Content";
import { useRef } from "react"
import DeviceForm from "./device-form";

interface Props{
    boardId:string;
    onUpdate?:CallableFunction;
    formLayout:ObjectRecord;
}
export default function AddForm(props:Props){
    const formRef=useRef<Form>(null);

    const submitHandle=()=>{
        const form=formRef.current;
        if(form?.statusResponse==200){
            const data=form.submissionResponse;
            if(props.onUpdate) props.onUpdate(data);
            form.newRecord();
        }
    }

    return (
        <div>
        <Content>
        <DeviceForm ref={formRef} boardId={props.boardId} formLayout={{id:0,name:""}} submitHandle={submitHandle}>
        <SaveButton caption={"Add Device"} />
        </DeviceForm>
        </Content>
        </div>
    )
}