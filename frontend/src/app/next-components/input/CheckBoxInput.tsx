'use client'

import { FormGenCheckBox } from "@/components/formGenComponents/FormGenCheckBox";
import { Col, Form } from "react-bootstrap";

export default class CheckBoxInput extends FormGenCheckBox{
    render(){
        return(
            <Col md="3">
            <div className="mb-3">
            <Form.Group>
            <Form.Check id={this.componentId} required={this.props.required} reverse={true} label={this.props.label} checked={this.props.value} onChange={(event:React.ChangeEvent<HTMLInputElement>)=>this.onChange(event)} name={this.props.name} />
            <Form.Text id={this.props.name+"Warning"}>{this.props.warning} </Form.Text>
            </Form.Group>
            </div>
            </Col>
        );
    }
}