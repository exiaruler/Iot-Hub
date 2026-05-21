'use client'
import { ChangeEventHandler, Component } from "react"
import { Col, Form, Row } from "react-bootstrap";
import FormHandle from "../../component-base/form/FormHandle";
import SelectBase from "../../component-base/input/SelectBase";
import Warning from "./Warning";

type Props={
    label:string,
    name?:string,
    valueKey:string,
    displayKey:string,
    options?:any,
    api?:string,
    required?:boolean,
    readOnly?:boolean,
    disable?:boolean,
    onChange?:ChangeEventHandler,
    warning?:string,
    value?:any,
    size?:any,
    md?:number,
    xs?:number,
    formRef?:FormHandle|any;
}
type state={
    options:any;
    value:Object|null;
    displayValue:string;
    objectValue:null|Object;
    
}
export default class FormGenSelect extends SelectBase{
    
    
    render(){
        return(
            <Row>
            <Col md={this.props.md} xs={this.props.xs}>
            <Form.Group>
            <Form.Label>{this.props.label}</Form.Label>
            <div className="mb-3">
            <Form.Select disabled={this.props.disable} style={{width:this.props.size}} id={this.props.name+"Select"} required={this.props.required} onChange={(event:any)=>this.onChange(event)}  name={this.props.name} value={this.getStateValue()}>
            {
                <option hidden={false}>{''}</option>
            }
            {
                this.state.options.map((opt:Record<string,any>,num:number)=>(
                    <option key={num} value={this.setValue(num)}>{opt[this.props.displayKey]}</option>
                ))
            }
            </Form.Select>
            </div>
            <Warning name={this.name}warning={this.getWarning()}/>
            </Form.Group>
            </Col>
            </Row>
        );
    }
}