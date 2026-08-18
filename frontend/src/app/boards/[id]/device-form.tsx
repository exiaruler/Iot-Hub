'use client'

import Form from "@/app/next-components/form/Form"
import NewButton from "@/app/next-components/form/NewButton"
import TextInput from "@/app/next-components/input/TextInput"
import { ObjectRecord } from "@/app/next-components/layout/Content";
interface Props{
    boardId:string;
    submitHandle:CallableFunction;
    formLayout:ObjectRecord;
    children?:React.ReactNode;
    ref?: React.Ref<Form>;
}
// reuseable device form
export default function DeviceForm({boardId,submitHandle,formLayout,children,ref}:Props){
    
    return (
        <>
        <Form ref={ref} recordLayout={formLayout||{}} idKey={"id"} onSubmit={submitHandle} post={"/device/add-device/"+boardId} put={"/device/update-record/"}>
        <TextInput md={4} formRef={ref} label={"Name"} required={true} name={"name"} type={"text"} rows={0}/>
        <NewButton formRef={ref} caption={"Clear"} size={undefined} />
        {children}
        </Form>
        </>
    )
}