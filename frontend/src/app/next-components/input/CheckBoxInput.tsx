'use client'

import { FormGenCheckBox } from "@/components/formGenComponents/FormGenCheckBox";
import Warning from "@/components/formGenComponents/Warning";
import CheckBase from "@/components/input/CheckBase";
import { Col, Form } from "react-bootstrap";

export default class CheckBoxInput extends CheckBase{
    public componentId=this.props.name+"Check";
     render(){
            return(
                <Col md="3">
                <div className="mb-3">
                <Form.Group hidden={this.props.hidden}>
                <Form.Check id={this.componentId} required={this.props.required} reverse={true} label={this.props.label} checked={this.getStateValue()||this.props.value} onChange={(event:React.ChangeEvent<HTMLInputElement>)=>this.onChange(event)} name={this.props.name} />
                <Warning name={this.name} warning={this.getWarning()}  ref={this.warningComponent}/>
                </Form.Group>
                </div>
                </Col>
            );
        }
}