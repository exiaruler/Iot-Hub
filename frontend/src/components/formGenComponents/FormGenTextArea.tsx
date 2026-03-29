import { Col, Form, Row } from "react-bootstrap";
import { FormGenText } from "./FormGenText";
import InputBase from "../../component-base/InputBase";
import Warning from "./Warning";

export class FormGenTextArea extends InputBase{
    private rows=this.props.rows;
    render(){
        return(
            <Row>
            <Col md={this.props.md} xs={this.props.xs}>
            <Form.Group>
            <Form.Label>{this.props.label}</Form.Label>
            <Form.Control onChange={(event:React.ChangeEvent<HTMLInputElement>)=>this.onChange(event)}  spellCheck={true} id={this.props.name+"Text"} required={this.props.required} name={this.props.name} as="textarea" rows={this.rows} value={this.getStateValue()}/>
            <Warning name={this.name} warning={this.getWarning()}/>
            </Form.Group>
            </Col>
            </Row>
        );
    }
}