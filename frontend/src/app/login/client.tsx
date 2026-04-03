'use client'

import UiBase from "@/base/UiBase";
import { SaveButton } from "@/components/Buttons/SaveButton";
import { FormGenText } from "@/components/formGenComponents/FormGenText";
import Group from "@/components/Group";
import { setUser } from "@/redux/slice/loginSlice";
import { useRef, useState } from "react";
import { Row, Col, Form, Alert } from "react-bootstrap";
import { useDispatch } from "react-redux";
import Content, { ContentRef } from "../next-components/layout/Content";

export default function Client(){
const base=new UiBase();
    const dispatch = useDispatch();
    const contentRef=useRef<ContentRef>(null);
    const [form,setForm]=useState({
        username:"",
        password:""
    });
    const [formWarning,setFormWarnings]=useState({
      username:"",
      password:""
  });
    const [formErrorMsg,setFormErrorMsg]=useState<any>({
      hide:true,
      error:""
    });
    
    const submit=async(event:any)=>{
        event.preventDefault();
        const content=contentRef.current;
        try{
          const login=await base.userApi.loginRemote(form);
          if(login.message==="Successfully Authenticated"){
            dispatch(setUser(Object(login)));
            let timeout=base.util.addMillsToCurrent(login.timeout);
            document.cookie=`id=${login.id}; expires=${timeout.toUTCString()}; path=/; SameSite=Lax`;
            localStorage.setItem("login",login.token);
            window.location.href="/";
          }else
          {
            const error:any=login;
            setFormWarnings({
              username:"",
              password:""
            });
            setFormWarnings({
              username:error.fields[0].username,
              password:error.fields[1].password
            });
            if(error.error!==""){
              setFormErrorMsg({
                hide:false,
                error:error.error
              });
            }else{
              setFormErrorMsg({
                hide:true,
                error:error.error
              });
            }

          }
        }catch(err){

        }
      }
    return(
        <div>
        <Content ref={contentRef}>
        <Group>
        <Row >
        <Col xs={3} md={5}>
        </Col>
        <Col xs={9} md={3}>
        <Form onSubmit={submit}>
        <FormGenText label={"Username"} type={"string"} name={"username"} rows={0} required={false} onChange={(event: any) => base.onChange(event.target.name, event.target.value, setForm, form)} warning={formWarning.username} value={""} size={'60%'} />
        <FormGenText label={"Password"} type={"password"} name={"password"} rows={0} required={false} onChange={(event: any) => base.onChange(event.target.name, event.target.value, setForm, form)} warning={formWarning.password} value={""} size={'60%'} />
        <Alert variant='warning' key='warning' hidden={formErrorMsg.hide}>
        {formErrorMsg.error}
        </Alert>
        <SaveButton id={""} caption={"Login"} variant={"primary"} size={"lg"} active={false} disabled={false} type={"submit"} /> 
        </Form>
        </Col>
        <Col>
        </Col>
        </Row>
        </Group>
        </Content>
        </div>
    )
}