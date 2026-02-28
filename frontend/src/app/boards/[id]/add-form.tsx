'use client'
import SaveButton from "@/app/next-components/buttons/SaveButton";
import Form from "@/app/next-components/form/Form"
import NewButton from "@/app/next-components/form/NewButton";
import TextInput from "@/app/next-components/input/TextInput"
import { useRouter } from 'next/navigation';
import { useRef } from "react"

interface Props{
    boardId:string;
    onUpdate:CallableFunction;
}
export default function AddForm(props:Props){
    const formRef=useRef<Form>(null);
    const router = useRouter();
    const submitHandle=()=>{
        const form=formRef.current;
        if(form?.statusResponse==200){
            const data=form.submissionResponse;
            props.onUpdate(data);
            form.newRecord();
            window.location.reload();
        }
    }
    return (
        <div>
        <Form ref={formRef} recordLayout={{id:0,name:""}} idKey={"id"} onSubmit={submitHandle} post={"/device/add-device/"+props.boardId}>
        <TextInput md={4} formRef={formRef} label={"Name"} required={true} name={"name"} type={"text"} rows={0}/>
        <NewButton formRef={formRef} caption={"Clear"} size={undefined}/>
        <SaveButton caption={"Add Device"} size={undefined}/>
        </Form>
        </div>
    )
}