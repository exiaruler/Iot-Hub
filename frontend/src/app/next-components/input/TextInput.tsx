'use client'
import Warning from "@/components/formGenComponents/Warning";
import InputBase from "@/component-base/InputBase";
import { Row, Col, Form } from "react-bootstrap";
import { RecordContext } from "@/components/form/FormHandle";

export default class TextInput extends InputBase{
    static contextType = RecordContext;

    render(){
        return(
            <Row>
            <Col xs={this.props.xs} md={this.props.md}>
            <Form.Group hidden={this.props.hidden}>
            <Form.Label>{this.props.label}</Form.Label>
            <div className="mb-3">
            <Form.Control readOnly={this.props.readOnly} disabled={this.props.disable} style={{width:this.props.size}} id={this.props.name+"Text"} required={this.props.required} onChange={(event:React.ChangeEvent<HTMLInputElement>)=>this.onChange(event)}  name={this.props.name} type={this.type} value={this.getStateValue()} />
            </div>
            <Warning name={this.name} warning={this.getWarning()} ref={this.warningComponent}/>
            </Form.Group>
            </Col>
            </Row>
        );
    }
}