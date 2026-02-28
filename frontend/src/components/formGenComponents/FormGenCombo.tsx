'use client'
import { ChangeEventHandler, Component } from "react";
import { Form, Row } from "react-bootstrap";
import { props } from "./FieldProps";
type Props={
    label:string,
    name?:string,
    required?:boolean,
    onChange?:ChangeEventHandler
    warning?:string,
    value:any,
    size:any,
    valueKey:string,
    valueDisplayKey:string,
    data?:Array<any>,
    api?:string,
}
export class FormGenCombo extends Component<props>{
    constructor(props:any) {
        super(props);
        this.state = {
            options:[]
        };
    }
    private apiCall(){
        
    }
    render(){
        return(
            <Row >
            <Form.Group>
            <Form.Label>{this.props.label}</Form.Label>
            <div className="mb-3">
            <Form.Select id={this.props.name+"Select"} required={this.props.required} onChange={this.props.onChange}  name={this.props.name}>
            {
                
            }
            </Form.Select>
            <Form.Control id={this.props.name+"Text"} required={this.props.required} onChange={this.props.onChange}  name={this.props.name} type={this.props.type} defaultValue={this.props.value} size={this.props.size} />
            </div>
            <Form.Text id={this.props.name+"Warning"}>{this.props.warning} </Form.Text>
            </Form.Group>    
            </Row>
        );
    }
}