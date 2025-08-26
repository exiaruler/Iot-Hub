'use client'
import { type } from "os";
import { ChangeEventHandler, Component, useRef } from "react";
import { Col, Form, Row } from "react-bootstrap";
import FormHandle from "../form/FormHandle";
type Props={
    label:string,
    type:string,
    name?:string,
    required?:boolean,
    onChange?:any
    warning?:string,
    value?:boolean,
    size:any,
    formRef?:FormHandle|any;
}
type State={
    value:boolean;
}
export class FormGenCheckBox extends Component<Props,State>{
    public componentId=this.props.name+"Check";
    constructor(props:Props) {
        super(props);
        this.state = {
            value:false
        };
    }
    onChange(event:React.ChangeEvent<HTMLInputElement>){
        var value=event.target.checked;
        this.setState({...this.state,value:value});
        if(this.props.formRef){
            var form=this.props.formRef.current;
            form.onChangeRecord(this.props.name,value);
        }
        if(this.props.onChange){
            this.props.onChange;
        }
    }
   
    render(){
        return(
            <Col md="3">
            <div className="mb-3">
            <Form.Group>
            <Form.Check id={this.componentId} required={this.props.required} reverse={true} label={this.props.label} defaultChecked={this.props.value} onChange={(event:React.ChangeEvent<HTMLInputElement>)=>this.onChange(event)} name={this.props.name} />
            <Form.Text id={this.props.name+"Warning"}>{this.props.warning} </Form.Text>
            </Form.Group>
            </div>
            </Col>
        );
    }
}