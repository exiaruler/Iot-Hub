'use client';

import Warning from "@/components/formGenComponents/Warning";
import SelectBase from "@/component-base/SelectBase";
import { Col, Form, Row } from "react-bootstrap";

export default class SelectInput extends SelectBase{
    render(){
        return(
            <Row>
            <Col md={this.props.md} xs={this.props.xs}>
            <Form.Group>
            <Form.Label>{this.props.label}</Form.Label>
            <div className="mb-3">
            <Form.Select disabled={this.props.disable} style={{width:this.props.size}} id={this.props.name+"Select"} required={this.props.required} onChange={(event:any)=>this.onChange(event)}  name={this.props.name} value={this.getStateValue()||this.props.value}>
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