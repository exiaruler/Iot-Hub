'use client'

import { Row, Col, Form } from "react-bootstrap";
import TextInput from "../input/TextInput";
import Warning from "@/components/formGenComponents/Warning";
import { RecordContext } from "@/components/form/FormHandle";

export default class AngleInput extends TextInput{
    static contextType = RecordContext;
    public value=0;
    public type='number';
    render(){
        return(
            <Row>
                        <Col xs={this.props.xs} md={this.props.md}>
                        <Form.Group hidden={this.props.hidden}>
                        <Form.Label>{this.props.label}</Form.Label>
                        <div className="mb-3">
                        <Form.Control readOnly={this.props.readOnly} disabled={this.props.disable} style={{width:this.props.size}} id={this.props.name+"Text"} required={this.props.required} onChange={(event:React.ChangeEvent<HTMLInputElement>)=>this.onChange(event)}  name={this.props.name} type={'number'} value={this.getStateValue()||this.props.value} />
                        </div>
                        <Warning name={this.name} warning={this.getWarning()} ref={this.warningComponent}/>
                        </Form.Group>
                        </Col>
                        </Row>
        );
    }
}