'use client'
import { forwardRef, ReactNode, useState } from "react";
import { Alert, Form } from "react-bootstrap";
type Props={
    children:Array<ReactNode>;
}
const FormHandle=forwardRef(function FormHandle(props:Props,ref){
    const [formErrorMsg,setFormErrorMsg]=useState<any>({
          hide:true,
          error:""
        });
    
    return(
        <div>
        <Form>
        {
            
        }
        <Alert variant='warning' key='warning' hidden={formErrorMsg.hide}>
        {formErrorMsg.error}
        </Alert>
        </Form>
        </div>
    )
});
export default FormHandle;