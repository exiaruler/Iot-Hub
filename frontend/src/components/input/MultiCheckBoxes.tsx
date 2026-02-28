'use client'
import { Component } from "react";
import { Row, Col, Form, Stack } from "react-bootstrap";
import FormHandle from "../form/FormHandle";
import { FormGenCheckBox } from "../formGenComponents/FormGenCheckBox";
type Props={
    label:string;
    type:string;
    name?:string;
    rows:number;
    required?:boolean;
    readOnly?:boolean;
    disable?:boolean;
    onChange?:any;
    warning?:string;
    value:string;
    size?:any;
    md?:number;
    xs?:number;
    formRef?:FormHandle|any;
    multiSelect?:boolean;
    children?:Array<FormGenCheckBox>;
}
export default class MultiCheckBoxes extends Component<Props>{
    constructor(props:Props) {
            super(props);
            this.state = {
            };
    }
    public multiSelect=this.props.multiSelect;

    onChange(event:React.ChangeEvent<HTMLInputElement>){
        var value=event.target.value;
        this.setState({...this.state,value:value});
                
        if(this.props.formRef){
            var form=this.props.formRef.current;
            form.onChangeRecord(this.props.name,value);
        }
        
        if(this.props.onChange){
            this.props.onChange()
        }
    }
    render(){
        return(
            <Row>
            <Col xs={this.props.xs} md={this.props.md}>
            <Form.Group>
            <Form.Label>{this.props.label}</Form.Label>
            <div className="mb-3">
            <Stack direction="horizontal" gap={2}>
        
            </Stack>
            </div>
            <Form.Text id={this.props.name+"Warning"} >{this.props.warning} </Form.Text>
            </Form.Group>
            </Col>
            </Row>
        );
    }
}