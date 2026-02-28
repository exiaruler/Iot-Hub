import { useState } from "react";
import { Alert, Button, Col, Form, Row } from "react-bootstrap";
import { FormGenText } from "../../components/formGenComponents/FormGenText";
import { SaveButton } from "../../components/Buttons/SaveButton";
import { useDispatch, useSelector } from "react-redux";
import { setUser,getUser } from "../../redux/slice/loginSlice";
import UiBase from "../../base/UiBase";
import { useNavigate } from "react-router-dom";
import Group from "../../components/Group";
export default function Login(){
    const base=new UiBase();
    const dispatch = useDispatch();
    const nav=useNavigate();
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
        try{
          const login=await base.userApi.login(form);
          if(login.message==="Successfully Authenticated"){
            dispatch(setUser(Object(login)));
            let timeout=base.util.addMillsToCurrent(login.timeout);
            document.cookie="id="+login.id+"; expires="+timeout;
            nav("/");
            //window.location.href="/";
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
        <Group>
        <Row >
        <Col xs={3} md={5}>
        </Col>
        <Col xs={9} md={3}>
        <Form onSubmit={submit}>
        <FormGenText label={"Username"} type={"string"} name={"username"} rows={0} required={false} onChange={(event: any) => base.onChange(event.target.name, event.target.value, setForm, form)} warning={formWarning.username}  size={'60%'} />
        <FormGenText label={"Password"} type={"password"} name={"password"} rows={0} required={false} onChange={(event: any) => base.onChange(event.target.name, event.target.value, setForm, form)} warning={formWarning.password}  size={'60%'} />
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
        </div>
    )
}