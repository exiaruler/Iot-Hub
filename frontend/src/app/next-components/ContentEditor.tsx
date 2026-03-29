'use client'
import { useEffect, useRef, useState } from "react";
import FormHandle from "@/components/form/FormHandle";
import ContentInput from "@/components/input/ContentInput";
import { SaveButton } from "@/components/Buttons/SaveButton";
import { useSelector,useDispatch} from 'react-redux';
import { setContent as contentSet,getAppId,getContents } from "../../redux/slice/appSlice";
import { getLoginState } from "../../redux/slice/loginSlice";
import { ButtonComponent } from "../../component-base/ButtonComponent";
import HTMLRender from "@/components/content/HTMLRender";
import { FormGenTextArea } from "@/components/formGenComponents/FormGenTextArea";
import { usePathname } from 'next/navigation';
interface ContentEditorProps{
    debug?:boolean;
}
// content editor component
export default function ContentEditor(props:ContentEditorProps){
    var path=usePathname();
    const dispatch=useDispatch();
    const login=useSelector(getLoginState);
    const [edit,setEdit]=useState(false);
    const [preview,setPreview]=useState("");
    const [viewPreview,setViewPreview]=useState(false);
    const appId=useSelector(getAppId);
    var contents=useSelector(getContents);
    const [content,setContent]=useState("");
    const [contentRecord,setContentRecord]=useState(null);
    var contentId="";
    const formRef=useRef<FormHandle>(null);
    const editRef=useRef<ContentInput>(null);
    const editRefDebug=useRef<FormGenTextArea>(null);

    const openEditor=(action:boolean):void=>{
        setEdit(action);
    }
    const openPreview=(action:boolean):void=>{
        let editComp=editRef.current;
        let editCompDebug=editRefDebug.current;
        setViewPreview(action);
        if(editComp!=null){
            if(action) setPreview(editComp.value);
            if(!action) setPreview("");
        }
        if(editCompDebug!=null){
            if(action) setPreview(editCompDebug.value);
            if(!action) setPreview("");
        }
    }
    const getContent=():void=>{
        let cont=contents.find((con:Record<string,any>)=>con.pathname===path);
        const form=formRef.current;
        let editComp=editRef.current;
        let editCompDebug=editRefDebug.current;
        let rec=form?.getRecord();
        if(contents.length>0){
            if(cont!=null){
                setContent(cont.content);
                setContentRecord(cont);
                if(rec==null&&form){
                    form.setRecord(cont);
                    editComp?.forceUpdate();
                    editCompDebug?.forceUpdate();
                }
                contentId=cont._id;
            }
        }
    }
    const submit=()=>{
        const form=formRef.current;
        if(form?.ok){
            let record=form.submissionResponse;
            setContent(record.content);
            dispatch(contentSet(record));
            openEditor(false);
        }
    }
    useEffect(()=>{
        path=window.location.pathname;
        getContent();
    });
    return (
        <div>
        {login&&!edit?
        <ButtonComponent caption={"Edit Content"} size={undefined} onClick={()=>openEditor(true)} />
        :null}
        {!edit?
        <div id="Content-Display">
        <HTMLRender html={content}/>
        </div>
        :null}
        {login&&edit&&viewPreview?
        <div>
        <ButtonComponent caption={"Close Preview"} size={undefined} onClick={()=>openPreview(false)}/>
        <HTMLRender html={preview}/>
        </div>
        :null}
        {login&&edit?
        <div hidden={viewPreview}>
        <FormHandle ref={formRef} record={contentRecord} recordLayout={{content:"",_id:"",createdAt:'',updatedAt:'',pathname:path,application:null}} idKey={'_id'} post={"/content/add-content/"+appId} put={"/content/update-content/"+contentId} onSubmit={submit}>
        {!props.debug?
        <ContentInput ref={editRef} formRef={formRef} name={"content"} label={""} type={""} rows={0}/>
        :null}
        {props.debug?
        <FormGenTextArea ref={editRefDebug} formRef={formRef} label={""} name={"content"} type={""} rows={20}/>
        :null}
        <ButtonComponent onClick={() => openEditor(false)} caption={"Close Editor"} size={undefined}/>
        <ButtonComponent caption={"Preview"} size={undefined} onClick={()=>openPreview(true)}/>
        <SaveButton caption={""} size={undefined}/>
        </FormHandle>
        </div>
        :null}
        
        </div>
    );
}